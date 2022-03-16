package com.example.leidosrollvan.activitymethods;

import android.net.Uri;

public class BusinessNameMobileCheckMethods {

    public boolean checkBusinessName(String businessName) {
        return businessName.isEmpty();
    }

    public boolean checkBusinessMobile(String businessMobile) {
        return businessMobile.isEmpty();
    }

    public boolean checkUri(Uri checkedUri) {
        return checkedUri == null;
    }

}
