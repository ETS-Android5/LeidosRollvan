package com.example.leidosrollvan.activitymethods;

import org.junit.Assert;
import org.junit.Test;

public class BusinessProductFormActivityTest {

    private final NameCheckMethods testingClass = new NameCheckMethods();

    @Test
    public void test_empty_product_name(){

        boolean actual = testingClass.checkProductName("");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void test_correct_product_name(){

        boolean actual = testingClass.checkProductName("product Name");
        boolean expected = false;
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void test_empty_product_price(){

        boolean actual = testingClass.checkProductPrice("");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void test_correct_product_price(){

        boolean actual = testingClass.checkProductPrice("3200");
        boolean expected = false;
        Assert.assertEquals(expected, actual);

    }

}
