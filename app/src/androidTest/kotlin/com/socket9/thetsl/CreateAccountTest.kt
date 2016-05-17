package com.socket9.thetsl;

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.socket9.thetsl.activities.CreateAccountActivity
import com.socket9.thetsl.util.METErrorMatchers
import org.hamcrest.Matchers
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
    fun nameEmptyTest() {
        onView(withId(R.id.etPhone)).perform(typeText("0875567581"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etUsername)).check(matches(METErrorMatchers.withErrorText(Matchers.containsString("Name"))))
    }

    @Test
    fun passwordLessThan10Test() {
        onView(withId(R.id.etUsername)).perform(typeText("Phuchit Sirimongkolsathien"))
        onView(withId(R.id.etEmail)).perform(typeText("ripzery@gmail.com"))
        onView(withId(R.id.etPassword)).perform(typeText("11221"))
        onView(withId(R.id.etPhone)).perform(typeText("0875567581"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etPassword)).check(matches(METErrorMatchers.withErrorText(Matchers.containsString("Password"))))
    }

    @Test
    fun passwordMismatchTest() {
        onView(withId(R.id.etUsername)).perform(typeText("Phuchit Sirimongkolsathien"))
        onView(withId(R.id.etEmail)).perform(typeText("ripzery@gmail.com"))
        onView(withId(R.id.etPassword)).perform(typeText("11223344"))
        onView(withId(R.id.etConfirmPassword)).perform(typeText("445"))
        onView(withId(R.id.etPhone)).perform(typeText("0875567581"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etConfirmPassword)).check(matches(METErrorMatchers.withErrorText(Matchers.containsString("do not match"))))
    }

    @Test
    fun phoneLessThan10Test() {
        onView(withId(R.id.etUsername)).perform(typeText("Phuchit Sirimongkolsathien"))
        onView(withId(R.id.etEmail)).perform(typeText("ripzery@gmail.com"))
        onView(withId(R.id.etPassword)).perform(typeText("112233445"))
        onView(withId(R.id.etConfirmPassword)).perform(typeText("112233445"))
        onView(withId(R.id.etPhone)).perform(typeText("087556781"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etPhone)).check(matches(METErrorMatchers.withErrorText(Matchers.containsString("Phone must"))))
    }

    @Test
    fun emailWrongFormatTest() {
        onView(withId(R.id.etUsername)).perform(typeText("Phuchit Sirimongkolsathien"))
        onView(withId(R.id.etEmail)).perform(typeText("ripzery@gmail"))
        onView(withId(R.id.etPassword)).perform(typeText("112233445"))
        onView(withId(R.id.etConfirmPassword)).perform(typeText("112233445"))
        onView(withId(R.id.etPhone)).perform(typeText("0875567811"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etEmail)).check(matches(METErrorMatchers.withErrorText(Matchers.containsString("Email"))))
    }
}