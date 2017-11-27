package com.shaheen.a5thgeneration.signupproject.DataModels;

/**
 * Created by Shani on 11/15/2017.
 */

public class SearchTripData {
    String usernam;
    String address;
    String date;
    String pay;
    String image;
    String description;
    String address_living;

    public SearchTripData(String usernam, String address, String date, String pay, String image, String description, String address_living) {
        this.usernam = usernam;
        this.address = address;
        this.date = date;
        this.pay = pay;
        this.image = image;
        this.description = description;
        this.address_living = address_living;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress_living() {
        return address_living;
    }

    public void setAddress_living(String address_living) {
        this.address_living = address_living;
    }

    public String getUsernam() {
        return usernam;
    }

    public void setUsernam(String usernam) {
        this.usernam = usernam;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
