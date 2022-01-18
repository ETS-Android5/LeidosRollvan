package com.example.leidosrollvan;

public class Business {
    public String businessName, businessEmail, businessMobile;
    private BusinessMenu businessMenu;

    public Business(){

    }

    public Business(String businessName, String businessMobile, String businessEmail) {
        this.businessName = businessName;
        this.businessMobile = businessMobile;
        this.businessEmail = businessEmail;
    }

    /*public Business(String businessName, String businessMobile, String businessEmail, BusinessMenu businessMenu) {
        this.businessName = businessName;
        this.businessMobile = businessMobile;
        this.businessEmail = businessEmail;
        this.businessMenu = businessMenu;
    }*/
}
