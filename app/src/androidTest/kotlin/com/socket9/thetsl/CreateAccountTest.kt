package com.socket9.thetsl;

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.socket9.thetsl.activities.CreateAccountActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@RunWith(AndroidJUnit4::class)
class CreateAccountTest {


    @Rule @JvmField
    val activity = ActivityTestRule<CreateAccountActivity>(CreateAccountActivity::class.java)

    @Test
    fun passwordMismatchTest() {
        onView(withId(R.id.etUsername)).perform(typeText("Phuchit Sirimongkolsathien"))
        onView(withId(R.id.etEmail)).perform(typeText("ripzery@gmail.com"))
        onView(withId(R.id.etPassword)).perform(typeText("11223344"))
        onView(withId(R.id.etConfirmPassword)).perform(typeText("445"))
        onView(withId(R.id.etPhone)).perform(typeText("0875567581"), closeSoftKeyboard())
        //        onView(withId(R.id.etAddress)).perform(typeText("Thailand"))

        //        onView(withText("Creating account")).check(matches(isDisplayed()))

        onView(withId(R.id.btnRegister)).perform(click())

        onView(withText("Create Account")).check(matches(isDisplayed()))


        //        onView(withText("Creating account"))
        //            .inRoot(isDialog())
        //            .check(matches(isDisplayed()))
    }

    @Test
    fun passwordMatchTest() {
        onView(withId(R.id.etUsername)).perform(typeText("Phuchit Sirimongkolsathien"))
        onView(withId(R.id.etEmail)).perform(typeText("ripzery@gmail.com"))
        onView(withId(R.id.etPassword)).perform(typeText("112233445"))
        onView(withId(R.id.etConfirmPassword)).perform(typeText("112233445"))
        onView(withId(R.id.etPhone)).perform(typeText("0875567581"), closeSoftKeyboard())
        //        onView(withId(R.id.etAddress)).perform(typeText("Thailand"))

        onView(withId(R.id.btnRegister)).perform(click())
        Thread.sleep(100)
        onView(withText(R.string.dialog_progress_title)).check(matches(isDisplayed()))

    }
}