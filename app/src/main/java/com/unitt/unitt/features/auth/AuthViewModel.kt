package com.unitt.unitt.features.auth

import androidx.lifecycle.ViewModel
import com.unitt.unitt.core.model.AuthRoute
import com.unitt.unitt.core.model.PasswordRule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthUiState(
    val route: AuthRoute = AuthRoute.Splash,
    val loginEmail: String = "student.id@snu.ac.kr",
    val loginPassword: String = "Unit1234!",
    val resetEmail: String = "student.id@snu.ac.kr",
    val resetDigits: List<String> = listOf("4", "2", "9", "", "", ""),
    val newPassword: String = "Unit1234!",
    val newPasswordConfirmation: String = "Unit1234!",
) {
    val canLogin: Boolean
        get() = loginEmail.contains("@") && loginPassword.isNotEmpty()

    val canRequestResetCode: Boolean
        get() = resetEmail.contains("@") && resetEmail.contains(".")

    val resetCode: String
        get() = resetDigits.joinToString(separator = "")

    val canVerifyResetCode: Boolean
        get() = resetCode.length == 6 && resetDigits.all { it.length == 1 }

    val passwordRules: List<PasswordRule>
        get() = passwordRulesFor(newPassword)

    val canResetPassword: Boolean
        get() = passwordRules.all { it.satisfied } &&
            newPasswordConfirmation.isNotEmpty() &&
            newPassword == newPasswordConfirmation
}

fun passwordRulesFor(password: String): List<PasswordRule> = listOf(
    PasswordRule("8자 이상", password.length >= 8),
    PasswordRule("영문 포함", password.any { it.isLetter() }),
    PasswordRule("숫자 포함", password.any { it.isDigit() }),
    PasswordRule("특수문자 포함", password.any { !it.isLetterOrDigit() }),
)

class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun finishSplash() {
        _state.update { state ->
            if (state.route == AuthRoute.Splash) state.copy(route = AuthRoute.Login) else state
        }
    }

    fun updateLoginEmail(value: String) {
        _state.update { it.copy(loginEmail = value) }
    }

    fun updateLoginPassword(value: String) {
        _state.update { it.copy(loginPassword = value) }
    }

    fun login(): Boolean {
        return state.value.canLogin
    }

    fun showForgotEmail() {
        _state.update { it.copy(route = AuthRoute.ForgotEmail) }
    }

    fun showSignup() {
        _state.update { it.copy(route = AuthRoute.Signup) }
    }

    fun backToLogin() {
        _state.update { it.copy(route = AuthRoute.Login) }
    }

    fun updateResetEmail(value: String) {
        _state.update { it.copy(resetEmail = value) }
    }

    fun requestResetCode() {
        _state.update { state ->
            if (state.canRequestResetCode) state.copy(route = AuthRoute.ForgotCode) else state
        }
    }

    fun appendResetDigit(digit: String) {
        if (digit.length != 1 || digit.any { !it.isDigit() }) return
        _state.update { state ->
            val nextDigits = state.resetDigits.toMutableList()
            val index = nextDigits.indexOfFirst { it.isEmpty() }
            if (index == -1) {
                state
            } else {
                nextDigits[index] = digit
                val nextState = state.copy(resetDigits = nextDigits)
                if (nextState.canVerifyResetCode) nextState.copy(route = AuthRoute.ResetPassword) else nextState
            }
        }
    }

    fun removeResetDigit() {
        _state.update { state ->
            val nextDigits = state.resetDigits.toMutableList()
            val index = nextDigits.indexOfLast { it.isNotEmpty() }
            if (index == -1) state else state.copy(resetDigits = nextDigits.apply { set(index, "") })
        }
    }

    fun updateNewPassword(value: String) {
        _state.update { it.copy(newPassword = value) }
    }

    fun updateNewPasswordConfirmation(value: String) {
        _state.update { it.copy(newPasswordConfirmation = value) }
    }

    fun resetPasswordAndReturnToLogin() {
        _state.update { state ->
            if (!state.canResetPassword) {
                state
            } else {
                state.copy(
                    route = AuthRoute.Login,
                    loginEmail = state.resetEmail,
                    loginPassword = state.newPassword,
                )
            }
        }
    }
}
