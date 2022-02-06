package com.example.leidosrollvan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.leidosrollvan.activity.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> registerActivityActivityScenarioRule =
            new ActivityScenarioRule<RegisterActivity>(RegisterActivity.class);


    @Test
    public void test_registerNow_noDetails_navigation() {
        onView(withId(R.id.cirRegisterButton))
                .perform(click());
        onView(withId(R.id.customer_register_page)).check(matches(isDisplayed()));
    }

//    @Test
//    public void test_registerNow_correctDetails_navigation() {
//        onView(withId(R.id.editTextName))
//                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
//        onView(withId(R.id.editTextEmail))
//                .perform(typeText("18chakah1@gmail.com"),ViewActions.closeSoftKeyboard());
//        onView(withId(R.id.editTextMobile))
//                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
//        onView(withId(R.id.editTextPassword))
//                .perform(typeText("Password123"),ViewActions.closeSoftKeyboard());
//        onView(withId(R.id.cirRegisterButton))
//                .perform(click());
//        onView(withId(R.id.customer_login_page)).check(matches(isDisplayed()));
//    }

    @Test
    public void test_registerNow_noPassword_navigation() {
        onView(withId(R.id.editTextName))
                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextEmail))
                .perform(typeText("18chakah1@gmail.com"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextMobile))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirRegisterButton))
                .perform(click());
        onView(withId(R.id.customer_register_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerNow_badPassword_navigation() {
        onView(withId(R.id.editTextName))
                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextEmail))
                .perform(typeText("18chakah1@gmail.com"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextMobile))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText("a"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirRegisterButton))
                .perform(click());
        onView(withId(R.id.customer_register_page)).check(matches(isDisplayed()));
    }


    @Test
    public void test_registerNow_noEmail_navigation() {
        onView(withId(R.id.editTextName))
                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextEmail))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText("Password123"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirRegisterButton))
                .perform(click());
        onView(withId(R.id.customer_register_page)).check(matches(isDisplayed()));
    }
    @Test
    public void test_registerNow_noPhone_navigation() {
        onView(withId(R.id.editTextName))
                .perform(typeText("TestName"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextEmail))
                .perform(typeText("badEmail"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextMobile))
                .perform(typeText("Password123"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirRegisterButton))
                .perform(click());
        onView(withId(R.id.customer_register_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerNow_noName_navigation() {
        onView(withId(R.id.editTextEmail))
                .perform(typeText("18chakah1@gmail.com"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextMobile))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText("a"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.cirRegisterButton))
                .perform(click());
        onView(withId(R.id.customer_register_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_existingAccount_navigation(){
        onView(withId(R.id.existingAccount))
                .perform(click());
        onView(withId(R.id.customer_login_page)).check(matches(isDisplayed()));
    }

}
