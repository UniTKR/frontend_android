package com.unitt.unitt.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unitt.unitt.R
import com.unitt.unitt.core.model.AuthRoute
import com.unitt.unitt.designsystem.UniTTAppTheme
import com.unitt.unitt.designsystem.UniTTCard
import com.unitt.unitt.designsystem.UniTTPasswordField
import com.unitt.unitt.designsystem.UniTTPrimaryButton
import com.unitt.unitt.designsystem.UniTTSecondaryButton
import com.unitt.unitt.designsystem.UniTTTextField
import com.unitt.unitt.designsystem.UniTTTheme
import com.unitt.unitt.designsystem.UniTTTopBar
import com.unitt.unitt.designsystem.UniTTWordmark
import kotlinx.coroutines.delay

@Composable
fun AuthFlow(
    viewModel: AuthViewModel,
    onAuthenticated: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (state.route) {
        AuthRoute.Splash -> SplashScreen(onFinished = viewModel::finishSplash)
        AuthRoute.Login -> LoginScreen(state, viewModel, onAuthenticated)
        AuthRoute.ForgotEmail -> ForgotEmailScreen(state, viewModel)
        AuthRoute.ForgotCode -> ForgotCodeScreen(state, viewModel)
        AuthRoute.ResetPassword -> ResetPasswordScreen(state, viewModel)
        AuthRoute.Signup -> com.unitt.unitt.features.onboarding.OnboardingFlow(onFinished = onAuthenticated)
    }
}

@Composable
private fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(700)
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UniTTTheme.colors.backgroundPage)
            .testTag("splash-screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.unitt_glyph),
            contentDescription = null,
            modifier = Modifier.size(UniTTTheme.spacing.x64),
        )
        Spacer(Modifier.height(UniTTTheme.spacing.x16))
        UniTTWordmark(compact = false)
        Spacer(Modifier.height(UniTTTheme.spacing.x8))
        Text(
            stringResource(R.string.brand_tagline),
            style = UniTTTheme.typography.bodyMedium,
            color = UniTTTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun LoginScreen(
    state: AuthUiState,
    viewModel: AuthViewModel,
    onAuthenticated: () -> Unit,
) {
    var showsPassword by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(UniTTTheme.colors.backgroundPage)
            .verticalScroll(rememberScrollState())
            .padding(UniTTTheme.spacing.x16)
            .testTag("login-screen"),
        verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x24),
    ) {
        Spacer(Modifier.height(UniTTTheme.spacing.x24))
        UniTTWordmark()
        Column(verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
            Text(
                stringResource(R.string.login_title),
                style = UniTTTheme.typography.displayMedium,
                color = UniTTTheme.colors.textPrimary,
            )
            Text(
                stringResource(R.string.login_subtitle),
                style = UniTTTheme.typography.bodyMedium,
                color = UniTTTheme.colors.textSecondary,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16)) {
            UniTTTextField(
                value = state.loginEmail,
                onValueChange = viewModel::updateLoginEmail,
                label = stringResource(R.string.school_email),
                placeholder = "student.id@snu.ac.kr",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.testTag("login-email-field"),
            )
            UniTTPasswordField(
                value = state.loginPassword,
                onValueChange = viewModel::updateLoginPassword,
                label = stringResource(R.string.password),
                showsText = showsPassword,
                onToggleVisibility = { showsPassword = !showsPassword },
                modifier = Modifier.testTag("login-password-field"),
            )
            UniTTPrimaryButton(
                text = stringResource(R.string.login),
                enabled = state.canLogin,
                onClick = {
                    if (viewModel.login()) onAuthenticated()
                },
                modifier = Modifier.testTag("login-button"),
            )
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = viewModel::showForgotEmail, modifier = Modifier.testTag("forgot-password-button")) {
                Text(stringResource(R.string.forgot_password), color = UniTTTheme.colors.brandPrimary)
            }
            TextButton(onClick = viewModel::showSignup, modifier = Modifier.testTag("signup-button")) {
                Text(stringResource(R.string.signup), color = UniTTTheme.colors.brandPrimary)
            }
        }

        DividerText(stringResource(R.string.divider_or))
        UniTTSecondaryButton(stringResource(R.string.continue_with_apple), onClick = {})
        UniTTSecondaryButton(stringResource(R.string.continue_with_kakao), onClick = {})
    }
}

@Composable
private fun ForgotEmailScreen(state: AuthUiState, viewModel: AuthViewModel) {
    AuthScaffold(
        title = stringResource(R.string.find_password),
        onBack = viewModel::backToLogin,
        modifier = Modifier.testTag("forgot-email-screen"),
    ) {
        HeaderTitle(
            title = stringResource(R.string.forgot_email_title),
            subtitle = stringResource(R.string.forgot_email_subtitle),
        )
        UniTTTextField(
            value = state.resetEmail,
            onValueChange = viewModel::updateResetEmail,
            label = stringResource(R.string.school_email),
            placeholder = "student.id@snu.ac.kr",
            keyboardType = KeyboardType.Email,
            modifier = Modifier.testTag("forgot-email-field"),
        )
        Notice("학교 도메인지 먼저 확인할게요.")
        Spacer(Modifier.weight(1f))
        UniTTPrimaryButton(
            text = stringResource(R.string.request_code),
            enabled = state.canRequestResetCode,
            onClick = viewModel::requestResetCode,
            modifier = Modifier.testTag("forgot-email-next"),
        )
    }
}

