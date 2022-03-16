package com.example.leidosrollvan.dataClassesForMethods;

import android.text.BoringLayout;

public class EmailPasswordResponseModel {

    private String message;
    private boolean status;

    public void setMessage(String userMessage){
        message = userMessage;
    }

    public void setStatus(Boolean userStatus){
        status = userStatus;
    }

    public String getMessage(){
        return message;
    }

    public Boolean getStatus(){
        return status;
    }

}
