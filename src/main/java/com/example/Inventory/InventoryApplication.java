package com.example.Inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
@SpringBootApplication
public class InventoryApplication {

    public static void main(String[] args) {

        SpringApplication.run(InventoryApplication.class, args);
    }
}
	