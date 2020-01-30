package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class SupplyFun {
    public Logger logger = Logger.getLogger("MyLog");
    public FileHandler logFileHandler = new FileHandler("/Users/havyapanchal/Desktop/Logs/MyLogFile.log", true);
    @Autowired
    MongoTemplate mongoTemplate;

    public SupplyFun() throws IOException {
    }

    public Supply addSupply(Supply supply) {
        return mongoTemplate.save(supply);
    }

    public List<ViewSupply> getSupply() {
        List<ViewSupply> ret = new ArrayList<ViewSupply>();
        List<Supply> ls = mongoTemplate.findAll(Supply.class);
        for (int i = 0; i < ls.size(); i++) {
            ViewSupply viewSupply = new ViewSupply();
            Query query = new Query();
            query.addCriteria(Criteria.where("prodId").is(ls.get(i).getId().getProdId()));
            Product product = mongoTemplate.findOne(query, Product.class);
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("vendorId").is(ls.get(i).getId().getVendorId()));
            Vendor vendor = mongoTemplate.findOne(query1, Vendor.class);
            ret.add(new ViewSupply(product, vendor, ls.get(i).getQty()));
        }
        return ret;
    }

    public boolean updateSupplyUser(String vendorId, String prodId, int qty, String user_id) {
        logger.addHandler(logFileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        logFileHandler.setFormatter(formatter);
        Query query = new Query();

        Supply.CompositeKey temp = new Supply.CompositeKey(prodId, vendorId);

        query.addCriteria(Criteria.where("id").is(temp));

        String orderId;

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        orderId = bytes.toString();

        Order order = new Order(orderId, user_id, prodId, vendorId, qty);
        mongoTemplate.save(order);

        Supply supply = mongoTemplate.findOne(query, Supply.class);

        if (supply.getQty() < qty) {
            //Failed attempt to purchase
            logger.warning("Invalid attempt to purchase the products by user: " + user_id);
            return false;
        } else {
            supply.setQty(supply.getQty() - qty);
            mongoTemplate.save(supply);
            //successful attempt to purchase
            logger.info("User id: " + user_id + "purchased product with ProductID: "
                    + prodId + " from Vendor: " + vendorId + " of quantity: " + qty);
            return true;
        }

    }

    public void updateSupplyVendor(String vendorId, String prodId, int qty) {
        logger.addHandler(logFileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        logFileHandler.setFormatter(formatter);
        Query query = new Query();

        Supply.CompositeKey temp = new Supply.CompositeKey(prodId, vendorId);

        query.addCriteria(Criteria.where("id").is(temp));

        Supply supply = mongoTemplate.findOne(query, Supply.class);

        if (supply == null) {
            Supply supp = new Supply(qty, temp);
            //Vendor created new entry in supply table
            logger.info("Vendor: " + vendorId + " started supply of product: " + prodId + " with quantity: " + qty);
            mongoTemplate.save(supp);
        } else {
            supply.setQty(supply.getQty() + qty);
            //Vendor updated supply table
            logger.info("Vendor: " + " increased supply of product: " + prodId + " by quantity: " + qty );
            mongoTemplate.save(supply);
        }

    }
}
