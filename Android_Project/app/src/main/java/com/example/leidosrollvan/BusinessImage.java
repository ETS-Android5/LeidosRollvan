package com.example.leidosrollvan;

public class BusinessImage {
    private String mName;
    private String mImageUrl;

    public BusinessImage(){
        // empty  constructor for firebase;
    }

    public BusinessImage(String name, String imageUrl){
        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }
}
