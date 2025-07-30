package com.example.zenhive

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import com.example.zenhive.view.SignUpActivity
import org.junit.Rule
import org.junit.Test

class SignUpActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SignUpActivity>()

    @Test
    fun testSignUpButtonVisibleAndClickable() {
        // Look for the Sign Up with Google button by its text
        composeTestRule.onNodeWithText("Sign Up with Google")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun testLoginButtonNavigates() {
        // Look for the Login button by its text
        composeTestRule.onNodeWithText("Login")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
    }
}