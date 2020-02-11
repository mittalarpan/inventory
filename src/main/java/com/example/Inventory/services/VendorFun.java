package com.example.Inventory.services;

import com.example.Inventory.models.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@Service
public class VendorFun {
    public Logger logger = Logger.getLogger("myLogger");
    public FileHandler fileHandler = new FileHandler("/Users/havyapanchal/Desktop/LogFiles/logs_1.log");
    @Autowired
    private UserFun userFun;
    @Autowired
    private MongoTemplate mongoTemplate;

    public VendorFun() throws IOException {
    }

    private static String getSecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public Vendor saveVendor(Vendor vendor) {
        String vendor_id = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendor_id));
        Vendor chk = mongoTemplate.findOne(query, Vendor.class);
        if (chk == null) {
            logger.info("VendorID: " + vendor_id + " signed up");
            vendor.setPassword(getSecurePassword(vendor.getPassword()));
            return mongoTemplate.save(vendor);
        } else {
            logger.warning("VendorID: " + vendor_id + " tried to create account with email that is already registered");
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
            logger.warning("VendorID: " + vendorid + " tried to login without prior registration");
            return "";
        }

        String pass = getSecurePassword(vendor.getPassword());
        if (chk.getPassword().equals(pass)) {
            vendor.setVendorName(chk.getVendorName());
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[20];
            random.nextBytes(bytes);
            String token = bytes.toString();
            logger.info("VendorID: " + vendorid + " logged in successfully.");
            return token;
        } else {
            logger.warning("Password check failed VendorID: " + vendorid);
        }
        return "";
    }
}
