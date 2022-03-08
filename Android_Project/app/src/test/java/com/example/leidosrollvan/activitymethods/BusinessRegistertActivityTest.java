package com.example.leidosrollvan.activitymethods;

import android.net.Uri;

import org.junit.Assert;
import org.junit.Test;

public class BusinessRegistertActivityTest {

    private EmailCheckMethods emailCheckClass = new EmailCheckMethods();
    private PasswordCheckMethods passwordCheckClass = new PasswordCheckMethods();
    private BusinessNameMobileCheckMethods nameMobile = new BusinessNameMobileCheckMethods();

    @Test
    public void test_emty_email(){

        int actual = emailCheckClass.checkEmail("");

        int expected = 0;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_incorrect_email(){

        int actual = emailCheckClass.checkEmail("someemail");

        int expected = 1;

        Assert.assertEquals(expected, actual);
    }
    @Test
    public void test_email_without_char(){

        int actual = emailCheckClass.checkEmail("someemail.com");

        int expected = 1;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_email_without_dot(){

        int actual = emailCheckClass.checkEmail("some@emailcom");

        int expected = 1;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_correct_email(){

        int actual = emailCheckClass.checkEmail("some@email.com");

        int expected = 2;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_empty_password(){

        int actual = passwordCheckClass.check_password("");

        int expected = 0;

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void test_incorrect_password(){

        int actual = passwordCheckClass.check_password("12345");

        int expected = 1;

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void test_correct_password(){

        int actual = passwordCheckClass.check_password("123456");

        int expected = 2;

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void test_empty_business_name(){
        boolean actual = nameMobile.checkBusinessName("");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_correct_business_name(){
        boolean actual = nameMobile.checkBusinessName("someBusinessName");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_empty_business_mobile(){
        boolean actual = nameMobile.checkBusinessMobile("");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_correct_business_mobile(){
        boolean actual = nameMobile.checkBusinessMobile("021423423");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_nullable_uri(){
        Uri uri = null;
        boolean actual = nameMobile.checkUri(uri);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

}
