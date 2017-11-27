package com.shaheen.a5thgeneration.signupproject;

/**
 * Created by Shani on 10/17/2017.
 */

public class UsersData {

    private String description;

    private String imagePath;

    public UsersData() {
    }

    public UsersData(String imagePath, String description) {
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

}
