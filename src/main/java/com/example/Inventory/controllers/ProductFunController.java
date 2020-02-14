package com.example.Inventory.controllers;

import com.example.Inventory.models.Product;
import com.example.Inventory.services.ProductFun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductFunController {

    @Autowired
    private ProductFun productFun;

    @PostMapping("/inventory/product")
    public void saveProduct(@RequestBody Product product) throws IOException{
        productFun.saveProduct(product) ;
    }

    @GetMapping("/inventory/product")
    public List<Product> getAllProducts() {
        //return productFun.getAllProducts();
        return productFun.getAllProducts();
    }

    @GetMapping("/inventory/product/elastic/search")
    public List<Product> getProductsFromSearch(@RequestParam("query") String query) {
        return productFun.findProductByNameAndDescription(query);
    }

    @GetMapping("/inventory/product/autocomplete")
    public List<Product> getAutocomplete(@RequestParam("query") String query) {
        return productFun.search(query);
    }

}
