package com.example.Inventory;

import com.example.Inventory.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductFunController {

    @Autowired
    private ProductFun productFun ;

    @PostMapping("/inventory/product")
    public void saveProduct(@RequestBody Product product){
        productFun.saveProduct(product) ;
    }

    @GetMapping("/inventory/product")
    public List<Product> getAllProducts(){
        return productFun.getAllProducts() ;
    }
}
