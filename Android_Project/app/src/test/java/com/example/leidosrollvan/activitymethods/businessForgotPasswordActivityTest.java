package com.example.leidosrollvan.activitymethods;

import org.junit.Assert;
import org.junit.Test;

public class businessForgotPasswordActivityTest {

    private EmailCheckMethods testingClass = new EmailCheckMethods();

    @Test
    public void test_emty_email(){

        int actual = testingClass.checkEmail("");

        int expected = 0;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_incorrect_email(){

        int actual = testingClass.checkEmail("someemail");

        int expected = 1;

        Assert.assertEquals(expected, actual);
    }
    @Test
    public void test_email_without_char(){

        int actual = testingClass.checkEmail("someemail.com");

        int expected = 1;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_email_without_dot(){

        int actual = testingClass.checkEmail("some@emailcom");

        int expected = 1;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_correct_email(){

        int actual = testingClass.checkEmail("some@email.com");

        int expected = 2;

        Assert.assertEquals(expected, actual);
    }


}