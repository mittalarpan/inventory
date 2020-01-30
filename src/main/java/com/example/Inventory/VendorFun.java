package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class VendorFun {
    public Logger logger = Logger.getLogger("MyLog");
    public FileHandler logFileHandler = new FileHandler("/Users/havyapanchal/Desktop/Logs/MyLogFile.log", true);
    public VendorFun() throws IOException {}
    @Autowired
    private MongoTemplate mongoTemplate;

    public Vendor saveVendor(Vendor vendor) {
        logger.addHandler(logFileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter() ;
        logFileHandler.setFormatter(simpleFormatter);
        String vendorId = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorId));
        Vendor chk = mongoTemplate.findOne(query, Vendor.class);
        if (chk == null){
            logger.info("New user with User ID: " + vendorId + "signed up");
            return mongoTemplate.save(vendor);}
        else {
            logger.warning("User ID: " + vendorId + " attempted to create account with email that is already registered");
            return new Vendor("", "", "");
        }
    }

    public List<Vendor> getAllVendors() {
        return mongoTemplate.findAll(Vendor.class);
    }

    public boolean checkVendor(Vendor vendor) {
        logger.addHandler(logFileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        logFileHandler.setFormatter(simpleFormatter);
        String vendorId = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorId));
        Vendor chk = mongoTemplate.findOne(query, Vendor.class);

        if (chk == null){
            logger.warning("User ID: " + vendorId + " tried to login without prior registration");
            return false;
        }

        String pass = vendor.getPassword();

        if (chk.getPassword().equals(pass)) {
            vendor.setVendorName(chk.getVendorName());
            logger.info("User ID: " + vendorId + " logged in successfully.") ;
            return true;
        }
        else
        {
            logger.warning("Password check failed for User ID: " + vendorId);
        }
        return false;
    }

   /* public Vendor vendorDetails(String vendorId)
    {
        Query query = new Query() ;
        query.addCriteria(Criteria.where("vendorId").is(vendorId)) ;
        Vendor chk = mongoTemplate.findOne(query , Vendor.class) ;
        return chk ;
    }*/
}
