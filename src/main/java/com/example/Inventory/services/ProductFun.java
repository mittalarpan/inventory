package com.example.Inventory;

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
        Query query = new Query() ;
        query.addCriteria(Criteria.where("prodName").regex(prodName)) ;
        return mongoTemplate.find(query,Product.class) ;
    }
}

