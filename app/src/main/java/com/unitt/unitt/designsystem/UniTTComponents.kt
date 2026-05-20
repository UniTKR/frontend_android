package com.unitt.unitt.designsystem

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.unitt.unitt.R

data class UniTTTabItem(
    val label: String,
    val icon: ImageVector,
    val contentDescription: String = label,
)

@Composable
fun UniTTIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = UniTTTheme.colors.textPrimary,
    testTag: String? = null,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(UniTTTheme.sizes.touchMinimum)
            .then(if (testTag == null) Modifier else Modifier.testTag(testTag)),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
fun UniTTScreenScaffold(
    modifier: Modifier = Modifier,
    topBar: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(UniTTTheme.colors.backgroundPage)
            .imePadding(),
    ) {
        if (topBar == null) {
            Spacer(Modifier.statusBarsPadding())
        } else {
            topBar()
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            content = content,
        )
        bottomBar?.invoke()
    }
}

@Composable
fun UniTTBottomActionBar(
    modifier: Modifier = Modifier,
    applyNavigationPadding: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(UniTTTheme.colors.backgroundElevated)
            .then(if (applyNavigationPadding) Modifier.navigationBarsPadding() else Modifier),
    ) {
        HorizontalDivider(color = UniTTTheme.colors.borderDefault)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = UniTTTheme.spacing.x16, vertical = UniTTTheme.spacing.x12),
            horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x10),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
fun UniTTBottomTabs(
    items: List<UniTTTabItem>,
    selectedLabel: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(UniTTTheme.colors.backgroundElevated)
            .navigationBarsPadding(),
    ) {
        HorizontalDivider(color = UniTTTheme.colors.borderDefault)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(UniTTTheme.sizes.tabBarHeight)
                .padding(horizontal = UniTTTheme.spacing.x8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            items.forEach { item ->
                val selected = selectedLabel == item.label
                TextButton(
                    onClick = { onSelect(item.label) },
                    modifier = Modifier
                        .weight(1f)
                        .height(UniTTTheme.sizes.touchMinimum)
                        .testTag("tab-${item.label}"),
                    contentPadding = PaddingValues(horizontal = UniTTTheme.spacing.x4),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x2),
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.contentDescription,
                            tint = if (selected) UniTTTheme.colors.brandPrimary else UniTTTheme.colors.textSecondary,
                            modifier = Modifier
                                .size(21.dp)
                                .testTag("tab-icon-${item.label}"),
                        )
                        Text(
                            item.label,
                            style = UniTTTheme.typography.labelSmall,
                            color = if (selected) UniTTTheme.colors.brandPrimary else UniTTTheme.colors.textSecondary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UniTTWordmark(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x10),
    ) {
        Image(
            painter = painterResource(R.drawable.unitt_glyph_on_brand),
            contentDescription = null,
            modifier = Modifier.size(if (compact) 32.dp else 44.dp),
        )
        Text(
            text = "UniTT",
            style = if (compact) UniTTTheme.typography.heading2 else UniTTTheme.typography.displayMedium,
            color = UniTTTheme.colors.textPrimary,
        )
    }
}

@Composable
fun UniTTPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = UniTTTheme.sizes.ctaHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = UniTTTheme.colors.brandPrimary,
            contentColor = UniTTTheme.colors.textOnBrand,
            disabledContainerColor = UniTTTheme.colors.backgroundSubtle,
            disabledContentColor = UniTTTheme.colors.textDisabled,
        ),
        shape = RoundedCornerShape(UniTTTheme.radius.xl),
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(UniTTTheme.spacing.x6))
        }
        Text(text = text, style = UniTTTheme.typography.labelLarge)
    }
}

