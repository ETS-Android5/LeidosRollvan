package com.example.leidosrollvan.activitymethods;

import com.example.leidosrollvan.dataClassesForMethods.EmailPasswordResponseModel;

import java.util.regex.Pattern;

public class EmailCheckMethods {

    private Pattern EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public EmailPasswordResponseModel checkEmail(String email) {

        EmailPasswordResponseModel finalModel = new EmailPasswordResponseModel();

        if (email.isEmpty()) {
            finalModel.setMessage("Email is Required!");
            finalModel.setStatus(false);
        } else if (!EMAIL_ADDRESS.matcher(email).matches()) {
            finalModel.setMessage("Please provide valid email!");
            finalModel.setStatus(false);
        } else {
            finalModel.setMessage("OK");
            finalModel.setStatus(true);
        }

        return finalModel;

    }

}