@Composable
private fun ForgotCodeScreen(state: AuthUiState, viewModel: AuthViewModel) {
    AuthScaffold(
        title = stringResource(R.string.code_verification),
        onBack = { viewModel.showForgotEmail() },
        modifier = Modifier.testTag("forgot-code-screen"),
    ) {
        HeaderTitle(
            title = stringResource(R.string.otp_title),
            subtitle = "${state.resetEmail}로 보낸 코드를 확인해 주세요.",
        )
        OtpCells(state.resetDigits)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.remaining_time), style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.stateDanger)
            Text(stringResource(R.string.resend), style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.brandPrimary)
        }
        Notice("5회 이상 실패하면 5분 동안 다시 시도할 수 없어요.")
        Spacer(Modifier.weight(1f))
        NumberPad(
            prefix = "forgot-keypad",
            digitAction = viewModel::appendResetDigit,
            deleteAction = viewModel::removeResetDigit,
        )
    }
}

@Composable
private fun ResetPasswordScreen(state: AuthUiState, viewModel: AuthViewModel) {
    var showsPassword by remember { mutableStateOf(false) }
    var showsConfirmation by remember { mutableStateOf(false) }
    AuthScaffold(
        title = stringResource(R.string.reset_password),
        onBack = { viewModel.requestResetCode() },
        modifier = Modifier.testTag("reset-password-screen"),
    ) {
        HeaderTitle(
            title = stringResource(R.string.reset_password_title),
            subtitle = stringResource(R.string.reset_password_subtitle),
        )
        UniTTPasswordField(
            value = state.newPassword,
            onValueChange = viewModel::updateNewPassword,
            label = stringResource(R.string.new_password),
            showsText = showsPassword,
            onToggleVisibility = { showsPassword = !showsPassword },
            modifier = Modifier.testTag("reset-password-field"),
        )
        UniTTPasswordField(
            value = state.newPasswordConfirmation,
            onValueChange = viewModel::updateNewPasswordConfirmation,
            label = stringResource(R.string.confirm_new_password),
            showsText = showsConfirmation,
            onToggleVisibility = { showsConfirmation = !showsConfirmation },
            modifier = Modifier.testTag("reset-password-confirmation-field"),
        )
        UniTTCard {
            state.passwordRules.forEach { rule ->
                RuleRow(rule.title, rule.satisfied)
            }
            RuleRow("새 비밀번호 일치", state.newPassword == state.newPasswordConfirmation)
        }
        Spacer(Modifier.weight(1f))
        UniTTPrimaryButton(
            text = stringResource(R.string.change_password),
            enabled = state.canResetPassword,
            onClick = viewModel::resetPasswordAndReturnToLogin,
            modifier = Modifier.testTag("reset-password-submit"),
        )
    }
}

@Composable
fun AuthScaffold(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(UniTTTheme.colors.backgroundPage),
    ) {
        UniTTTopBar(title = title, onBack = onBack, modifier = Modifier.testTag("auth-back-button"))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .padding(UniTTTheme.spacing.x16),
            verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x20),
            content = content,
        )
    }
}

@Composable
fun HeaderTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
        Text(title, style = UniTTTheme.typography.displayMedium, color = UniTTTheme.colors.textPrimary)
        Text(subtitle, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
    }
}

@Composable
fun Notice(text: String) {
    UniTTCard {
        Text(text, style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
    }
}

@Composable
fun RuleRow(title: String, satisfied: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8), verticalAlignment = Alignment.CenterVertically) {
        Text(if (satisfied) "✓" else "○", color = if (satisfied) UniTTTheme.colors.stateSuccess else UniTTTheme.colors.textTertiary)
        Text(title, style = UniTTTheme.typography.labelSmall, color = if (satisfied) UniTTTheme.colors.stateSuccessText else UniTTTheme.colors.textSecondary)
    }
}

@Composable
fun OtpCells(digits: List<String>, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().testTag("otp-cells"), horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
        digits.forEachIndexed { index, digit ->
            UniTTCard(modifier = Modifier.weight(1f)) {
                Text(
                    digit.ifEmpty { " " },
                    style = UniTTTheme.typography.otp,
                    color = UniTTTheme.colors.textPrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                if (digit.isEmpty() && index == digits.indexOfFirst { it.isEmpty() }) {
                    HorizontalDivider(color = UniTTTheme.colors.borderFocus)
                }
            }
        }
    }
}

@Composable
fun NumberPad(
    prefix: String,
    digitAction: (String) -> Unit,
    deleteAction: () -> Unit,
) {
    val rows = listOf(listOf("1", "2", "3"), listOf("4", "5", "6"), listOf("7", "8", "9"), listOf("", "0", "delete"))
    Column(verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
        rows.forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
                row.forEach { value ->
                    val label = if (value == "delete") "⌫" else value
                    UniTTSecondaryButton(
                        text = label,
                        enabled = value.isNotEmpty(),
                        onClick = {
                            if (value == "delete") deleteAction() else digitAction(value)
                        },
                        modifier = Modifier.weight(1f).testTag(if (value == "delete") "$prefix-delete" else "$prefix-digit-$value"),
                    )
                }
            }
        }
    }
}

@Composable
private fun DividerText(text: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12)) {
        HorizontalDivider(Modifier.weight(1f), color = UniTTTheme.colors.borderDefault)
        Text(text, style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.textTertiary)
        HorizontalDivider(Modifier.weight(1f), color = UniTTTheme.colors.borderDefault)
    }
}

@Composable
fun AuthPreview() {
    UniTTAppTheme {
        LoginScreen(AuthUiState(route = AuthRoute.Login), AuthViewModel(), onAuthenticated = {})
    }
}
