package com.example.Inventory;

import org.springframework.data.annotation.Id;

public class Vendor {

    public Vendor(String vendorId, String vendorName, String password) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.password = password;
    }

    @Id
    private String vendorId ;
    private String vendorName ;
    private String password ;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
