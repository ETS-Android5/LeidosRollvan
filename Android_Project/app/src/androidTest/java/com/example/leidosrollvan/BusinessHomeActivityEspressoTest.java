package com.example.leidosrollvan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.leidosrollvan.activity.BusinessHomeActivity;
import com.example.leidosrollvan.activity.BusinessRegisterActivity;

import org.junit.Rule;
import org.junit.Test;

public class BusinessHomeActivityEspressoTest {
    @Rule
    public ActivityScenarioRule<BusinessHomeActivity> businessHomeActivityScenarioRule =
            new ActivityScenarioRule<BusinessHomeActivity>(BusinessHomeActivity.class);

    @Test
    public void test_addItems_navigation(){
        onView(withId(R.id.business_home_add))
                .perform(click());
        onView(withId(R.id.businessProductForm)).check(matches(isDisplayed()));
    }
    @Test
    public void test_categories_navigation(){
        onView(withId(R.id.business_home_category))
                .perform(click());
        onView(withId(R.id.categories)).check(matches(isDisplayed()));
    }
    @Test
    public void test_offers_navigation(){
        onView(withId(R.id.business_home_notifications))
                .perform(click());
        onView(withId(R.id.noti)).check(matches(isDisplayed()));
    }
    @Test
    public void test_logout_navigation(){
        onView(withId(R.id.business_home_logout))
                .perform(click());
        onView(withId(R.id.business_login_page)).check(matches(isDisplayed()));
    }
    @Test
    public void test_settings_navigation(){
        onView(withId(R.id.business_home_add))
                .perform(click());
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }
}
