package com.unitt.unitt.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class UniTTColors(
    val brandPrimary: Color,
    val brandPrimaryHover: Color,
    val brandPrimaryPressed: Color,
    val brandPrimarySubtle: Color,
    val brandBorder: Color,
    val brandOnPrimary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val textOnBrand: Color,
    val textLink: Color,
    val backgroundPage: Color,
    val backgroundSurface: Color,
    val backgroundElevated: Color,
    val backgroundSubtle: Color,
    val backgroundCanvas: Color,
    val borderSubtle: Color,
    val borderDefault: Color,
    val borderStrong: Color,
    val borderFocus: Color,
    val stateSuccess: Color,
    val stateSuccessText: Color,
    val stateSuccessBg: Color,
    val stateWarning: Color,
    val stateWarningText: Color,
    val stateWarningBg: Color,
    val stateDanger: Color,
    val stateDangerText: Color,
    val stateDangerBg: Color,
    val stateNeutral: Color,
    val chipListedBg: Color,
    val chipListedInk: Color,
    val chipReservedBg: Color,
    val chipReservedInk: Color,
    val chipCompletedBg: Color,
    val chipCompletedInk: Color,
    val chipCanceledBg: Color,
    val chipCanceledInk: Color,
    val chipDisputedBg: Color,
    val chipDisputedInk: Color,
)

@Immutable
data class UniTTSpacing(
    val x2: Dp = 2.dp,
    val x4: Dp = 4.dp,
    val x6: Dp = 6.dp,
    val x8: Dp = 8.dp,
    val x10: Dp = 10.dp,
    val x12: Dp = 12.dp,
    val x16: Dp = 16.dp,
    val x20: Dp = 20.dp,
    val x24: Dp = 24.dp,
    val x28: Dp = 28.dp,
    val x32: Dp = 32.dp,
    val x40: Dp = 40.dp,
    val x48: Dp = 48.dp,
    val x56: Dp = 56.dp,
    val x64: Dp = 64.dp,
)

@Immutable
data class UniTTRadius(
    val sm: Dp = 6.dp,
    val md: Dp = 8.dp,
    val lg: Dp = 10.dp,
    val xl: Dp = 12.dp,
    val pill: Dp = 999.dp,
)

@Immutable
data class UniTTTypography(
    val displayMedium: TextStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.sp),
    val heading1: TextStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.sp),
    val heading2: TextStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.sp),
    val heading3: TextStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.sp),
    val bodyLarge: TextStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp),
    val bodyMedium: TextStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp),
    val bodySmall: TextStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp),
    val labelLarge: TextStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.sp),
    val labelMedium: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.sp),
    val labelSmall: TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.sp),
    val numeric: TextStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, letterSpacing = 0.sp),
    val otp: TextStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, letterSpacing = 0.sp),
)

@Immutable
data class UniTTSizes(
    val touchMinimum: Dp = 48.dp,
    val inputHeight: Dp = 48.dp,
    val navBarHeight: Dp = 48.dp,
    val ctaHeight: Dp = 52.dp,
    val tabBarHeight: Dp = 56.dp,
    val avatarXL: Dp = 80.dp,
)

