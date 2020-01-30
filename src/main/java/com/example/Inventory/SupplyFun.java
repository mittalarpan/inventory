package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class SupplyFun {
    @Autowired
    MongoTemplate mongoTemplate;

    public Supply addSupply(Supply supply) {
        Query query = new Query() ;

        return mongoTemplate.save(supply);
    }

    public List<ViewSupply> getSupply() {
        List<ViewSupply> ret = new ArrayList<ViewSupply>() ;
        List<Supply> ls = mongoTemplate.findAll(Supply.class);
        for(int i=0;i<ls.size();i++){
            ViewSupply viewSupply = new ViewSupply() ;
            Query query = new Query() ;
            query.addCriteria(Criteria.where("prodId").is(ls.get(i).getId().getProdId())) ;
            Product product = mongoTemplate.findOne(query , Product.class) ;
            Query query1 = new Query() ;
            query1.addCriteria(Criteria.where("vendorId").is(ls.get(i).getId().getVendorId())) ;
            Vendor vendor = mongoTemplate.findOne(query1 , Vendor.class) ;
            ret.add(new ViewSupply(product , vendor , ls.get(i).getQty())) ;
        }
        return ret ;
    }

    public boolean updateSupplyUser(String vendorId, String prodId, int qty , String user_id) {

        Query query = new Query() ;

        Supply.CompositeKey temp = new Supply.CompositeKey(prodId,vendorId) ;

        query.addCriteria(Criteria.where("id").is(temp));

        String orderid ;

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        orderid = bytes.toString();

        Order order = new Order(orderid , user_id , prodId , vendorId , qty) ;
        mongoTemplate.save(order) ;
        
        Supply supply = mongoTemplate.findOne(query , Supply.class) ;

        if(supply.getQty() < qty){
            System.out.println("False");
            return false ;
        }
        else{
            supply.setQty(supply.getQty() - qty) ;
            mongoTemplate.save(supply) ;
            System.out.println("True");
            return true ;
        }

    }

    public void updateSupplyVendor(String vendorId, String prodId, int qty) {

        Query query = new Query() ;

        Supply.CompositeKey temp = new Supply.CompositeKey(prodId,vendorId) ;

        query.addCriteria(Criteria.where("id").is(temp));

        Supply supply = mongoTemplate.findOne(query , Supply.class) ;

        if(supply == null){
            Supply supp = new Supply(qty , temp) ;
           mongoTemplate.save(supp) ;
        }
        else{
            supply.setQty(supply.getQty() + qty) ;
            mongoTemplate.save(supply) ;
        }

    }
}
