package com.example.Inventory.controllers;

import com.example.Inventory.models.*;
import com.example.Inventory.services.ProductFun;
import com.example.Inventory.services.SupplyFun;
import com.example.Inventory.services.UserFun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class UserFunController {

    @Autowired
    private UserFun userFun;
    @Autowired
    private SupplyFun supplyFun;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProductFun productFun;

    @CrossOrigin
    @PostMapping("/inventory/signUp")
    public User saveUser(@RequestBody User user) {
//        return mongoTemplate.save(user,"user") ;
        return userFun.saveUser(user);
    }

    @GetMapping("/inventory")
    public List<User> getAllUsers() {
        return userFun.getAllUsers();
    }

    @CrossOrigin
    @PostMapping(value = "/inventory/login")
    public/* ResponseEntity<Boolean> */ UserToken checkUser(@RequestParam(name = "user_id") String user_id, @RequestParam(name = "password") String password, HttpServletRequest request) {

        User user = new User();
        user.setName("");
        user.setUser_id(user_id);
        user.setPassword(password);
        boolean chk = userFun.checkUser(user, request);
        if (chk) {
            String token = (String) request.getSession().getAttribute("token");
            return new UserToken(token);
        } else {
            String token = "";
            return new UserToken(token);
        }
    }

    @CrossOrigin
    @GetMapping("/inventory/user/home")
    public List<Product> getAllOrders(@RequestParam(name = "user_id") String user_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user_id));
        List<Order> orders = mongoTemplate.find(query, Order.class);
        Collections.sort(orders);
        Collections.reverse(orders);
        System.out.println(orders.size());
        List<Product> recentBuys = new ArrayList<Product>();
        for (int i = 0; i < Math.min(5, orders.size()); i++) {
            //Query query1 = new Query() ;
            Product product = mongoTemplate.findById(orders.get(i).getProdId(), Product.class);
            System.out.println(product.getProdId() + " " + product.getProdName());
            recentBuys.add(product);
        }
        return recentBuys;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/home/getProduct")
    public List<ViewSupply> getProduct(@RequestParam(name = "prodId") String prodId) {
        supplyFun.getProduct(prodId);
        return supplyFun.getProductFromTransaction(prodId);
    }

    @CrossOrigin
    @PostMapping("inventory/user/supply")
    public boolean updateSupply(@RequestParam(name = "vendorId") String vendorId, @RequestParam(name = "qty") int qty, @RequestParam(name = "user_id") String user_id, @RequestParam(name = "price") int price, @RequestParam(name="prodId") String prodId) {
        supplyFun.updateTransactionUser(vendorId, prodId, qty, price);
        supplyFun.updateSupplyUser(vendorId, prodId, qty, user_id);
        return true ;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/logout")
    public boolean userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("token");
        return true;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/purchaseReport")
    public List<ViewReport> purchaseReport(@RequestParam(name = "user_id") String user_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user_id));
        List<Order> ls = mongoTemplate.find(query, Order.class);
        Collections.sort(ls);
        Collections.reverse(ls);
        List<ViewReport> vr = new ArrayList<ViewReport>();
        for (int i = 0; i < ls.size(); i++) {
            Order order = ls.get(i);
            String vendorIdd = order.getVendorId();
            Query getVendor = new Query();
            getVendor.addCriteria(Criteria.where("vendorId").is(vendorIdd));
            Vendor vendor = mongoTemplate.findOne(getVendor, Vendor.class);

            Query getProduct = new Query();
            String prod_id = order.getProdId();
            getProduct.addCriteria(Criteria.where("prodId").is(prod_id));
            Product product = mongoTemplate.findOne(getProduct, Product.class);

            Query getUser = new Query();
            getUser.addCriteria(Criteria.where("user_id").is(order.getUserId()));
            User user = mongoTemplate.findOne(getUser, User.class);

            System.out.println(user.getUser_id());
            System.out.println(vendor.getVendorId());
            System.out.println(product.getProdId());
            ViewReport viewReport = new ViewReport(product, vendor.getVendorName(), order.getQ(), user.getUser_id(), user.getName(), order.getTimestamp());

            vr.add(viewReport);
        }
        return vr;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/accountDetails")
    public User getUserDetails(@RequestParam(name = "user_id") String user_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(user_id));
        User user = mongoTemplate.findOne(query, User.class);
        return user;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/search")
    public List<Product> getProducts(@RequestParam(name = "search_query") String prodName) {
        List<Product> products = productFun.getQueryProducts(prodName);
        Collections.sort(products);
        return products;
    }
}