private val LightColors = UniTTColors(
    brandPrimary = Color(0xFF4F46E5),
    brandPrimaryHover = Color(0xFF4338CA),
    brandPrimaryPressed = Color(0xFF3730A3),
    brandPrimarySubtle = Color(0xFFEEF2FF),
    brandBorder = Color(0xFFC7D2FE),
    brandOnPrimary = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF0F172A),
    textSecondary = Color(0xFF475569),
    textTertiary = Color(0xFF94A3B8),
    textDisabled = Color(0xFFCBD5E1),
    textOnBrand = Color(0xFFFFFFFF),
    textLink = Color(0xFF4F46E5),
    backgroundPage = Color(0xFFFFFFFF),
    backgroundSurface = Color(0xFFF8FAFC),
    backgroundElevated = Color(0xFFFFFFFF),
    backgroundSubtle = Color(0xFFF1F5F9),
    backgroundCanvas = Color(0xFFEFF6FF),
    borderSubtle = Color(0xFFF1F5F9),
    borderDefault = Color(0xFFE2E8F0),
    borderStrong = Color(0xFFCBD5E1),
    borderFocus = Color(0xFF4F46E5),
    stateSuccess = Color(0xFF10B981),
    stateSuccessText = Color(0xFF047857),
    stateSuccessBg = Color(0xFFECFDF5),
    stateWarning = Color(0xFFF59E0B),
    stateWarningText = Color(0xFFB45309),
    stateWarningBg = Color(0xFFFFFBEB),
    stateDanger = Color(0xFFDC2626),
    stateDangerText = Color(0xFFB91C1C),
    stateDangerBg = Color(0xFFFEF2F2),
    stateNeutral = Color(0xFF94A3B8),
    chipListedBg = Color(0xFFF1F5F9),
    chipListedInk = Color(0xFF475569),
    chipReservedBg = Color(0xFFFFEDD5),
    chipReservedInk = Color(0xFFC2410C),
    chipCompletedBg = Color(0xFFECFDF5),
    chipCompletedInk = Color(0xFF047857),
    chipCanceledBg = Color(0xFFF8FAFC),
    chipCanceledInk = Color(0xFF64748B),
    chipDisputedBg = Color(0xFFFEF2F2),
    chipDisputedInk = Color(0xFFB91C1C),
)

private val DarkColors = LightColors.copy(
    brandPrimary = Color(0xFF818CF8),
    brandPrimaryHover = Color(0xFFA5B4FC),
    brandPrimaryPressed = Color(0xFFC7D2FE),
    brandPrimarySubtle = Color(0xFF312E81),
    textPrimary = Color(0xFFF8FAFC),
    textSecondary = Color(0xFFCBD5E1),
    textTertiary = Color(0xFF94A3B8),
    textDisabled = Color(0xFF64748B),
    backgroundPage = Color(0xFF0B1020),
    backgroundSurface = Color(0xFF0F172A),
    backgroundElevated = Color(0xFF1E293B),
    backgroundSubtle = Color(0xFF0F172A),
    backgroundCanvas = Color(0xFF111827),
    borderSubtle = Color(0xFF1E293B),
    borderDefault = Color(0xFF334155),
    borderStrong = Color(0xFF475569),
)

private val LocalUniTTColors = staticCompositionLocalOf { LightColors }
private val LocalUniTTSpacing = staticCompositionLocalOf { UniTTSpacing() }
private val LocalUniTTRadius = staticCompositionLocalOf { UniTTRadius() }
private val LocalUniTTTypography = staticCompositionLocalOf { UniTTTypography() }
private val LocalUniTTSizes = staticCompositionLocalOf { UniTTSizes() }

object UniTTTheme {
    val colors: UniTTColors
        @Composable
        @ReadOnlyComposable
        get() = LocalUniTTColors.current

    val spacing: UniTTSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalUniTTSpacing.current

    val radius: UniTTRadius
        @Composable
        @ReadOnlyComposable
        get() = LocalUniTTRadius.current

    val typography: UniTTTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalUniTTTypography.current

    val sizes: UniTTSizes
        @Composable
        @ReadOnlyComposable
        get() = LocalUniTTSizes.current
}

@Composable
fun UniTTAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    androidx.compose.runtime.CompositionLocalProvider(
        LocalUniTTColors provides colors,
        LocalUniTTSpacing provides UniTTSpacing(),
        LocalUniTTRadius provides UniTTRadius(),
        LocalUniTTTypography provides UniTTTypography(),
        LocalUniTTSizes provides UniTTSizes(),
    ) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = colors.brandPrimary,
                onPrimary = colors.brandOnPrimary,
                background = colors.backgroundPage,
                onBackground = colors.textPrimary,
                surface = colors.backgroundElevated,
                onSurface = colors.textPrimary,
                error = colors.stateDanger,
            ),
            content = content,
        )
    }
}
