package com.example.Inventory.elastic;

import com.example.Inventory.models.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<Product,String> {
    
}
