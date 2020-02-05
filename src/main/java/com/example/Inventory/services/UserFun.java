package com.example.Inventory.services;

import com.example.Inventory.models.User;
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
import java.util.logging.SimpleFormatter;

@Service
public class UserFun {
    public Logger logger = Logger.getLogger("myLogger");
    public FileHandler fileHandler = new FileHandler("/Users/havyapanchal/Desktop/LogFiles/logs_1.log");
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
        String userid = user.getUser_id();
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userid));
        User chk = mongoTemplate.findOne(query, User.class);
        if (chk == null) {
            logger.info("New User signed up with userID: " + user.getUser_id());
            return mongoTemplate.save(user);
        } else {
            logger.info("UserID: " + user.getUser_id() + " tried to create account with prior registered email");
            return new User("", "", "");
        }
    }

    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    public boolean checkUser(User user, HttpServletRequest request) {
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        String userid = user.getUser_id();
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userid));
        User chk = mongoTemplate.findOne(query, User.class);

        if (chk == null) {
            logger.warning("UserID: " + user.getUser_id() + " tried to login without prior registration");
            return false;
        }

        String pass = user.getPassword();

        if (chk.getPassword().equals(pass)) {
            user.setName(chk.getName());
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[20];
            random.nextBytes(bytes);
            token = bytes.toString();
            request.getSession().setAttribute("token", token);
            logger.info("UserID: " + user.getUser_id() + " logged in successfully.") ;
            return true;
        }

        return false;
    }
}
