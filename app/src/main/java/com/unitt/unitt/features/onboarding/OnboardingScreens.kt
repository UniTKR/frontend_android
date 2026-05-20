package com.unitt.unitt.features.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unitt.unitt.R
import com.unitt.unitt.core.model.OnboardingStep
import com.unitt.unitt.core.model.TermAgreement
import com.unitt.unitt.core.model.University
import com.unitt.unitt.designsystem.UniTTAvatar
import com.unitt.unitt.designsystem.UniTTCard
import com.unitt.unitt.designsystem.UniTTPasswordField
import com.unitt.unitt.designsystem.UniTTPrimaryButton
import com.unitt.unitt.designsystem.UniTTSecondaryButton
import com.unitt.unitt.designsystem.UniTTTextField
import com.unitt.unitt.designsystem.UniTTTheme
import com.unitt.unitt.designsystem.UniTTTopBar
import com.unitt.unitt.designsystem.UniTTRow
import com.unitt.unitt.features.auth.HeaderTitle
import com.unitt.unitt.features.auth.NumberPad
import com.unitt.unitt.features.auth.Notice
import com.unitt.unitt.features.auth.OtpCells
import com.unitt.unitt.features.auth.RuleRow

@Composable
fun OnboardingFlow(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.step) {
        if (state.step == OnboardingStep.Completed) onFinished()
    }

    when (state.step) {
        OnboardingStep.School -> SchoolSelectionScreen(state, viewModel)
        OnboardingStep.Email -> EmailVerificationScreen(state, viewModel)
        OnboardingStep.Code -> OtpCodeScreen(state, viewModel)
        OnboardingStep.Password -> PasswordSetupScreen(state, viewModel)
        OnboardingStep.Terms -> TermsAgreementScreen(state, viewModel)
        OnboardingStep.Profile -> ProfileSetupScreen(state, viewModel)
        OnboardingStep.Completed -> Box(Modifier.fillMaxSize().testTag("onboarding-complete-screen"))
    }
}

@Composable
private fun OnboardingScaffold(
    title: String,
    step: OnboardingStep,
    onBack: (() -> Unit)?,
    tag: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UniTTTheme.colors.backgroundPage)
            .testTag(tag),
    ) {
        UniTTTopBar(title = title, onBack = onBack, rightText = if (step == OnboardingStep.School) "건너뛰기" else null, onRight = {})
        LinearProgressIndicator(
            progress = { step.progress / 6f },
            modifier = Modifier.fillMaxWidth(),
            color = UniTTTheme.colors.brandPrimary,
            trackColor = UniTTTheme.colors.backgroundSubtle,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(UniTTTheme.spacing.x16),
            verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x20),
            content = content,
        )
    }
}

@Composable
private fun SchoolSelectionScreen(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    OnboardingScaffold(
        title = stringResource(R.string.signup),
        step = state.step,
        onBack = null,
        tag = "school-selection-screen",
    ) {
        HeaderTitle(stringResource(R.string.school_select_title), stringResource(R.string.school_select_subtitle))
        UniTTTextField(
            value = state.searchText,
            onValueChange = viewModel::updateSearchText,
            label = stringResource(R.string.search),
            placeholder = stringResource(R.string.school_search_hint),
            modifier = Modifier.testTag("school-search-field"),
        )
        Text(stringResource(R.string.popular_schools), style = UniTTTheme.typography.labelMedium, color = UniTTTheme.colors.textSecondary)
        state.filteredUniversities.forEach { university ->
            SchoolRow(university, state.selectedUniversity?.id == university.id, onClick = { viewModel.selectUniversity(university) })
        }
        Spacer(Modifier.height(UniTTTheme.spacing.x16))
        UniTTPrimaryButton(
            text = stringResource(R.string.next),
            enabled = state.canContinueFromSchool,
            onClick = viewModel::continueFromCurrentStep,
            modifier = Modifier.testTag("primary-cta"),
        )
    }
}

@Composable
private fun SchoolRow(university: University, selected: Boolean, onClick: () -> Unit) {
    UniTTCard(
        modifier = Modifier.testTag("school-row-${university.id}"),
        onClick = onClick,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(university.name, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
                Text("${university.district} · ${university.domain}", style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
            }
            Text(if (selected) "선택됨" else "선택", style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.brandPrimary)
        }
    }
}

@Composable
private fun EmailVerificationScreen(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    OnboardingScaffold(
        title = stringResource(R.string.signup),
        step = state.step,
        onBack = viewModel::goBack,
        tag = "email-verification-screen",
    ) {
        HeaderTitle(stringResource(R.string.email_title), stringResource(R.string.email_subtitle))
        UniTTTextField(
            value = state.emailLocalPart,
            onValueChange = viewModel::updateEmailLocalPart,
            label = stringResource(R.string.email_local_part),
            placeholder = "student.id",
            keyboardType = KeyboardType.Email,
            modifier = Modifier.testTag("email-local-part-field"),
        )
        Notice("@${state.selectedUniversity?.domain ?: University.popular.first().domain} 주소로 인증 메일을 보냅니다.")
        Spacer(Modifier.height(UniTTTheme.spacing.x24))
        UniTTPrimaryButton(
            text = stringResource(R.string.send_verification_email),
            enabled = state.canSendVerificationEmail,
            onClick = viewModel::continueFromCurrentStep,
            modifier = Modifier.testTag("primary-cta"),
        )
    }
}

