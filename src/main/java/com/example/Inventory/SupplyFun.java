package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.sql.Timestamp ;

import java.io.IOException;
import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
//import java
@Service
public class SupplyFun {
    public Logger logger = Logger.getLogger("myLogger") ;
    public FileHandler fileHandler = new FileHandler("/Users/havyapanchal/Desktop/LogFiles/logs.log");
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ProductFun productfun ;
    public SupplyFun() throws IOException {
    }

    public Supply addSupply(Supply supply) {
        Query query = new Query();

        return mongoTemplate.save(supply);
    }

    public List<Product> getSupply() {
        /*List<ViewSupply> ret = new ArrayList<ViewSupply>();
        List<Supply> ls = mongoTemplate.findAll(Supply.class);


        for(int i=0;i<ls.size();i++){
            ViewSupply viewSupply = new ViewSupply() ;
            Query query = new Query() ;
            query.addCriteria(Criteria.where("prodId").is(ls.get(i).getId().getProdId())) ;
            Product product = mongoTemplate.findOne(query , Product.class) ;
            System.out.println(product.getProdId());
            Query query1 = new Query() ;
            query1.addCriteria(Criteria.where("vendorId").is(ls.get(i).getId().getVendorId())) ;
            Vendor vendor = mongoTemplate.findOne(query1 , Vendor.class) ;
            System.out.println(vendor.getVendorId()+ " x");
            if(product != null && vendor != null)
            ret.add(new ViewSupply(product , vendor , ls.get(i).getQty())) ;
        }
        return ret ;*/
        return productfun.getAllProducts() ;
    }

    public boolean updateSupplyUser(String vendorId, String prodId, int qty, String user_id) {
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter() ;
        fileHandler.setFormatter(simpleFormatter);
        Query query = new Query();

        Supply.CompositeKey temp = new Supply.CompositeKey(prodId, vendorId);

        query.addCriteria(Criteria.where("id").is(temp));

        String order_id;

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        order_id = bytes.toString();
        Calendar calendar = Calendar.getInstance();
        Date timestamp = (Date) calendar.getTime();

        Order order = new Order(order_id, user_id, prodId, vendorId, qty , timestamp);
        System.out.println("In order" + order.getVendorId()) ;
        mongoTemplate.save(order);

        Supply supply = mongoTemplate.findOne(query, Supply.class);
        System.out.println(supply.getQty()) ;
        if (supply.getQty() < qty) {
            System.out.println("False");
            logger.warning("Invalid attempt for purchase by UserID: "+ user_id);
            return false;
        } else {
            supply.setQty(supply.getQty() - qty);
            mongoTemplate.save(supply);
            System.out.println("True");
            logger.info("UserID: " + user_id + " purchased ProdID: " + prodId
            + " from VendorID: " + vendorId + " of qty: " + qty) ;
            return true;
        }

    }

    public void updateSupplyVendor(String vendorId, String prodId, int qty , int price) {
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter() ;
        fileHandler.setFormatter(simpleFormatter);
        Query query = new Query();

        Supply.CompositeKey temp = new Supply.CompositeKey(prodId, vendorId);

        query.addCriteria(Criteria.where("id").is(temp));

        Supply supply = mongoTemplate.findOne(query, Supply.class);

        if (supply == null) {
            Supply supp = new Supply(qty, temp , price);
            logger.info("VendorID: " + vendorId + " initiated supply of ProdID: " + prodId);
            mongoTemplate.save(supp);
        } else {
            supply.setQty(supply.getQty() + qty);
            logger.info("Vendor: " + vendorId + " increased supply for productID: " + prodId
            + " by qty: " + qty) ;
            mongoTemplate.save(supply);
        }

    }

    public List<ViewSupply> getProduct(String prodId) {
        Query query = new Query() ;
        query.addCriteria(Criteria.where("prodId").is(prodId)) ;
        List<Supply> supply = mongoTemplate.findAll(Supply.class) ;
        System.out.println(supply.size() + " is size");
        List<ViewSupply> ls = new ArrayList<ViewSupply>() ;
        Product product = mongoTemplate.findById(prodId , Product.class) ;
        for(int i=0;i<supply.size();i++){
            String vendorId = supply.get(i).getId().getVendorId()  ;
            String prodid = supply.get(i).getId().getProdId() ;
            if(prodId != prodId)
                continue ;
            int price = supply.get(i).getPrice() ;
            Vendor vendor = mongoTemplate.findById(vendorId , Vendor.class) ;
            int qty = supply.get(i).getQty() ;
            ls.add(new ViewSupply(product , vendor , qty , price)) ;
        }
        return ls ;
    }
}
