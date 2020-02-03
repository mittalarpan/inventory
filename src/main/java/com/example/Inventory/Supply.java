package com.example.Inventory;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class Supply implements Comparable<Supply> {

    int qty ;
    int price ;

    @Id
    private CompositeKey id;

    static class CompositeKey implements Serializable{
        private String prodId   ;
        private String vendorId ;

        public CompositeKey(String prodId, String vendorId) {
            this.prodId = prodId;
            this.vendorId = vendorId;
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
    }

    public Supply(int qty, CompositeKey id , int price) {
        this.qty = qty;
        this.id = id;
        this.price=price ;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public CompositeKey getId() {
        return id;
    }

    public void setId(CompositeKey id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int compareTo(Supply supply) {
        int price = supply.getPrice() ;
        return this.price-price ;
    }
    //    @Id
//    private CompositeKey id;
//    int qty;
//
//    public Supply(CompositeKey id, int qty) {
//        this.id = id;
//        this.qty = qty;
//    }
//
//    static class CompositeKey implements Serializable {
//        private Product product;
//        private Vendor vendor;
//
//        public CompositeKey(Product product, Vendor vendor) {
//            this.product = product;
//            this.vendor = vendor;
//        }
//
//        public Product getProduct() {
//            return product;
//        }
//
//        public void setProduct(Product product) {
//            this.product = product;
//        }
//
//        public Vendor getVendor() {
//            return vendor;
//        }
//
//        public void setVendor(Vendor vendor) {
//            this.vendor = vendor;
//        }
//    }
//
//    public CompositeKey getId() {
//        return id;
//    }
//
//    public void setId(CompositeKey id) {
//        this.id = id;
//    }
//
//    public int getQty() {
//        return qty;
//    }
//
//    public void setQty(int qty) {
//        this.qty = qty;
//    }
}