@Composable
private fun OtpCodeScreen(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    OnboardingScaffold(
        title = stringResource(R.string.code_verification),
        step = state.step,
        onBack = viewModel::goBack,
        tag = "otp-code-screen",
    ) {
        HeaderTitle(stringResource(R.string.otp_signup_title), "${state.emailAddress}로 보낸 코드를 확인해 주세요.")
        OtpCells(state.otpDigits)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.remaining_time), style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.stateDanger)
            Text(stringResource(R.string.resend), style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.brandPrimary, modifier = Modifier.testTag("resend-code-button"))
        }
        Spacer(Modifier.height(UniTTTheme.spacing.x16))
        NumberPad(prefix = "keypad", digitAction = viewModel::appendOtpDigit, deleteAction = viewModel::removeLastOtpDigit)
    }
}

@Composable
private fun PasswordSetupScreen(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    var showsPassword by remember { mutableStateOf(false) }
    var showsConfirmation by remember { mutableStateOf(false) }
    OnboardingScaffold(
        title = stringResource(R.string.signup),
        step = state.step,
        onBack = viewModel::goBack,
        tag = "password-setup-screen",
    ) {
        HeaderTitle(stringResource(R.string.password_signup_title), "학교 인증 후 사용할 비밀번호를 설정해 주세요.")
        UniTTPasswordField(state.password, viewModel::updatePassword, stringResource(R.string.password), showsPassword, { showsPassword = !showsPassword })
        UniTTPasswordField(state.passwordConfirmation, viewModel::updatePasswordConfirmation, stringResource(R.string.confirm_new_password), showsConfirmation, { showsConfirmation = !showsConfirmation })
        UniTTCard {
            state.passwordRules.forEach { RuleRow(it.title, it.satisfied) }
            RuleRow("비밀번호 일치", state.doPasswordsMatch)
        }
        UniTTPrimaryButton(
            text = stringResource(R.string.next),
            enabled = state.canContinuePassword,
            onClick = viewModel::continueFromCurrentStep,
            modifier = Modifier.testTag("primary-cta"),
        )
    }
}

@Composable
private fun TermsAgreementScreen(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    OnboardingScaffold(
        title = stringResource(R.string.signup),
        step = state.step,
        onBack = viewModel::goBack,
        tag = "terms-agreement-screen",
    ) {
        HeaderTitle(stringResource(R.string.terms_title), "필수 약관에 모두 동의해야 가입할 수 있어요.")
        UniTTRow(
            title = stringResource(R.string.agree_all),
            checked = state.allTermsAccepted,
            onCheckedChange = viewModel::setAllTermsAccepted,
            modifier = Modifier.testTag("terms-toggle-all"),
        )
        TermAgreement.all.forEach { term ->
            UniTTRow(
                title = term.title,
                subtitle = if (term.required) "필수" else "선택",
                trailing = if (term.hasDetail) "보기" else null,
                checked = state.acceptedTermIds.contains(term.id),
                onCheckedChange = { viewModel.toggleTerm(term) },
                modifier = Modifier.testTag("term-toggle-${term.id}"),
            )
        }
        UniTTPrimaryButton(
            text = stringResource(R.string.next),
            enabled = state.canContinueTerms,
            onClick = viewModel::continueFromCurrentStep,
            modifier = Modifier.testTag("primary-cta"),
        )
    }
}

@Composable
private fun ProfileSetupScreen(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    OnboardingScaffold(
        title = stringResource(R.string.signup),
        step = state.step,
        onBack = viewModel::goBack,
        tag = "profile-setup-screen",
    ) {
        HeaderTitle(stringResource(R.string.profile_title), "거래할 때 보이는 이름과 프로필을 설정해요.")
        Column(verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12)) {
            UniTTAvatar(if (state.hasProfilePhoto) "✓" else "U", modifier = Modifier.testTag("profile-photo-picker"))
            UniTTSecondaryButton(
                text = if (state.hasProfilePhoto) "사진 선택됨" else "프로필 사진 선택",
                onClick = { viewModel.setProfilePhotoSelected(!state.hasProfilePhoto) },
            )
        }
        UniTTTextField(
            value = state.nickname,
            onValueChange = viewModel::updateNickname,
            label = stringResource(R.string.nickname),
            modifier = Modifier.testTag("nickname-field"),
        )
        Text(state.nicknameCountText, style = UniTTTheme.typography.bodySmall, color = if (state.isNicknameValid) UniTTTheme.colors.textSecondary else UniTTTheme.colors.stateDanger)
        UniTTPrimaryButton(
            text = stringResource(R.string.start),
            enabled = state.canFinishProfile,
            onClick = viewModel::continueFromCurrentStep,
            modifier = Modifier.testTag("primary-cta"),
        )
    }
}
