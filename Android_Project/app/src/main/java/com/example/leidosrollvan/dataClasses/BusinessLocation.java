package com.example.leidosrollvan.dataClasses;

public class BusinessLocation {
    public String postCode;

    public BusinessLocation(String postCode){
        this.postCode = postCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public BusinessLocation(){}
}
