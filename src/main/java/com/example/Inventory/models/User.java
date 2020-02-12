package com.example.Inventory.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document(collection = "user")
public class User {

    @Id
    private String user_id = "";
    private String password = "";
    private String name = "" ;
    private String roles = "";
    private String permissions = "";

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

    public User() {
    }

    public User(String user_id, String password, String name) {
        this.user_id = user_id;
        this.password = password;
        this.name = name;
        this.permissions = "ROLE_USER" ;
        this.roles = "USER" ;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
