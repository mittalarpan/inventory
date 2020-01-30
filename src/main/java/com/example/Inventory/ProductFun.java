package com.example.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductFun{

    @Autowired
    private MongoTemplate mongoTemplate ;

    public void saveProduct(Product product){
        mongoTemplate.save(product) ;
    }

    public List<Product> getAllProducts() {
        return mongoTemplate.findAll(Product.class) ;
    }
}
