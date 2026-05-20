package com.unitt.unitt.features.onboarding

import androidx.lifecycle.ViewModel
import com.unitt.unitt.core.model.OnboardingStep
import com.unitt.unitt.core.model.TermAgreement
import com.unitt.unitt.core.model.University
import com.unitt.unitt.features.auth.passwordRulesFor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.School,
    val searchText: String = "",
    val selectedUniversity: University? = null,
    val emailLocalPart: String = "student.id",
    val otpDigits: List<String> = listOf("4", "2", "9", "", "", ""),
    val password: String = "Unit1234!",
    val passwordConfirmation: String = "Unit1234!",
    val acceptedTermIds: Set<String> = setOf("age", "service", "privacy", "location", "marketing"),
    val nickname: String = "관악구학생",
    val hasProfilePhoto: Boolean = false,
) {
    val filteredUniversities: List<University>
        get() {
            val query = searchText.trim()
            return if (query.isEmpty()) {
                University.popular
            } else {
                University.popular.filter {
                    it.name.contains(query, ignoreCase = true) ||
                        it.district.contains(query, ignoreCase = true)
                }
            }
        }

    val canContinueFromSchool: Boolean
        get() = selectedUniversity != null

    val canSendVerificationEmail: Boolean
        get() = emailLocalPart.trim().isNotEmpty()

    val emailAddress: String
        get() = "$emailLocalPart@${selectedUniversity?.domain ?: University.popular.first().domain}"

    val otpCode: String
        get() = otpDigits.joinToString(separator = "")

    val canVerifyCode: Boolean
        get() = otpCode.length == 6 && otpDigits.all { it.length == 1 }

    val passwordRules
        get() = passwordRulesFor(password)

    val isPasswordValid: Boolean
        get() = passwordRules.all { it.satisfied }

    val doPasswordsMatch: Boolean
        get() = passwordConfirmation.isNotEmpty() && password == passwordConfirmation

    val canContinuePassword: Boolean
        get() = isPasswordValid && doPasswordsMatch

    val allTermsAccepted: Boolean
        get() = TermAgreement.all.map { it.id }.toSet().all { acceptedTermIds.contains(it) }

    val canContinueTerms: Boolean
        get() = TermAgreement.requiredIds.all { acceptedTermIds.contains(it) }

    val nicknameCountText: String
        get() = "${nickname.length} / 10"

    val isNicknameValid: Boolean
        get() = nickname.trim().length in 2..10

    val canFinishProfile: Boolean
        get() = isNicknameValid
}

class OnboardingViewModel : ViewModel() {
    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    fun updateSearchText(value: String) {
        _state.update { it.copy(searchText = value) }
    }

    fun selectUniversity(university: University) {
        _state.update { it.copy(selectedUniversity = university) }
    }

    fun updateEmailLocalPart(value: String) {
        _state.update { it.copy(emailLocalPart = value) }
    }

    fun updatePassword(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun updatePasswordConfirmation(value: String) {
        _state.update { it.copy(passwordConfirmation = value) }
    }

    fun updateNickname(value: String) {
        _state.update { it.copy(nickname = value) }
    }

    fun setProfilePhotoSelected(selected: Boolean) {
        _state.update { it.copy(hasProfilePhoto = selected) }
    }

    fun goBack() {
        _state.update { state ->
            val next = when (state.step) {
                OnboardingStep.School -> OnboardingStep.School
                OnboardingStep.Email -> OnboardingStep.School
                OnboardingStep.Code -> OnboardingStep.Email
                OnboardingStep.Password -> OnboardingStep.Code
                OnboardingStep.Terms -> OnboardingStep.Password
                OnboardingStep.Profile -> OnboardingStep.Terms
                OnboardingStep.Completed -> OnboardingStep.Profile
            }
            state.copy(step = next)
        }
    }

    fun continueFromCurrentStep() {
        _state.update { state ->
            val next = when {
                state.step == OnboardingStep.School && state.canContinueFromSchool -> OnboardingStep.Email
                state.step == OnboardingStep.Email && state.canSendVerificationEmail -> OnboardingStep.Code
                state.step == OnboardingStep.Code && state.canVerifyCode -> OnboardingStep.Password
                state.step == OnboardingStep.Password && state.canContinuePassword -> OnboardingStep.Terms
                state.step == OnboardingStep.Terms && state.canContinueTerms -> OnboardingStep.Profile
                state.step == OnboardingStep.Profile && state.canFinishProfile -> OnboardingStep.Completed
                else -> state.step
            }
            state.copy(step = next)
        }
    }

    fun appendOtpDigit(digit: String) {
        if (digit.length != 1 || digit.any { !it.isDigit() }) return
        _state.update { state ->
            val nextDigits = state.otpDigits.toMutableList()
            val index = nextDigits.indexOfFirst { it.isEmpty() }
            if (index == -1) {
                state
            } else {
                nextDigits[index] = digit
                val nextState = state.copy(otpDigits = nextDigits)
                if (nextState.canVerifyCode) nextState.copy(step = OnboardingStep.Password) else nextState
            }
        }
    }

    fun removeLastOtpDigit() {
        _state.update { state ->
            val nextDigits = state.otpDigits.toMutableList()
            val index = nextDigits.indexOfLast { it.isNotEmpty() }
            if (index == -1) state else state.copy(otpDigits = nextDigits.apply { set(index, "") })
        }
    }

    fun toggleTerm(term: TermAgreement) {
        _state.update { state ->
            val next = state.acceptedTermIds.toMutableSet()
            if (!next.add(term.id)) next.remove(term.id)
            state.copy(acceptedTermIds = next)
        }
    }

    fun setAllTermsAccepted(accepted: Boolean) {
        _state.update {
            it.copy(acceptedTermIds = if (accepted) TermAgreement.all.map { term -> term.id }.toSet() else emptySet())
        }
    }
}
