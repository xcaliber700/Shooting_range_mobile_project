package com.example.shootingrangapp.model;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class ShootingRangeModel implements Serializable {
    private String storeId;
    private String name;
    private String email;
    private String address;
    private String telephoneNo;
    private GeoPoint geopoint;

    public ShootingRangeModel(){}

    public ShootingRangeModel(String storeId, String name, String email, String address, String telephoneNo, GeoPoint geopoint) {
        this.storeId = storeId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.telephoneNo = telephoneNo;
        this.geopoint = geopoint;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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
