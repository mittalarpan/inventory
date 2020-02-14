package com.example.Inventory.controllers;

import com.example.Inventory.models.Supply;
import com.example.Inventory.services.SupplyFun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class SupplyFunController {

    @Autowired
    SupplyFun supplyFun;

    @CrossOrigin
    @PostMapping("/inventory/supply")
    public boolean addSupply(@RequestParam(name = "vendorId") String vendorId, @RequestParam(name = "prodId") String prodId, @RequestParam(name = "qty") int qty, @RequestParam(name = "price") int price) throws ExecutionException, InterruptedException {
        supplyFun.addSupply(vendorId, prodId, qty, price);
        supplyFun.addVendorTransaction(vendorId,prodId,qty,price) ;
        return true;
    }

    @CrossOrigin
    @GetMapping("/xx")
    public List<Supply> xx()
    {
        return supplyFun.getSupply() ;
    }
}

