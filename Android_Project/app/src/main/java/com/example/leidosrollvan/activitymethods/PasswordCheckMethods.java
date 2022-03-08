package com.example.leidosrollvan.activitymethods;

public class PasswordCheckMethods {

    public int check_password(String password){

        if (password.isEmpty()){
            return 0;
        }
        else if (password.length() < 6){
            return 1;
        }
        else
            return 2;

    }

}
