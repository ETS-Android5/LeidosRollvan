package com.example.leidosrollvan;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.leidosrollvan.activity.BusinessRegisterActivity;
import com.example.leidosrollvan.activity.RegisterActivity;
import com.example.leidosrollvan.activity.MainActivity;
import com.example.leidosrollvan.fragments.HomeFragment;


import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BusinessRegisterActivityEspressoTest {


    @Rule
    public ActivityScenarioRule<BusinessRegisterActivity> businessRegisterActivityActivityScenarioRule =
            new ActivityScenarioRule<BusinessRegisterActivity>(BusinessRegisterActivity.class);



}