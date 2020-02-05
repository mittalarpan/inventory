package com.example.Inventory.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Document(collection = "transaction")
public class Transaction implements Comparable<Transaction> {
    @Id
    private String transaction_id ;
    private String prodId ;
    private String vendorId ;
    private int qty ;
    private int price ;
    private Date timeStamp ;

    public Transaction(String transaction_id, String prodId, String vendorId, int qty, int price, Date timeStamp) {
        this.transaction_id = transaction_id;
        this.prodId = prodId;
        this.vendorId = vendorId;
        this.qty = qty;
        this.price = price;
        this.timeStamp = timeStamp;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(Transaction transaction) {
            int price = transaction.getPrice();
            return this.price - price;
    }
}
