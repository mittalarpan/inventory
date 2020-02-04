package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class VendorFunController {

    @Autowired
    private VendorFun vendorFun;

    @Autowired
    private ProductFun productFun;

    @Autowired
    private MongoTemplate mongoTemplate;

    @CrossOrigin
    @PostMapping("/inventory/vendor/signUp")
    public Vendor saveVendor(@RequestBody Vendor vendor) {
        return vendorFun.saveVendor(vendor);
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor")
    public List<Vendor> getAllVendors() {
        return vendorFun.getAllVendors();
    }

    @CrossOrigin
    @PostMapping("/inventory/vendor/login")
    public VendorToken checkVendor(@RequestBody Vendor vendor, HttpServletRequest request) { //vendor login check
        String vendorToken = vendorFun.checkVendor(vendor, request);
        return new VendorToken(vendorToken);
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/login")
    public List<Product> getAllProducts(@RequestParam(name = "vendorId") String vendorId) {
        System.out.println(vendorId);
        return productFun.getAllProducts();
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/home")
    public List<Map<Object, Object>> getAllOrders(@RequestParam(name = "vendorId") String vendor_id) {
        List<Supply> supplies = mongoTemplate.query(Supply.class).all();
        List<Supply> vendorSupply = new ArrayList<Supply>();
        for (int i = 0; i < supplies.size(); i++) {
            Supply current = supplies.get(i);
            if (current.getId().getVendorId().equals(vendor_id)) {
                vendorSupply.add(current);
            }
        }
        Collections.sort(vendorSupply, Comparator.comparing(Supply::getTimestamp));
        Collections.reverse(vendorSupply);
        List<Map<Object, Object>> mapList = new ArrayList<Map<Object, Object>>();

        for (int i = 0; i < vendorSupply.size(); i++) {
            Map<Object, Object> mp = new HashMap<Object, Object>();
            String product_id = vendorSupply.get(i).getId().getProdId();
            int price = vendorSupply.get(i).getPrice();
            int qty = vendorSupply.get(i).getQty();
            Query query = new Query();
            query.addCriteria(Criteria.where("prodId").is(product_id));
            Product product = mongoTemplate.findOne(query, Product.class);
            mp.put("product", product);
            mp.put("qty", qty);
            mp.put("price", price);
            mapList.add(mp);
        }
        return mapList;
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/sellReport")
    public List<ViewReport> sellReport(@RequestParam(name = "vendorId") String vendorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorId));
        List<Order> ls = mongoTemplate.find(query, Order.class);
        Collections.sort(ls);
        List<ViewReport> vr = new ArrayList<ViewReport>();
        for (int i = 0; i < ls.size(); i++) {
            Order order = ls.get(i);
            String vendorIdd = order.getVendorId();
            Query getVendor = new Query();
            getVendor.addCriteria(Criteria.where("vendorId").is(vendorIdd));
            Vendor vendor = mongoTemplate.findOne(getVendor, Vendor.class);
            Query getProduct = new Query();
            getProduct.addCriteria(Criteria.where("prodId").is(order.getProdId()));
            Product product = mongoTemplate.findOne(getProduct, Product.class);
            Query getUser = new Query();
            getUser.addCriteria(Criteria.where("user_id").is(order.getUserId()));
            User user = mongoTemplate.findOne(getUser, User.class);
            ViewReport viewReport = new ViewReport(product, vendor.getVendorName(), order.getQ(), user.getUser_id(), user.getName(), order.getTimestamp());
            vr.add(viewReport);
        }
        return vr;
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/accountDetails")
    public Vendor getVendorDetails(@RequestParam(name = "vendorId") String vendorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorId));
        Vendor vendor = mongoTemplate.findOne(query, Vendor.class);
        return vendor;
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/search")
    public List<Product> getProducts(@RequestParam(name = "search_query") String prodName) {
        List<Product> products = productFun.getQueryProducts(prodName);
        Collections.sort(products);
        return products;
    }

    @CrossOrigin
    @GetMapping("inventory/vendor/getProduct")
    public Product getProduct(@RequestParam(name = "prodId") String prodId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("prodId").is(prodId));
        Product product = mongoTemplate.findOne(query, Product.class);
        return product;
    }
}
