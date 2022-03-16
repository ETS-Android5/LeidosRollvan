package com.example.leidosrollvan.activitymethods;

public class NameCheckMethods {

    public boolean checkProductName(String productName) {
        return productName.replaceAll(" ", "").equals("");
    }

    public boolean checkProductPrice(String productPrice) {
        return productPrice.toString().equals("");
    }

}
