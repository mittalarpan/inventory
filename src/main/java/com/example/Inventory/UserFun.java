package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class UserFun{

    @Autowired
    private MongoTemplate mongoTemplate ;
    private String token="" ;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User saveUser(User user){

        String userid = user.getUser_id() ;
        Query query = new Query() ;
        query.addCriteria(Criteria.where("user_id").is(userid)) ;
        User chk = mongoTemplate.findOne(query , User.class) ;
        if(chk == null)
        return  mongoTemplate.save(user) ;
        else{
            return new User("","","") ;
        }
    }

    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class) ;
    }

     public boolean checkUser(User user, HttpServletRequest request){

        String userid = user.getUser_id() ;
        Query query = new Query() ;
        query.addCriteria(Criteria.where("user_id").is(userid)) ;
        User chk = mongoTemplate.findOne(query , User.class) ;

        if(chk==null)
            return false ;

        String pass = user.getPassword() ;

        if(chk.getPassword().equals(pass)){
            user.setName(chk.getName());
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[20];
            random.nextBytes(bytes);
            token = bytes.toString();
            request.getSession().setAttribute("token" , token) ;
            return true ;
        }

        return false ;
     }
}
