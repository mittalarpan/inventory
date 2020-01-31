package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@Service
public class VendorFun {
    public Logger logger = Logger.getLogger("myLogger");
    public FileHandler fileHandler = new FileHandler("/Users/arpanmittal/Desktop/LogFiles/logs.log");
    @Autowired
    private MongoTemplate mongoTemplate;

    public VendorFun() throws IOException {
    }

    public Vendor saveVendor(Vendor vendor) {
        String vendorid = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorid));
        Vendor chk = mongoTemplate.findOne(query, Vendor.class);
        if (chk == null) {
            logger.info("VendorID: " + vendorid + " singed up");
            return mongoTemplate.save(vendor);
        } else {
            logger.warning("VendorID: " + vendorid + " tried to create account with email that is already registered");
            return new Vendor("", "", "");
        }
    }

    public List<Vendor> getAllVendors() {
        return mongoTemplate.findAll(Vendor.class);
    }

    public String checkVendor(Vendor vendor, HttpServletRequest request) {

        String vendorid = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorid));
        Vendor chk = mongoTemplate.findOne(query, Vendor.class);

        if (chk == null) {
            logger.warning("VendorID: "+ vendorid + " tried to login without prior registration");
            return "";
        }

        String pass = vendor.getPassword();

        if (chk.getPassword().equals(pass)) {
            vendor.setVendorName(chk.getVendorName());
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[20];
            random.nextBytes(bytes);
            String token = bytes.toString();
            logger.info("VendorID: " + vendorid + " logged in successfully.") ;
            return token;
        }
        else
        {
            logger.warning("Password check failed VendorID: " + vendorid);
        }
        return "";
    }

   /* public Vendor vendorDetails(String vendorId)
    {
        Query query = new Query() ;
        query.addCriteria(Criteria.where("vendorId").is(vendorId)) ;
        Vendor chk = mongoTemplate.findOne(query , Vendor.class) ;
        return chk ;
    }*/
}
