package com.unitt.unitt

import com.unitt.unitt.core.model.AuthRoute
import com.unitt.unitt.core.model.ListingCreateCategory
import com.unitt.unitt.core.model.ListingStatus
import com.unitt.unitt.core.model.NotificationTab
import com.unitt.unitt.core.model.OnboardingStep
import com.unitt.unitt.core.model.TermAgreement
import com.unitt.unitt.core.model.University
import com.unitt.unitt.features.auth.AuthViewModel
import com.unitt.unitt.features.main.ReportStep
import com.unitt.unitt.features.main.UserPrototypeViewModel
import com.unitt.unitt.features.onboarding.OnboardingViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PrototypeViewModelTest {
    @Test
    fun authLoginAndPasswordResetValidation() {
        val viewModel = AuthViewModel()

        assertTrue(viewModel.state.value.canLogin)
        viewModel.updateLoginPassword("")
        assertFalse(viewModel.state.value.canLogin)

        viewModel.showForgotEmail()
        assertEquals(AuthRoute.ForgotEmail, viewModel.state.value.route)
        viewModel.requestResetCode()
        assertEquals(AuthRoute.ForgotCode, viewModel.state.value.route)
        viewModel.appendResetDigit("1")
        viewModel.appendResetDigit("2")
        viewModel.appendResetDigit("3")
        assertEquals(AuthRoute.ResetPassword, viewModel.state.value.route)

        viewModel.updateNewPassword("Unit1234!")
        viewModel.updateNewPasswordConfirmation("Unit1234!")
        assertTrue(viewModel.state.value.canResetPassword)
    }

    @Test
    fun onboardingSignupSixStepsAreValidated() {
        val viewModel = OnboardingViewModel()

        assertFalse(viewModel.state.value.canContinueFromSchool)
        viewModel.selectUniversity(University.popular.first())
        assertTrue(viewModel.state.value.canContinueFromSchool)
        viewModel.continueFromCurrentStep()
        assertEquals(OnboardingStep.Email, viewModel.state.value.step)

        viewModel.updateEmailLocalPart("   ")
        assertFalse(viewModel.state.value.canSendVerificationEmail)
        viewModel.updateEmailLocalPart("student.id")
        viewModel.continueFromCurrentStep()
        assertEquals(OnboardingStep.Code, viewModel.state.value.step)

        viewModel.appendOtpDigit("1")
        viewModel.appendOtpDigit("2")
        viewModel.appendOtpDigit("3")
        assertEquals(OnboardingStep.Password, viewModel.state.value.step)

        viewModel.updatePassword("short")
        assertFalse(viewModel.state.value.canContinuePassword)
        viewModel.updatePassword("Unit1234!")
        viewModel.updatePasswordConfirmation("Unit1234!")
        assertTrue(viewModel.state.value.canContinuePassword)
    }

    @Test
    fun termsAndNicknameGateSignupCompletion() {
        val viewModel = OnboardingViewModel()

        viewModel.setAllTermsAccepted(false)
        assertFalse(viewModel.state.value.canContinueTerms)
        TermAgreement.requiredIds.forEach { id ->
            TermAgreement.all.first { it.id == id }.also(viewModel::toggleTerm)
        }
        assertTrue(viewModel.state.value.canContinueTerms)

        viewModel.updateNickname("김")
        assertFalse(viewModel.state.value.isNicknameValid)
        viewModel.updateNickname("관악구학생")
        assertTrue(viewModel.state.value.isNicknameValid)
        viewModel.updateNickname("abcdefghijkl")
        assertFalse(viewModel.state.value.isNicknameValid)
    }

    @Test
    fun searchAndCreateListingStateAreValidated() {
        val viewModel = UserPrototypeViewModel()

        assertTrue(viewModel.state.value.searchResults.isEmpty())
        viewModel.updateSearchText("에어팟")
        assertEquals("airpods", viewModel.state.value.searchResults.first().id)

        assertTrue(viewModel.state.value.canSubmitListing)
        viewModel.updateCreateTitle("   ")
        assertFalse(viewModel.state.value.canSubmitListing)

        viewModel.updateCreateTitle("자료구조 이론서 9판")
        viewModel.selectCreateCategory(ListingCreateCategory.Electronics)
        assertEquals(ListingCreateCategory.Electronics, viewModel.state.value.selectedCreateCategory)
    }

    @Test
    fun reportBlockNotificationTradeAndSettingsStateAreValidated() {
        val viewModel = UserPrototypeViewModel()

        viewModel.state.value.also { assertEquals(ListingStatus.Reserved, it.tradeStatus) }
        viewModel.completeTrade()
        assertEquals(ListingStatus.Completed, viewModel.state.value.tradeStatus)

        viewModel.startReport()
        viewModel.nextReportStep()
        assertEquals(ReportStep.Reason, viewModel.state.value.reportStep)
        assertFalse(viewModel.state.value.canContinueReport)
        viewModel.updateReportReason("노쇼/약속 불이행")
        assertTrue(viewModel.state.value.canContinueReport)

        viewModel.blockCurrentUser()
        assertTrue(viewModel.state.value.showingBlockToast)

        viewModel.selectNotificationTab(NotificationTab.System)
        assertEquals(1, viewModel.state.value.visibleNotifications.size)

        assertFalse(viewModel.state.value.showingLogoutDialog)
        viewModel.requestLogout()
        assertTrue(viewModel.state.value.showingLogoutDialog)
    }
}
