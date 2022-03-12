package com.example.leidosrollvan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.containsString;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.leidosrollvan.activity.BusinessLoginActivity;
import com.example.leidosrollvan.activity.BusinessProductFormActivity;
import com.example.leidosrollvan.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BusinessProductFormActivityEspressoTest {


    @Rule
    public ActivityScenarioRule<BusinessProductFormActivity> businessProductFormActivityActivityScenarioRule =
            new ActivityScenarioRule<BusinessProductFormActivity>(BusinessProductFormActivity.class);

    @Test
    public void test_cancel_navigation() {
        onView(withId(R.id.popupCancel))
                .perform(click());
        onView(withId(R.id.business_home_page)).check(matches(isDisplayed()));
    }

    public void test_noProductName_navigation() {
        onView(withId(R.id.productPricePopup))
                .perform(typeText("1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSave))
                .perform(click());
        onView(withId(R.id.businessProductForm)).check(matches(isDisplayed()));
    }

    public void test_badProductName_navigation() {
        onView(withId(R.id.productNamePopup))
                .perform(typeText(" "), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSave))
                .perform(click());
        onView(withId(R.id.businessProductForm)).check(matches(isDisplayed()));
    }

    public void test_badProductName_goodPrice_navigation() {
        onView(withId(R.id.productNamePopup))
                .perform(typeText(" "), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.productPricePopup))
                .perform(typeText("1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSave))
                .perform(click());
        onView(withId(R.id.businessProductForm)).check(matches(isDisplayed()));
    }

    public void test_noProductPrice_navigation() {
        onView(withId(R.id.productNamePopup))
                .perform(typeText("Burger"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSave))
                .perform(click());
        onView(withId(R.id.businessProductForm)).check(matches(isDisplayed()));
    }

    public void test_badProductPrice_navigation() {
        onView(withId(R.id.productPricePopup))
                .perform(typeText("food"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.popupSave))
                .perform(click());
        onView(withId(R.id.businessProductForm)).check(matches(isDisplayed()));
    }

    public void test_section_spinner_displays_correctly() {
        onView(withId(R.id.spinnerSection))
                .perform(click());
        onView(withText("Dinner"))
                .perform(click());
        onView(withId(R.id.spinnerSection)).check(matches(withSpinnerText(containsString("Dinner"))));
    }
}
