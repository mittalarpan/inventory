package com.example.Inventory.models;

public class CompositeKey {
    private String prodId;
    private String vendorId;

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
