package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplyFunController {

    @Autowired
    SupplyFun supplyFun;

    @Autowired
    MongoTemplate mongoTemplate;

    @CrossOrigin
    @PostMapping("/inventory/supply")
    public boolean addSupply(@RequestParam(name = "vendorId") String vendorId, @RequestParam(name = "prodId") String prodId, @RequestParam(name = "qty") int qty, @RequestParam(name = "price") int price) {
        supplyFun.updateSupplyVendor(vendorId, prodId, qty, price);
        return true;
    }

}

