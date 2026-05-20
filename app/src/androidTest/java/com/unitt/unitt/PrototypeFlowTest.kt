package com.unitt.unitt

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class PrototypeFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun signupFlowReachesHome() {
        waitForTag("login-screen")
        composeRule.onNodeWithTag("signup-button").performClick()

        waitForTag("school-selection-screen")
        composeRule.onNodeWithTag("school-row-snu").performClick()
        composeRule.onNodeWithTag("primary-cta").performScrollTo().performClick()

        waitForTag("email-verification-screen")
        composeRule.onNodeWithTag("primary-cta").performScrollTo().performClick()

        waitForTag("otp-code-screen")
        composeRule.onNodeWithTag("keypad-digit-1").performClick()
        composeRule.onNodeWithTag("keypad-digit-2").performClick()
        composeRule.onNodeWithTag("keypad-digit-3").performClick()

        waitForTag("password-setup-screen")
        composeRule.onNodeWithTag("primary-cta").performScrollTo().performClick()

        waitForTag("terms-agreement-screen")
        composeRule.onNodeWithTag("primary-cta").performScrollTo().performClick()

        waitForTag("profile-setup-screen")
        composeRule.onNodeWithTag("primary-cta").performScrollTo().performClick()

        waitForTag("user-home-screen")
    }

    @Test
    fun loginSearchProductAndReportFlow() {
        launchPastAuth()
        composeRule.onNodeWithTag("category-textbook-button").performClick()
        composeRule.onNodeWithTag("product-card-data-structure").performClick()
        waitForTag("product-detail-screen")
        composeRule.onNodeWithTag("top-back-button").performClick()

        waitForTag("user-home-screen")
        composeRule.onNodeWithTag("tab-검색").performClick()
        waitForTag("search-screen")
        composeRule.onNodeWithTag("recent-search-airpods").performClick()
        composeRule.onNodeWithTag("search-result-airpods").performClick()
        waitForTag("product-detail-screen")
        composeRule.onNodeWithTag("top-right-button").performClick()

        waitForTag("report-flow-screen")
        composeRule.onNodeWithTag("report-next").performClick()
        composeRule.onNodeWithTag("report-reason-noshow").performClick()
        composeRule.onNodeWithTag("report-next").performClick()
        composeRule.onNodeWithTag("report-next").performClick()
        waitForTag("report-done-screen")
    }

    @Test
    fun createChatReviewAndProfileFlow() {
        launchPastAuth()
        composeRule.onNodeWithTag("tab-등록").performClick()
        waitForTag("listing-create-screen")
        composeRule.onNodeWithTag("create-category-textbook").performClick()
        composeRule.onNodeWithTag("create-next").performClick()
        composeRule.onNodeWithTag("create-next").performClick()
        composeRule.onNodeWithTag("create-next").performClick()
        composeRule.onNodeWithTag("create-next").performClick()
        composeRule.onNodeWithTag("create-submit").performClick()
        waitForTag("create-done-screen")
        composeRule.onNodeWithTag("create-done-home").performClick()

        composeRule.onNodeWithTag("tab-채팅").performClick()
        waitForTag("chat-list-screen")
        composeRule.onNodeWithTag("chat-row-chat-airpods").performClick()
        waitForTag("chat-room-screen")
        composeRule.onNodeWithTag("appointment-menu-button").performClick()
        composeRule.onNodeWithTag("appointment-submit-button").performClick()
        waitForTag("trade-panel-screen")
        composeRule.onNodeWithTag("trade-complete-button").performClick()
        waitForTag("review-screen")
        composeRule.onNodeWithTag("review-star-5").performClick()
        composeRule.onNodeWithTag("review-submit-button").performClick()

        waitForTag("my-page-screen")
        composeRule.onNodeWithTag("my-settings-button").performClick()
        waitForTag("settings-screen")
        composeRule.onNodeWithTag("settings-logout-button").performScrollTo().performClick()
    }

    @Test
    fun notificationSettingsAndBlockFlow() {
        launchPastAuth()
        composeRule.onNodeWithTag("home-notifications-button").performClick()
        waitForTag("notifications-screen")
        composeRule.onNodeWithTag("top-settings-button").performClick()
        waitForTag("notification-settings-screen")
        composeRule.onNodeWithTag("top-back-button").performClick()

        composeRule.onNodeWithTag("tab-채팅").performClick()
        composeRule.onNodeWithTag("chat-row-chat-airpods").performClick()
        waitForTag("chat-room-screen")
        composeRule.onNodeWithTag("top-right-button").performClick()
        composeRule.onNodeWithTag("block-user-button").performClick()
        waitForTag("block-list-screen")
    }

    private fun launchPastAuth() {
        waitForTag("login-screen")
        composeRule.onNodeWithTag("login-button").performClick()
        waitForTag("user-home-screen")
    }

    private fun waitForTag(tag: String) {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
