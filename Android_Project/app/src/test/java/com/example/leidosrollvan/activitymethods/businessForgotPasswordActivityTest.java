package com.example.leidosrollvan.activitymethods;

import com.example.leidosrollvan.dataClassesForMethods.EmailPasswordResponseModel;

import org.junit.Assert;
import org.junit.Test;

public class businessForgotPasswordActivityTest {

    private final EmailCheckMethods emailCheckClass = new EmailCheckMethods();

    @Test
    public void test_empty_email(){

        EmailPasswordResponseModel actual = emailCheckClass.checkEmail("");

        EmailPasswordResponseModel expected = new EmailPasswordResponseModel();

        expected.setStatus(false);
        expected.setMessage("Email is Required!");

        Assert.assertEquals(expected.getMessage(), actual.getMessage());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    public void test_incorrect_email(){

        EmailPasswordResponseModel actual = emailCheckClass.checkEmail("someemail");

        EmailPasswordResponseModel expected = new EmailPasswordResponseModel();
        expected.setStatus(false);
        expected.setMessage("Please provide valid email!");

        Assert.assertEquals(expected.getMessage(), actual.getMessage());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }
    @Test
    public void test_email_without_char(){

        EmailPasswordResponseModel actual = emailCheckClass.checkEmail("someemail.com");

        EmailPasswordResponseModel expected = new EmailPasswordResponseModel();
        expected.setStatus(false);
        expected.setMessage("Please provide valid email!");

        Assert.assertEquals(expected.getMessage(), actual.getMessage());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    public void test_email_without_dot(){

        EmailPasswordResponseModel actual = emailCheckClass.checkEmail("some@emailcom");

        EmailPasswordResponseModel expected = new EmailPasswordResponseModel();
        expected.setStatus(false);
        expected.setMessage("Please provide valid email!");

        Assert.assertEquals(expected.getMessage(), actual.getMessage());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    public void test_correct_email(){

        EmailPasswordResponseModel actual = emailCheckClass.checkEmail("some@email.com");

        EmailPasswordResponseModel expected = new EmailPasswordResponseModel();
        expected.setStatus(true);
        expected.setMessage("OK");

        Assert.assertEquals(expected.getMessage(), actual.getMessage());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }


}