package com.example.leidosrollvan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.leidosrollvan.activity.BusinessLoginActivity;
import com.example.leidosrollvan.activity.BusinessSettingsActivity;

import org.junit.Rule;
import org.junit.Test;

public class BusinessSettingsActivityEspressoTest {
    @Rule
    public ActivityScenarioRule<BusinessSettingsActivity> businessSettingsActivityScenarioRule =
            new ActivityScenarioRule<BusinessSettingsActivity>(BusinessSettingsActivity.class);

    @Test
    public void test_cancel_navigation() {
        onView(withId(R.id.popupCancelEdit))
                .perform(click());
        onView(withId(R.id.business_home_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_badBusinessName_navigation(){
        onView(withId(R.id.businessNamePopup))
                .perform(typeText(" "), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.businessMobilePopup))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSaveEdit))
                .perform(click());
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }
    @Test
    public void test_noBusinessName_navigation(){
        onView(withId(R.id.businessMobilePopup))
                .perform(typeText("123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSaveEdit))
                .perform(click());
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }

    @Test
    public void test_badBusinessMobile_navigation(){
        onView(withId(R.id.businessNamePopup))
                .perform(typeText("Burger Van"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.businessMobilePopup))
                .perform(typeText("Food"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSaveEdit))
                .perform(click());
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }
    @Test
    public void test_noBusinessMobile_navigation(){
        onView(withId(R.id.businessNamePopup))
                .perform(typeText("Burger Van"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSaveEdit))
                .perform(click());
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }

}
