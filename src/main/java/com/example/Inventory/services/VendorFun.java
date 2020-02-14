package com.example.Inventory.services;

import com.example.Inventory.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
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
    @Autowired
    private ProductFun productFun ;
    @Autowired
    private PasswordEncoder passwordEncoder ;
    public VendorFun() throws IOException {
    }

//    private static String getSecurePassword(String passwordToHash) {
//        String generatedPassword = null;
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(passwordToHash.getBytes());
//            byte[] bytes = md.digest();
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < bytes.length; i++) {
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            generatedPassword = sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return generatedPassword;
//    }

    public Vendor saveVendor(Vendor vendor) {
        String vendor_id = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendor_id));
        Vendor vendorDb = mongoTemplate.findOne(query, Vendor.class);
        if (vendorDb == null) {
            logger.info("VendorID: " + vendor_id + " signed up");
            vendor.setPassword(passwordEncoder.encode(vendor.getPassword()));
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

        String currentVendorId = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(currentVendorId));
        Vendor vendorDb = mongoTemplate.findOne(query, Vendor.class);

        if (vendorDb == null) {
            logger.warning("VendorID: " + currentVendorId + " tried to login without prior registration");
            return "";
        }

        if (passwordEncoder.matches(vendor.getPassword(),vendorDb.getPassword())) {
            logger.info("VendorID: " + currentVendorId + " logged in successfully.");
            return "successful-login";
        } else {
            logger.warning("Password check failed VendorID: " + currentVendorId);
        }
        return "";
    }

    public List<ViewReport> getSellReport(String vendorId,String sortBy) {
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorId));
        List<Order> ls = mongoTemplate.find(query, Order.class);
//        Collections.sort(ls);
//        Collections.reverse(ls);

        List<ViewReport> vr = new ArrayList<ViewReport>();

        for (int i = 0; i < ls.size(); i++) {
            Order currentOrder = ls.get(i);
            String currentVendorId = currentOrder.getVendorId();
            String currentProductId = currentOrder.getProdId() ;
            String currentUserId = currentOrder.getUserId() ;

            Query getVendor = new Query();
            getVendor.addCriteria(Criteria.where("vendorId").is(currentVendorId));
            Vendor vendor = mongoTemplate.findOne(getVendor, Vendor.class);

            Product product = productFun.findProductById(currentProductId) ;

            Query getUser = new Query();
            getUser.addCriteria(Criteria.where("user_id").is(currentUserId));
            User user = mongoTemplate.findOne(getUser, User.class);

            Date orderDate = currentOrder.getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = dateFormat.format(orderDate);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
            String time = timeFormat.format(orderDate);
            ViewReport viewReport = new ViewReport(product, vendor.getVendorName(), currentOrder.getQ(), user.getUser_id(), user.getName(),date + " , " + time);
            vr.add(viewReport);
        }
        if(sortBy.equals("date"))
        {
            Collections.sort(vr, Comparator.comparing(ViewReport::getTimestamp));
            Collections.reverse(vr) ;
        }
        else if(sortBy.equals("qty"))
        {
            Collections.sort(vr,Comparator.comparing(ViewReport::getQty));
        }
        else
        {
            Collections.sort(vr);
        }
        return vr;
    }
}
