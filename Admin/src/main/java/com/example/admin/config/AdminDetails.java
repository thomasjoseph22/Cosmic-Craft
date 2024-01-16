package com.example.admin.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

public class AdminDetails extends User implements Serializable {
    private String firstName;

    private String lastName;

    private String mobileNumber;
    public AdminDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String firstName,String lastName, String mobileNumber) {
        super(username, password, authorities);
        this.firstName=firstName;
        this.lastName=lastName;
        this.mobileNumber = mobileNumber;
    }
}
