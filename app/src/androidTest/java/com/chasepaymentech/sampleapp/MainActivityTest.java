package com.chasepaymentech.sampleapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule=new ActivityScenarioRule<>(MainActivity.class);
    private MainActivity mainActivity=null;
    //Test Data
    final static String PAN="5454545454545454";
    final static String CVV="123";
    final static String EXPIRY="0225";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_OnCreate_mtd_encCardBtn() {
        ActivityScenario<MainActivity> scenario=rule.getScenario();
        Espresso.onView(ViewMatchers.withId(R.id.panInput)).perform(ViewActions.typeText(PAN),ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.cvvInput)).perform(ViewActions.typeText(CVV),ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.expiryInput)).perform(ViewActions.typeText(EXPIRY),ViewActions.closeSoftKeyboard());
        //Click on encCardBtn button
        Espresso.onView(ViewMatchers.withId(R.id.encCardBtn)).perform(ViewActions.click());
        //Check the Results
        Espresso.onView(ViewMatchers.withId(R.id.panOutput)).check(ViewAssertions.matches(ViewMatchers.withText(PAN)));
        Espresso.onView(ViewMatchers.withId(R.id.cvvOutput)).check(ViewAssertions.matches(ViewMatchers.withText(CVV)));
        Espresso.onView(ViewMatchers.withId(R.id.expiryOutput)).check(ViewAssertions.matches(ViewMatchers.withText(EXPIRY)));
        Espresso.onView(ViewMatchers.withId(R.id.integrityOutput)).check(ViewAssertions.matches(ViewMatchers.withText("N/A")));
        Espresso.onView(ViewMatchers.withId(R.id.keyIdOutput)).check(ViewAssertions.matches(ViewMatchers.withText("N/A")));
        Espresso.onView(ViewMatchers.withId(R.id.phaseOutput)).check(ViewAssertions.matches(ViewMatchers.withText("N/A")));

    }
    @Test
    public void test_OnCreate_mtd_encCardNdCvvBtn() {
        ActivityScenario<MainActivity> scenario=rule.getScenario();
        Espresso.onView(ViewMatchers.withId(R.id.panInput)).perform(ViewActions.typeText(PAN),ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.cvvInput)).perform(ViewActions.typeText(CVV),ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.expiryInput)).perform(ViewActions.typeText(EXPIRY),ViewActions.closeSoftKeyboard());
        //Click on encCardBtn button
        Espresso.onView(ViewMatchers.withId(R.id.encCardNdCvvBtn)).perform(ViewActions.click());
        //Check the Results
        Espresso.onView(ViewMatchers.withId(R.id.panOutput)).check(ViewAssertions.matches(ViewMatchers.withText(PAN)));
        Espresso.onView(ViewMatchers.withId(R.id.cvvOutput)).check(ViewAssertions.matches(ViewMatchers.withText(CVV)));
        Espresso.onView(ViewMatchers.withId(R.id.expiryOutput)).check(ViewAssertions.matches(ViewMatchers.withText(EXPIRY)));
        Espresso.onView(ViewMatchers.withId(R.id.integrityOutput)).check(ViewAssertions.matches(ViewMatchers.withText("N/A")));
        Espresso.onView(ViewMatchers.withId(R.id.keyIdOutput)).check(ViewAssertions.matches(ViewMatchers.withText("N/A")));
        Espresso.onView(ViewMatchers.withId(R.id.phaseOutput)).check(ViewAssertions.matches(ViewMatchers.withText("N/A")));

    }
}