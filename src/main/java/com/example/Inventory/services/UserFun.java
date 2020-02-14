package com.example.Inventory.services;

import com.example.Inventory.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class UserFun {
    public Logger logger = Logger.getLogger("myLogger");
    public FileHandler fileHandler = new FileHandler("/Users/havyapanchal/Desktop/LogFiles/logs_1.log");
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductFun productFun ;
    @Autowired
    private MongoTemplate mongoTemplate;
    private String token = "";

    public UserFun() throws IOException {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User saveUser(User user) {
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        String userId = user.getUser_id();
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        User userDb = mongoTemplate.findOne(query, User.class);
        if (userDb == null) {
            logger.info("New User signed up with userID: " + user.getUser_id());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return mongoTemplate.save(user);
        } else {
            logger.info("UserID: " + user.getUser_id() + " tried to create account with prior registered email");
            return new User("", "", "");
        }
    }

    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    public boolean checkUser(String userId, String password) {
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        User userDb = mongoTemplate.findOne(query, User.class);

        if (userDb == null) {
            logger.warning("UserID: " + userDb.getUser_id() + " tried to login without prior registration");
            return false;
        }

        if (passwordEncoder.matches(password, userDb.getPassword())) {
            logger.info("User ID: " + userId + " logged in successfully.");
            return true;
        } else {
            logger.info("User ID: " + userId + " failed to attempt login.");
        }
        return false;
    }

    public List<ViewReport> getPurchaseReport(String user_id,String sortBy) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user_id));
        List<Order> ls = mongoTemplate.find(query, Order.class);
//        Collections.sort(ls);
//        Collections.reverse(ls);

        List<ViewReport> vr = new ArrayList<ViewReport>();

        for (int i = 0; i < ls.size(); i++) {
            Order currentOrder = ls.get(i);
            String currentVendorId = currentOrder.getVendorId();
            String currentProductId = currentOrder.getProdId();
            String currentUserId = currentOrder.getUserId() ;

            Query getVendor = new Query();
            getVendor.addCriteria(Criteria.where("vendorId").is(currentVendorId));
            Vendor currentVendor = mongoTemplate.findOne(getVendor, Vendor.class);

            Product currentProduct = productFun.findProductById(currentProductId) ;

            Query getUser = new Query();
            getUser.addCriteria(Criteria.where("user_id").is(currentUserId));
            User currentUser = mongoTemplate.findOne(getUser, User.class);

            Date orderDate = currentOrder.getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = dateFormat.format(orderDate);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
            String time = timeFormat.format(orderDate);
            ViewReport viewReport = new ViewReport(currentProduct, currentVendor.getVendorName(), currentOrder.getQ(), currentUserId, currentUser.getName(),date + " , " + time);
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
