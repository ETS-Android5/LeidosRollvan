package com.example.leidosrollvan.activitymethods;

import com.example.leidosrollvan.dataClassesForMethods.EmailPasswordResponseModel;

public class PasswordCheckMethods {

    public EmailPasswordResponseModel checkPassword(String password){

        EmailPasswordResponseModel finalModel = new EmailPasswordResponseModel();

        if (password.isEmpty()){
            finalModel.setMessage("Password is Required!");
            finalModel.setStatus(false);
        }
        else if (password.length() < 6){
            finalModel.setMessage("Minimum password length should be 6 characters");
            finalModel.setStatus(false);
        }
        else {
            finalModel.setMessage("OK");
            finalModel.setStatus(true);
        }

        return finalModel;

    }

}
