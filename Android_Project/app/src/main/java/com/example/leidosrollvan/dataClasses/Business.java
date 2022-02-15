package com.example.leidosrollvan.dataClasses;

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

    public String getBusinessName() {
        return businessName;
    }

    public String getContact() {
        return businessMobile;
    }

    /*public Business(String businessName, String businessMobile, String businessEmail, BusinessMenu businessMenu) {
        this.businessName = businessName;
        this.businessMobile = businessMobile;
        this.businessEmail = businessEmail;
        this.businessMenu = businessMenu;
    }*/
}
