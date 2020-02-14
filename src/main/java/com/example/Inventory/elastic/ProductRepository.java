package com.example.Inventory.elastic;

import com.example.Inventory.models.Product;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductRepository extends ElasticsearchRepository<Product,String> {
    List<Product> search(QueryBuilder query) ;

    List<Product> findAll();
}
