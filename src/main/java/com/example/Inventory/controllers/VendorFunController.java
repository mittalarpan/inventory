package com.example.Inventory.controllers;

import com.example.Inventory.models.*;
import com.example.Inventory.services.ProductFun;
import com.example.Inventory.services.VendorFun;
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
    public VendorToken checkVendor(@RequestBody Vendor vendor, HttpServletRequest request) {
        String vendorToken = vendorFun.checkVendor(vendor, request);
        return new VendorToken(vendorToken);
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/login")
    public List<Product> getAllProducts(@RequestParam(name = "vendorId") String vendorId) {
        //System.out.println(vendorId);
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
            Product product = productFun.findProductById(product_id);
            mp.put("product", product);
            mp.put("qty", qty);
            mp.put("price", price);
            mapList.add(mp);
        }
        return mapList;
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/sellReport")
    public List<ViewReport> sellReport(@RequestParam(name = "vendorId") String vendorId,
                                       @RequestParam(name = "selected") String sortBy) {

        return vendorFun.getSellReport(vendorId, sortBy);
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
    //List of products that are available to sell
    public List<Product> getProducts(@RequestParam(name = "search_query") String searchQuery) {
        List<Product> list = productFun.findProductByNameAndDescription(searchQuery);
        List<Product> products = new ArrayList<Product>(list) ;
        Collections.sort(products);
        return products;
    }

    @CrossOrigin
    @GetMapping("inventory/vendor/getProduct")
    public Product getProduct(@RequestParam(name = "prodId") String productId) {
        return productFun.findProductById(productId);
    }
}
