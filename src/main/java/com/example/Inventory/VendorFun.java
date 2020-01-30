package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorFun {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Vendor saveVendor(Vendor vendor) {
        String vendorid = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorid));
        Vendor chk = mongoTemplate.findOne(query, Vendor.class);
        if (chk == null)
            return mongoTemplate.save(vendor);
        else {
            return new Vendor("", "", "");
        }
    }

    public List<Vendor> getAllVendors() {
        return mongoTemplate.findAll(Vendor.class);
    }

    public boolean checkVendor(Vendor vendor) {

        String vendorid = vendor.getVendorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendorId").is(vendorid));
        Vendor chk = mongoTemplate.findOne(query, Vendor.class);

        if (chk == null)
            return false;

        String pass = vendor.getPassword();

        if (chk.getPassword().equals(pass)) {
            vendor.setVendorName(chk.getVendorName());
            return true;
        }

        return false;
    }

   /* public Vendor vendorDetails(String vendorId)
    {
        Query query = new Query() ;
        query.addCriteria(Criteria.where("vendorId").is(vendorId)) ;
        Vendor chk = mongoTemplate.findOne(query , Vendor.class) ;
        return chk ;
    }*/
}
