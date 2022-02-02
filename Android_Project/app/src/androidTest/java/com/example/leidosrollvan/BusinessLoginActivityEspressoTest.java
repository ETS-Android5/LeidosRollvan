package com.example.leidosrollvan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.leidosrollvan.activity.BusinessLoginActivity;
import com.example.leidosrollvan.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BusinessLoginActivityEspressoTest {


    @Rule
    public ActivityScenarioRule<BusinessLoginActivity> businessLoginActivityActivityScenarioRule =
            new ActivityScenarioRule<BusinessLoginActivity>(BusinessLoginActivity.class);

    @Test //Clicking register now button correctly navigates to register page
    public void test_registerNow_navigation(){
        onView(withId(R.id.businessRegister))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }

    @Test //Clicking back button after clicking register now button correctly navigates to login page
    public void test_registerNow_back_navigation(){
        onView(withId(R.id.businessRegister))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_forgotPassword_navigation(){
        onView(withId(R.id.businessForgotPassword))
                .perform(click());
        onView(withId(R.id.business_forgot_password_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_forgotPassword_back_navigation(){
        onView(withId(R.id.businessForgotPassword))
                .perform(click());
        onView(withId(R.id.business_forgot_password_page)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_toCustomer_navigation(){
        onView(withId(R.id.gotoUserLogin))
                .perform(click());
        onView(withId(R.id.customer_login_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_sideButton_navigation(){
        onView(withId(R.id.business_login_sidebutton))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }

    @Test //Clicking back button after clicking register now button correctly navigates to login page
    public void test_sideButton_back_navigation(){
        onView(withId(R.id.business_login_sidebutton))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    //login tests

    @Test
    public void test_login_incorrectDetails_navigation(){
        onView(withId(R.id.editBusinessLoginTextEmail))
                .perform(typeText("alextest@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessLoginTextPassword))
                .perform(typeText("WrongPassword"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirBusinessLoginButton))
                .perform(click());
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_noEmail_navigation(){
        onView(withId(R.id.editBusinessLoginTextPassword))
                .perform(typeText("Password123"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirBusinessLoginButton))
                .perform(click());
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_noPassword_navigation(){
        onView(withId(R.id.editBusinessLoginTextEmail))
                .perform(typeText("alextest@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirBusinessLoginButton))
                .perform(click());
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_noDetails_navigation(){
        onView(withId(R.id.cirBusinessLoginButton))
                .perform(click());
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_correctDetails_navigation() throws InterruptedException {
        new ActivityScenarioRule<MainActivity>(MainActivity.class);
        onView(withId(R.id.editBusinessLoginTextEmail))
                .perform(typeText("2558782c@student.gla.ac.uk"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessLoginTextPassword))
                .perform(typeText("Password123"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirBusinessLoginButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.business_home_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_correctDetails_back_navigation() throws InterruptedException {
        new ActivityScenarioRule<MainActivity>(MainActivity.class);
        onView(withId(R.id.editBusinessLoginTextEmail))
                .perform(typeText("2558782c@student.gla.ac.uk"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessLoginTextPassword))
                .perform(typeText("Password123"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirBusinessLoginButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.business_home_page)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }



}