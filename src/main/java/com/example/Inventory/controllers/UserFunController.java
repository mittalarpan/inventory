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
        user.setPermissions("ROLE_USER");
        user.setRoles("USER");
        return userFun.saveUser(user);
    }

    @GetMapping("/inventory")
    public List<User> getAllUsers() {
        return userFun.getAllUsers();
    }

    @CrossOrigin
    @PostMapping(value = "/inventory/login")
    public UserToken checkUser(@RequestParam(name = "user_id") String userId, @RequestParam(name = "password") String password) {

        boolean chk = userFun.checkUser(userId, password);
        if (chk) {
            return new UserToken("success-login");
        } else {
            return new UserToken("");
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

        List<Product> recentBuys = new ArrayList<>();
        for (int i = 0; i < Math.min(5, orders.size()); i++) {

            String productId = orders.get(i).getProdId();
            Product product = productFun.findProductById(productId);
            recentBuys.add(product);
        }
        return recentBuys;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/home/getProduct")
    public List<ViewSupply> getProduct(@RequestParam(name = "prodId") String prodId) {
        return supplyFun.findProductByTransaction(prodId);
    }

    @CrossOrigin
    @PostMapping("inventory/user/supply")
    public boolean updateSupply(@RequestParam(name = "vendorId") String vendorId, @RequestParam(name = "qty") int qty, @RequestParam(name = "user_id") String user_id, @RequestParam(name = "price") int price, @RequestParam(name = "prodId") String prodId) {
        supplyFun.addUserTransaction(vendorId, prodId, qty, price);
        supplyFun.reduceSupply(vendorId, prodId, qty, user_id);
        return true;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/logout")
    public boolean userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("token");
        return true;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/purchaseReport")
    public List<ViewReport> purchaseReport(@RequestParam(name = "user_id") String user_id, @RequestParam(name = "selected") String sortBy) {
        System.out.println("selected : " + sortBy);
        return userFun.getPurchaseReport(user_id, sortBy);
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
    //Giving list of products that are available in the market
    public List<Product> getProducts(@RequestParam(name = "search_query") String searchQuery) {
        List<Product> products = productFun.findProductByNameAndDescription(searchQuery);
        List<Product> current_list = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
            Query query = new Query();
            String prod_id = products.get(i).getProdId();
            query.addCriteria(Criteria.where("prodId").is(prod_id));
            Transaction temp = mongoTemplate.findOne(query, Transaction.class);
            if (temp != null) {
                current_list.add(products.get(i));
            }
        }
        Collections.sort(current_list);
        return current_list;
    }
}
