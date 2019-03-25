package com.example.velord.masteringandroiddevelopmentwithkotlin

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.journaler.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {
    @get:Rule
    public  val mainActivityRule =
            ActivityTestRule(MainActivity::class.java)

    @Test
    fun testMainActivity(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }
}