@Composable
fun UniTTSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = UniTTTheme.sizes.ctaHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = UniTTTheme.colors.backgroundElevated,
            contentColor = UniTTTheme.colors.textPrimary,
            disabledContainerColor = UniTTTheme.colors.backgroundSubtle,
            disabledContentColor = UniTTTheme.colors.textDisabled,
        ),
        shape = RoundedCornerShape(UniTTTheme.radius.xl),
        border = BorderStroke(1.dp, UniTTTheme.colors.borderDefault),
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(UniTTTheme.spacing.x6))
        }
        Text(text = text, style = UniTTTheme.typography.labelLarge)
    }
}

@Composable
fun UniTTTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    leadingIconDescription: String? = null,
    trailingIconDescription: String? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x4), modifier = modifier) {
        Text(label, style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.textSecondary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = UniTTTheme.colors.textTertiary) },
            textStyle = UniTTTheme.typography.bodyMedium,
            singleLine = singleLine,
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = leadingIconDescription,
                        tint = UniTTTheme.colors.textTertiary,
                    )
                }
            },
            trailingIcon = trailingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = trailingIconDescription,
                        tint = UniTTTheme.colors.textTertiary,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(UniTTTheme.radius.lg),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = UniTTTheme.colors.borderFocus,
                unfocusedBorderColor = UniTTTheme.colors.borderDefault,
                focusedContainerColor = UniTTTheme.colors.backgroundElevated,
                unfocusedContainerColor = UniTTTheme.colors.backgroundElevated,
                focusedTextColor = UniTTTheme.colors.textPrimary,
                unfocusedTextColor = UniTTTheme.colors.textPrimary,
                cursorColor = UniTTTheme.colors.brandPrimary,
            ),
        )
    }
}

@Composable
fun UniTTPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showsText: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x4), modifier = modifier) {
        Text(label, style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.textSecondary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = UniTTTheme.typography.bodyMedium,
            singleLine = true,
            visualTransformation = if (showsText) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (showsText) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (showsText) "비밀번호 숨기기" else "비밀번호 보기",
                        tint = UniTTTheme.colors.textSecondary,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(UniTTTheme.radius.lg),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = UniTTTheme.colors.borderFocus,
                unfocusedBorderColor = UniTTTheme.colors.borderDefault,
                focusedContainerColor = UniTTTheme.colors.backgroundElevated,
                unfocusedContainerColor = UniTTTheme.colors.backgroundElevated,
                focusedTextColor = UniTTTheme.colors.textPrimary,
                unfocusedTextColor = UniTTTheme.colors.textPrimary,
                cursorColor = UniTTTheme.colors.brandPrimary,
            ),
        )
    }
}

@Composable
fun UniTTTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    rightText: String? = null,
    onRight: (() -> Unit)? = null,
    rightIcon: ImageVector? = null,
    rightContentDescription: String? = rightText,
    rightTestTag: String? = null,
    applyStatusPadding: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(UniTTTheme.colors.backgroundElevated)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (applyStatusPadding) Modifier.statusBarsPadding() else Modifier)
                .height(UniTTTheme.sizes.navBarHeight)
                .padding(horizontal = UniTTTheme.spacing.x6),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onBack == null) {
                Spacer(Modifier.size(UniTTTheme.sizes.touchMinimum))
            } else {
                UniTTIconButton(
                    icon = Icons.Outlined.ArrowBackIosNew,
                    contentDescription = "뒤로가기",
                    onClick = onBack,
                    modifier = Modifier
                        .size(UniTTTheme.sizes.touchMinimum),
                    testTag = "top-back-button",
                )
            }
            Text(
                title,
                modifier = Modifier.weight(1f),
                style = UniTTTheme.typography.heading3,
                color = UniTTTheme.colors.textPrimary,
                maxLines = 1,
            )
            if (rightText == null || onRight == null) {
                Spacer(Modifier.size(UniTTTheme.sizes.touchMinimum))
            } else if (rightIcon != null) {
                UniTTIconButton(
                    icon = rightIcon,
                    contentDescription = rightContentDescription ?: rightText,
                    onClick = onRight,
                    tint = UniTTTheme.colors.brandPrimary,
                    testTag = rightTestTag ?: if (rightText == "설정") "top-settings-button" else "top-right-button",
                )
            } else {
                TextButton(
                    onClick = onRight,
                    modifier = Modifier
                        .height(UniTTTheme.sizes.touchMinimum)
                        .testTag(rightTestTag ?: if (rightText == "설정") "top-settings-button" else "top-right-button"),
                    contentPadding = PaddingValues(horizontal = UniTTTheme.spacing.x8),
                ) {
                    Text(rightText, style = UniTTTheme.typography.labelMedium, color = UniTTTheme.colors.brandPrimary)
                }
            }
        }
        HorizontalDivider(color = UniTTTheme.colors.borderDefault)
    }
}

