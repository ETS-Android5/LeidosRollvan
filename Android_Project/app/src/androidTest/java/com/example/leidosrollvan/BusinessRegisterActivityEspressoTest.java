package com.example.leidosrollvan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.leidosrollvan.activity.BusinessRegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BusinessRegisterActivityEspressoTest {


    @Rule
    public ActivityScenarioRule<BusinessRegisterActivity> businessRegisterActivityActivityScenarioRule =
            new ActivityScenarioRule<BusinessRegisterActivity>(BusinessRegisterActivity.class);

    @Test
    public void test_existingAccount_navigation(){
        onView(withId(R.id.scrollable_business_register_page)).perform(swipeUp());
        onView(withId(R.id.existingBusinessAccount))
                .perform(click());
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_sideButton_navigation(){
        onView(withId(R.id.business_register_sidebutton))
                .perform(click());
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }

    //Inputs
    @Test //Clicking back button after clicking register now button correctly navigates to login page
    public void test_sideButton_back_navigation(){
        onView(withId(R.id.business_register_sidebutton))
                .perform(click());
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerNow_noDetails_navigation() {
        onView(withId(R.id.scrollable_business_register_page)).perform(swipeUp());
        onView(withId(R.id.cirBusinessRegisterButton))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerNow_noPassword_navigation() {
        onView(withId(R.id.editBusinessTextName))
                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextEmail))
                .perform(typeText("18chakah1@gmail.com"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextMobile))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.scrollable_business_register_page)).perform(swipeUp());
        onView(withId(R.id.cirBusinessRegisterButton))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerNow_badPassword_navigation() {
        onView(withId(R.id.editBusinessTextName))
                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextEmail))
                .perform(typeText("18chakah1@gmail.com"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextMobile))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextPassword))
                .perform(typeText("a"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.scrollable_business_register_page)).perform(swipeUp());
        onView(withId(R.id.cirBusinessRegisterButton))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }


    @Test
    public void test_registerNow_noEmail_navigation() {
        onView(withId(R.id.editBusinessTextName))
                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextEmail))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextPassword))
                .perform(typeText("Password123"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.scrollable_business_register_page)).perform(swipeUp());
        onView(withId(R.id.cirBusinessRegisterButton))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }
    @Test
    public void test_registerNow_noPhone_navigation() {
        onView(withId(R.id.editBusinessTextName))
                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextEmail))
                .perform(typeText("badEmail"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextMobile))
                .perform(typeText("Password123"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.scrollable_business_register_page)).perform(swipeUp());
        onView(withId(R.id.cirBusinessRegisterButton))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerNow_noName_navigation() {
        onView(withId(R.id.editBusinessTextEmail))
                .perform(typeText("18chakah1@gmail.com"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextMobile))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editBusinessTextPassword))
                .perform(typeText("a"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.scrollable_business_register_page)).perform(swipeUp());
        onView(withId(R.id.cirBusinessRegisterButton))
                .perform(click());
        onView(withId(R.id.business_register_page)).check(matches(isDisplayed()));
    }

}