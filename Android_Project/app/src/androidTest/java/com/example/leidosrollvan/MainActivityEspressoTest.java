package com.example.leidosrollvan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.leidosrollvan.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {


    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    //Near You
    @Test
    public void test_NearYou_navigation() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.businessHorizRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.business_details_page)).check(matches(isDisplayed()));
    }
    @Test
    public void test_NearYou_back_navigation() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.businessHorizRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.business_details_page)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.customer_home_page)).check(matches(isDisplayed()));
    }

    //Top Deals
    @Test
    public void test_TopDeals_navigation() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.businessHorizRecyclerView))
                .perform(scrollTo(),RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.business_details_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_TopDeals_back_navigation() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.businessHorizRecyclerView))
                .perform(scrollTo(),RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.business_details_page)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.customer_home_page)).check(matches(isDisplayed()));
    }

    //All Businesses
//Top Deals

    @Test
    public void test_allBusinesses_navigation() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.MoreBusi)).perform(scrollTo());
        onView(withId(R.id.businessRecyclerView))
                .perform(scrollTo(),RecyclerViewActions.actionOnItemAtPosition(2,click()));
        onView(withId(R.id.business_details_page)).check(matches(isDisplayed()));
    }

    @Test
    public void test_allBusinesses_back_navigation() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.MoreBusi)).perform(scrollTo());
        onView(withId(R.id.businessRecyclerView))
                .perform(scrollTo(),RecyclerViewActions.actionOnItemAtPosition(2,click()));
        onView(withId(R.id.business_details_page)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.customer_home_page)).check(matches(isDisplayed()));
    }

    //Clicking on navbar navigation then clicking back will not bring you to previous fragment

    @Test
    public void test_navbar_home2search_navigation(){
        onView(withId(R.id.id_search))
                .perform(click());
        onView(withId(R.id.search_page)).check(matches(isDisplayed()));
    }
//    @Test
//    public void test_navbar_home2search_back_navigation(){
//        onView(withId(R.id.id_search))
//                .perform(click());
//        onView(withId(R.id.search_page)).check(matches(isDisplayed()));
//        pressBack();
//        onView(withId(R.id.customer_home_page)).check(matches(isDisplayed()));
//    }
//    @Test
//    public void test_navbar_home2map_navigation(){
//        onView(withId(R.id.id_map))
//                .perform(click());
//        onView(withId(R.id.map_page)).check(matches(isDisplayed()));
//    }
//    @Test
//    public void test_navbar_home2map_back_navigation(){
//        onView(withId(R.id.id_map))
//                .perform(click());
//        onView(withId(R.id.map_page)).check(matches(isDisplayed()));
//        pressBack();
//        onView(withId(R.id.customer_home_page)).check(matches(isDisplayed()));
//    }
    //
//    @Test
//    public void test_navbar_home2account_navigation(){
//        onView(withId(R.id.id_account))
//                .perform(click());
//        onView(withId(R.id.customer_guest_account_page)).check(matches(isDisplayed()));
//        onView(withId(R.id.customer_account_page)).check(matches(isDisplayed()));
//    }
//    @Test
//    public void test_navbar_home2account_back_navigation(){
//        onView(withId(R.id.id_account))
//                .perform(click());
//        onView(withId(R.id.customer_account_page)).check(matches(isDisplayed()));
//        pressBack();
//        onView(withId(R.id.customer_home_page)).check(matches(isDisplayed()));
//    }

}