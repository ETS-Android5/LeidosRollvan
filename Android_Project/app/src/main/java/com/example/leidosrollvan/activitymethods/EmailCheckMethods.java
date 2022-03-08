package com.example.leidosrollvan.activitymethods;

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

    public int checkEmail(String email) {

        if (email.isEmpty())
            return 0;
        else if (!EMAIL_ADDRESS.matcher(email).matches()) {
            return 1;
        } else return 2;

    }

}
