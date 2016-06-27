//package com.socket9.thetsl.util
//
//import android.support.test.espresso.matcher.BoundedMatcher
//import android.view.View
//import com.rengwuxian.materialedittext.MaterialEditText
//import org.hamcrest.Description
//import org.hamcrest.Matcher
//
///**
// * Created by Euro (ripzery@gmail.com) on 5/13/16 AD.
// */
//
//object METErrorMatchers {
//    fun withErrorText(stringMatcher: Matcher<String>): Matcher<View> {
//        return object : BoundedMatcher<View, MaterialEditText>(MaterialEditText::class.java) {
//            override fun describeTo(description: Description?) {
//                description?.appendText("with error text: ")
//                stringMatcher.describeTo(description)
//            }
//
//            override fun matchesSafely(item: MaterialEditText?): Boolean {
//                return stringMatcher.matches(item?.error.toString())
//                //                return item?.context?.getString(stringMatcher).equals(item?.error.toString())
//            }
//
//        }
//    }
//}