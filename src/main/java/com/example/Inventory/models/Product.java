package com.example.Inventory.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "elasticsearch",type="product")
@Setting(settingPath = "/resources/es-config/elastic-analyzer.json")
public class Product implements Comparable<Product> {

    @Id
    private String prodId = "";
    @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
    private String prodName = "";
    private int price = 0;
    private String description = "" ;

    public Product() {
    }

    public Product(String prodId, String prodName, int price, String description) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.price = price;
        this.description = description;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int compareTo(Product product) {
        int price = product.getPrice();
        return this.price - price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
