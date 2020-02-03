package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import javax.swing.text.View;
import java.util.ArrayList;
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
    public VendorToken checkVendor(@RequestBody Vendor vendor, HttpServletRequest request) { //vendor login check
        String vendorToken =  vendorFun.checkVendor(vendor,request) ;
        return new VendorToken(vendorToken) ;
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/login")
    public List<Product> getAllProducts(@RequestParam(name = "vendorId") String vendorId) {
        System.out.println(vendorId);
        return productFun.getAllProducts();
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/sellReport")
    public List<ViewReport> sellReport(@RequestParam (name = "vendorId") String vendorId){
        Query query = new Query() ;
        query.addCriteria(Criteria.where("vendorId").is(vendorId)) ;
        List<Order> ls = mongoTemplate.find(query , Order.class) ;
        List<ViewReport> vr = new ArrayList<ViewReport>() ;
        for(int i=0;i<ls.size();i++){
            Order order = ls.get(i) ;
            String vendorIdd = order.getVendorId() ;
            Query getVendor = new Query() ;
            getVendor.addCriteria(Criteria.where("vendorId").is(vendorIdd)) ;
            Vendor vendor = mongoTemplate.findOne(getVendor , Vendor.class) ;
            Query getProduct = new Query() ;
            getProduct.addCriteria(Criteria.where("prodId").is(order.getProdId())) ;
            Product product = mongoTemplate.findOne(getProduct , Product.class) ;
            System.out.println(product.getProdId() + " vendorId");

            Query getUser = new Query() ;
            getUser.addCriteria(Criteria.where("user_id").is(order.getUserId())) ;
            User user = mongoTemplate.findOne(getUser , User.class) ;

            ViewReport viewReport = new ViewReport(product , vendor.getVendorName() , order.getQ() , user.getUser_id(),user.getName()) ;

            vr.add(viewReport) ;
        }
        return vr ;
    }

    @CrossOrigin
    @GetMapping("/inventory/vendor/accountDetails")
    public Vendor getVendorDetails(@RequestParam(name = "vendorId") String vendorId){
        Query query = new Query() ;
        query.addCriteria(Criteria.where("vendorId").is(vendorId)) ;
        Vendor vendor = mongoTemplate.findOne(query , Vendor.class) ;
        return vendor ;
    }

   /*@CrossOrigin
    @GetMapping("/inventory/try")
    public Vendor getVendor(@RequestParam(name = "vendorId") String vendorId)
    {
        return vendorFun.vendorDetails(vendorId) ;
    }*/
}
