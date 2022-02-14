package com.example.leidosrollvan.dataClasses;

public class BusinessImage {
    public String mName;
    public String mImageUrl;

    public BusinessImage() {
        // empty  constructor for firebase;
    }

    public BusinessImage(String name, String imageUrl){
        this.mName = name;
        this.mImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
