package com.example.Inventory.models;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vendor {

    public Vendor(String vendorId, String vendorName, String password) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.password = password;
        this.roles = "VENDOR";
        this.permissions = "ROLE_VENDOR" ;
    }

    @Id
    private String vendorId ;
    private String vendorName ;
    private String password ;
    private String roles ;
    private String permissions;

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

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

    public List<String> getRoleList()
    {
        if(this.roles.length()>0)
        {
            return Arrays.asList(this.roles.split(",")) ;
        }
        return new ArrayList<>() ;
    }
    public List<String> getPermissionList()
    {
        if(this.permissions.length()>0)
        {
            return Arrays.asList(this.permissions.split(",")) ;
        }
        return new ArrayList<>() ;
    }

}
