package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class UserFunController {
    public Logger logger = Logger.getLogger("MyLog");
    public FileHandler logFileHandler = new FileHandler("/Users/havyapanchal/Desktop/Logs/MyLogFile.log",true);
    public UserFunController() throws IOException{}
    @Autowired
    private UserFun userFun;
    @Autowired
    private SupplyFun supplyFun;
    @Autowired
    private MongoTemplate mongoTemplate ;

    @CrossOrigin
    @PostMapping("/inventory/signUp")
    public User saveUser(@RequestBody User user) {
        return userFun.saveUser(user);
    }

    @GetMapping("/inventory")
    public List<User> getAllUsers() {
        return userFun.getAllUsers();
    }

    @CrossOrigin
    @RequestMapping(value = "/inventory/login", method={RequestMethod.GET, RequestMethod.POST})
    public UserToken checkUser(@RequestParam(name="user_id") String user_id , @RequestParam (name="password") String password , HttpServletRequest request) {

        User user = new User() ;
        user.setName("");
        user.setUser_id(user_id);
        user.setPassword(password) ;
        userFun.checkUser(user , request) ;
        String token = (String)request.getSession().getAttribute("token") ;
        return new UserToken(token) ;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/home")
    public List<ViewSupply> getAllSupply() {
        return supplyFun.getSupply();
    }

    @CrossOrigin
    @PostMapping("inventory/user/supply")
    public boolean updateSupply(@RequestParam(name = "vendorId") String vendorId, @RequestParam(name = "prodId") String prodId, @RequestParam(name = "qty") int qty , @RequestParam(name = "user_id") String user_id) {
        return supplyFun.updateSupplyUser(vendorId, prodId, qty , user_id);
    }

    @CrossOrigin
    @GetMapping("/inventory/user/logout")
    public boolean userLogout(HttpServletRequest request){
        request.getSession().removeAttribute("token") ;
        // logout function log is still pending here
        return true ;
    }

    @CrossOrigin
    @GetMapping("/inventory/user/purchaseReport")
    public List<Order> sellReport(@RequestParam (name = "user_id") String user_id){

        Query query = new Query() ;
        query.addCriteria(Criteria.where("userId").is(user_id)) ;
        List<Order> ls = mongoTemplate.find(query , Order.class) ;
        return ls ;
    }
}