@Composable
fun UniTTCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(UniTTTheme.radius.lg)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(UniTTTheme.colors.backgroundElevated)
            .border(1.dp, UniTTTheme.colors.borderDefault, shape)
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick))
            .padding(UniTTTheme.spacing.x16),
        verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8),
        content = content,
    )
}

@Composable
fun UniTTChip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    background: Color = if (selected) UniTTTheme.colors.brandPrimarySubtle else UniTTTheme.colors.backgroundSurface,
    contentColor: Color = if (selected) UniTTTheme.colors.brandPrimary else UniTTTheme.colors.textSecondary,
) {
    val shape = RoundedCornerShape(UniTTTheme.radius.pill)
    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(1.dp, if (selected) UniTTTheme.colors.brandBorder else UniTTTheme.colors.borderDefault, shape)
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick))
            .padding(horizontal = UniTTTheme.spacing.x12, vertical = UniTTTheme.spacing.x8),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = UniTTTheme.typography.labelMedium, color = contentColor)
    }
}

@Composable
fun UniTTEmptyState(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.NotificationsNone,
    iconDescription: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = UniTTTheme.spacing.x40),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x10),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(UniTTTheme.colors.brandPrimarySubtle),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                tint = UniTTTheme.colors.brandPrimary,
                modifier = Modifier.size(30.dp),
            )
        }
        Text(title, style = UniTTTheme.typography.heading3, color = UniTTTheme.colors.textPrimary)
        Text(body, style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
    }
}

@Composable
fun UniTTRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailing: String? = null,
    onClick: (() -> Unit)? = null,
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    leadingIconDescription: String? = null,
    trailingIcon: ImageVector? = null,
    trailingIconDescription: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(UniTTTheme.radius.lg))
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick))
            .padding(vertical = UniTTTheme.spacing.x12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12),
    ) {
        if (checked != null && onCheckedChange != null) {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        }
        if (leadingIcon != null) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(UniTTTheme.colors.backgroundSurface),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = leadingIconDescription,
                    tint = UniTTTheme.colors.brandPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x2)) {
            Text(title, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
            if (subtitle != null) {
                Text(subtitle, style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
            }
        }
        if (trailing != null) {
            Text(trailing, style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.textTertiary)
        }
        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = trailingIconDescription,
                tint = UniTTTheme.colors.textTertiary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
fun UniTTSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    leadingIconDescription: String? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = UniTTTheme.spacing.x8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12),
    ) {
        if (leadingIcon != null) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(UniTTTheme.colors.backgroundSurface),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = leadingIconDescription,
                    tint = UniTTTheme.colors.brandPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
            if (subtitle != null) {
                Text(subtitle, style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun UniTTAvatar(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes image: Int? = null,
) {
    Surface(
        modifier = modifier.size(UniTTTheme.sizes.avatarXL),
        shape = CircleShape,
        color = UniTTTheme.colors.brandPrimarySubtle,
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (image == null) {
                Text(text, style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.brandPrimary)
            } else {
                Image(painterResource(image), contentDescription = null, modifier = Modifier.size(UniTTTheme.sizes.avatarXL))
            }
        }
    }
}
