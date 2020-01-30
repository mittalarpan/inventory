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
import java.util.logging.SimpleFormatter;

@Service
public class UserFun {
    public Logger logger = Logger.getLogger("MyLog");
    public FileHandler logFileHandler = new FileHandler("/Users/havyapanchal/Desktop/Logs/MyLogFile.log", true);
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
        logger.addHandler(logFileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        logFileHandler.setFormatter(formatter);
        String userId = user.getUser_id();
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        User chk = mongoTemplate.findOne(query, User.class);
        if (chk == null) {
            logger.info("New user with User ID: " + userId + "signed up");
            return mongoTemplate.save(user);
        } else {
            logger.warning("User ID: " + userId + " attempted to create account with email that is already registered");
            return new User("", "", "");
        }
    }

    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    public void checkUser(User user, HttpServletRequest request) {
        logger.addHandler(logFileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        logFileHandler.setFormatter(formatter);
        String userId = user.getUser_id();
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        User chk = mongoTemplate.findOne(query, User.class);
        if (chk == null) {
            logger.warning("User ID: " + userId + " tried to login without prior registration");
        }
        String pass = user.getPassword();

        if (chk.getPassword().equals(pass)) {
            user.setName(chk.getName());
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[20];
            random.nextBytes(bytes);
            token = bytes.toString();
            request.getSession().setAttribute("token", token);
            logger.info("User ID: " + userId + " logged in successfully.") ;
        }
        else{
            logger.warning("Password check failed for User ID: " + userId);
        }
    }
}
