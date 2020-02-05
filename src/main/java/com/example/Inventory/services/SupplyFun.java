package com.example.Inventory.services;

import com.example.Inventory.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//import java
@Service
public class SupplyFun {
    public Logger logger = Logger.getLogger("myLogger");
    public FileHandler fileHandler = new FileHandler("/Users/havyapanchal/Desktop/LogFiles/logs_1.log");
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ProductFun productfun;

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
        return productfun.getAllProducts();
    }

    public boolean updateSupplyUser(String vendorId, String prodId, int qty, String user_id) {
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);

        String order_id;

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        order_id = bytes.toString();
        Calendar calendar = Calendar.getInstance();
        Date timestamp = (Date) calendar.getTime();

        Order order = new Order(order_id, user_id, prodId, vendorId, qty, timestamp);
        System.out.println("In order: " + order.getVendorId());
        mongoTemplate.save(order);

        Query query = new Query();
        System.out.println("prodId: " + prodId + "ven: "+ vendorId);
        CompositeKey temp = new CompositeKey(prodId, vendorId);
        query.addCriteria(Criteria.where("id").is(temp));
        Supply supply = mongoTemplate.findOne(query, Supply.class);

        if (supply.getQty() < qty) {
            System.out.println("False");
            logger.warning("Invalid attempt for purchase by UserID: " + user_id);
            return false;
        } else {
            supply.setQty(supply.getQty() - qty);
            mongoTemplate.save(supply);
            System.out.println("True");
            logger.info("UserID: " + user_id + " purchased ProdID: " + prodId
                    + " from VendorID: " + vendorId + " of qty: " + qty);
            return true;
        }

    }

    public void updateSupplyVendor(String vendorId, String prodId, int qty, int price) {
        Calendar calendar = Calendar.getInstance();
        Date timestamp = (Date) calendar.getTime();
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        Query query = new Query();

        CompositeKey temp = new CompositeKey(prodId, vendorId);

        query.addCriteria(Criteria.where("id").is(temp));

        Supply supply = mongoTemplate.findOne(query, Supply.class);

        Product product = mongoTemplate.findById(prodId , Product.class) ;

        if (supply == null) {

            Supply supp = new Supply(qty, temp, price ,timestamp);
            logger.info("VendorID: " + vendorId + " initiated supply of ProdID: " + prodId);
            mongoTemplate.save(supp);
        } else {
            supply.setPrice(price);
            supply.setQty(supply.getQty() + qty);
            logger.info("Vendor: " + vendorId + " increased supply for productID: " + prodId
                    + " by qty: " + qty);

            mongoTemplate.save(supply);
        }
        product.setPrice(Math.min(price,product.getPrice())) ;
        mongoTemplate.save(product,"product") ;
    }

    public List<ViewSupply> getProduct(String prodId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("prodId").is(prodId));
        List<Supply> supply = mongoTemplate.findAll(Supply.class);
        System.out.println(supply.size() + " is size");
        List<ViewSupply> ls = new ArrayList<ViewSupply>();
        Product product = mongoTemplate.findById(prodId, Product.class);
        for (int i = 0; i < supply.size(); i++) {
            //System.out.println(i) ;
            String vendorId = supply.get(i).getId().getVendorId();
            String prodid = supply.get(i).getId().getProdId();
            System.out.println(prodid + " " + prodId + " " + vendorId);
            if (!(prodId.equals(prodid)))
                continue;
            int price = supply.get(i).getPrice();
            Vendor vendor = mongoTemplate.findById(vendorId, Vendor.class);
            int qty = supply.get(i).getQty();
            System.out.println(product.getProdId() + " " + vendor.getVendorId() + " " + qty + " " + price);
            ls.add(new ViewSupply(product, vendor, qty, price));
        }
        Collections.sort(ls) ;
        return ls;
    }

    public void updateTransaction(String vendorId, String prodId, int qty, int price) {
        Calendar calendar = Calendar.getInstance();
        Date timestamp = (Date) calendar.getTime();
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String transaction_id = bytes.toString();
        Transaction transaction = new Transaction(transaction_id,prodId,vendorId,qty,price,timestamp);
        mongoTemplate.save(transaction,"transaction") ;
        Product product = mongoTemplate.findById(prodId,Product.class);
        product.setPrice(Math.min(product.getPrice(),price));
        mongoTemplate.save(product,"product") ;
    }

    public List<ViewSupply> getProductFromTransaction(String prodId) {
        String previous = "" ;
        List<Transaction> transactions = mongoTemplate.query(Transaction.class).all();
        Collections.sort(transactions);
        Product product = mongoTemplate.findById(prodId,Product.class);
        List<ViewSupply> viewSupplies = new ArrayList<ViewSupply>() ;
        Map<String,Boolean> flag = new HashMap<String, Boolean>() ;
        for(int i=0;i<transactions.size();i++)
        {
            String vendor_id = transactions.get(i).getVendorId() ;
            String product_id = transactions.get(i).getProdId() ;
            if(flag.containsKey(vendor_id)==false && prodId.equals(product_id))
            {
                Vendor vendor = mongoTemplate.findById(vendor_id,Vendor.class) ;
                int qty = transactions.get(i).getQty() ;
                int price = transactions.get(i).getPrice() ;
               viewSupplies.add(new ViewSupply(product, vendor, qty, price));
               previous = vendor_id ;
               flag.put(vendor_id,true);
            }
        }
        return viewSupplies ;
    }

    public void updateTransactionUser(String vendorId, String prodId, int qty, int price) {
        Criteria byVendorId = Criteria.where("vendorId").is(vendorId) ;
        Criteria byProdId = Criteria.where("prodId").is(prodId) ;
        Criteria byPrice = Criteria.where("price").is(price) ;
//        Criteria criteria = new Criteria().andOperator(byVendorId, byPrice);
//        Criteria criteria1 = new Criteria().andOperator(criteria,byProdId) ;
        Criteria criteria = new Criteria().andOperator(byPrice,byProdId,byVendorId) ;
        Query query = new Query(criteria);
        Transaction transaction = mongoTemplate.findOne(query,Transaction.class) ;
        transaction.setQty(transaction.getQty() - qty);
        mongoTemplate.save(transaction,"transaction") ;
    }
}
