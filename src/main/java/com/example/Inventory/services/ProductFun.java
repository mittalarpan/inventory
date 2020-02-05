package com.example.Inventory.services;

import com.example.Inventory.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    public List<Product> getQueryProducts(String prodName) {
        Criteria regex = Criteria.where("prodName").regex(prodName, "i");
//        mongoOperations.find(new Query().addCriteria(regex), User.class);
        Query query = new Query() ;
//        query.addCriteria(Criteria.where("prodName").regex(prodName)) ;
        query.addCriteria(regex) ;
        return mongoTemplate.find(query,Product.class) ;
    }
}

