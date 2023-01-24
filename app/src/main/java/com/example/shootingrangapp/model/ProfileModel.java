package com.example.shootingrangapp.model;

import com.google.firebase.firestore.GeoPoint;

public class ProfileModel {
    private String userId;
    private String name;
    private String email;
    boolean merchant;
    private String gender;
    private String address;
    private String telephoneNo;
    private GeoPoint geopoint;

    public ProfileModel(){}

    public ProfileModel(String userId, String name, String email, boolean merchant, String gender, String address, String telephoneNo) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.merchant = merchant;
        this.gender = gender;
        this.address = address;
        this.telephoneNo = telephoneNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMerchant() {
        return merchant;
    }

    public void setMerchant(boolean merchant) {
        this.merchant = merchant;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(GeoPoint geopoint) {
        this.geopoint = geopoint;
    }
}
