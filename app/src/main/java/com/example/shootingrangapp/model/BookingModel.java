package com.example.shootingrangapp.model;


import com.example.utils.Constants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingModel implements Serializable {
    private String userId;
    private String merchantId;
    private GunModel gun;
    private String paymentMethod;
    private String bookingId;
    private String bookingAmount;
    private Date bookingDate;

    public BookingModel(){}

    public BookingModel(String userId, String merchantId, GunModel gun, String paymentMethod, String bookingId, Date bookingDate) {
        this.userId = userId;
        this.merchantId = merchantId;
        this.gun = gun;
        this.paymentMethod = paymentMethod;
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public GunModel getGun() {
        return gun;
    }

    public void setGun(GunModel gun) {
        this.gun = gun;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        return sdf.format(bookingDate);
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(String bookingAmount) {
        this.bookingAmount = bookingAmount;
    }
}
