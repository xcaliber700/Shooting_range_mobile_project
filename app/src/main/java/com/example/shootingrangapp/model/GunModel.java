package com.example.shootingrangapp.model;


import java.io.Serializable;

public class GunModel implements Serializable {
    private String merchantId;
    private String ammunition;
    private int ammunitionQty;
    private Float ammunitionPrice;
    private String gunImage;
    private String ammunitionImage;
    private String gun;

    public GunModel(){}

    public GunModel(String merchantId, String ammunition, int ammunitionQty, Float ammunitionPrice, String gunImage, String ammunitionImage, String gun) {
        this.merchantId = merchantId;
        this.ammunition = ammunition;
        this.ammunitionQty = ammunitionQty;
        this.ammunitionPrice = ammunitionPrice;
        this.gunImage = gunImage;
        this.ammunitionImage = ammunitionImage;
        this.gun = gun;
    }

    public String getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(String ammunition) {
        this.ammunition = ammunition;
    }

    public int getAmmunitionQty() {
        return ammunitionQty;
    }

    public void setAmmunitionQty(int ammunitionQty) {
        this.ammunitionQty = ammunitionQty;
    }

    public Float getAmmunitionPrice() {
        return ammunitionPrice;
    }

    public void setAmmunitionPrice(Float ammunitionPrice) {
        this.ammunitionPrice = ammunitionPrice;
    }

    public String getGunImage() {
        return gunImage;
    }

    public void setGunImage(String gunImage) {
        this.gunImage = gunImage;
    }

    public String getAmmunitionImage() {
        return ammunitionImage;
    }

    public void setAmmunitionImage(String ammunitionImage) {
        this.ammunitionImage = ammunitionImage;
    }

    public String getGun() {
        return gun;
    }

    public void setGun(String gun) {
        this.gun = gun;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
