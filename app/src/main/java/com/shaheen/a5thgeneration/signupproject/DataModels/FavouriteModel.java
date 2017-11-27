package com.shaheen.a5thgeneration.signupproject.DataModels;

import android.graphics.Bitmap;

/**
 * Created by Shani on 10/29/2017.
 */

public class FavouriteModel {
    String  image;
    String name;

    public FavouriteModel(String  image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
