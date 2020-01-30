package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VendorFunController {

    @Autowired
    private VendorFun vendorFun;

    @Autowired
    private ProductFun productFun;

    @Autowired
    private MongoTemplate mongoTemplate ;

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
    public boolean checkVendor(@RequestBody Vendor vendor) {
        vendor.setVendorName("");
        return vendorFun.checkVendor(vendor);
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/login")
    public List<Product> getAllProducts(@RequestParam(name = "vendorId") String vendorId) {
        return productFun.getAllProducts();
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/sellReport")
    public List<Order> sellReport(@RequestParam (name = "vendorId") String vendorId){
        Query query = new Query() ;
        query.addCriteria(Criteria.where("vendorId").is(vendorId)) ;
        List<Order> ls = mongoTemplate.find(query , Order.class) ;
        return ls ;
    }


   /*@CrossOrigin
    @GetMapping("/inventory/try")
    public Vendor getVendor(@RequestParam(name = "vendorId") String vendorId)
    {
        return vendorFun.vendorDetails(vendorId) ;
    }*/
}
