//package com.example.Inventory.Db;
//
//
//import com.example.Inventory.models.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class DbInit implements CommandLineRunner {
//    @Autowired
//    private MongoTemplate mongoTemplate ;
//    @Autowired
//    private PasswordEncoder passwordEncoder ;
//    public DbInit(MongoTemplate mongoTemplate){
//        this.mongoTemplate = mongoTemplate ;
//    }
//    @Override
//    public void run(String... args) {
//        /*this.userRepository.deleteAll();
//        User user = new User(1,"user",passwordEncoder.encode("user123"),"USER","") ;
//        User admin = new User(2,"admin",passwordEncoder.encode("admin123"),"ADMIN","API1,API2,ROLE_ADMIN") ;
//        User manager = new User(3,"manager",passwordEncoder.encode("manager123"),"MANAGER","API1,ROLE_MANAGER") ;
//        this.userRepository.save(user);
//        this.userRepository.save(admin);
//        this.userRepository.save(manager);*/
//    }
//}
//
