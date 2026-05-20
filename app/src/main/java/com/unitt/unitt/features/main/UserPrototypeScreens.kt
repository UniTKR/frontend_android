package com.unitt.unitt.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unitt.unitt.R
import com.unitt.unitt.core.model.ListingCreateCategory
import com.unitt.unitt.core.model.ListingStatus
import com.unitt.unitt.core.model.MockChat
import com.unitt.unitt.core.model.MockData
import com.unitt.unitt.core.model.MockListing
import com.unitt.unitt.core.model.MockNotification
import com.unitt.unitt.core.model.NotificationTab
import com.unitt.unitt.core.model.ReportTarget
import com.unitt.unitt.core.model.UserTab
import com.unitt.unitt.designsystem.UniTTAvatar
import com.unitt.unitt.designsystem.UniTTBottomActionBar
import com.unitt.unitt.designsystem.UniTTBottomTabs
import com.unitt.unitt.designsystem.UniTTCard
import com.unitt.unitt.designsystem.UniTTChip
import com.unitt.unitt.designsystem.UniTTEmptyState
import com.unitt.unitt.designsystem.UniTTIconButton
import com.unitt.unitt.designsystem.UniTTPrimaryButton
import com.unitt.unitt.designsystem.UniTTScreenScaffold
import com.unitt.unitt.designsystem.UniTTSecondaryButton
import com.unitt.unitt.designsystem.UniTTSwitchRow
import com.unitt.unitt.designsystem.UniTTTabItem
import com.unitt.unitt.designsystem.UniTTTextField
import com.unitt.unitt.designsystem.UniTTTheme
import com.unitt.unitt.designsystem.UniTTTopBar
import com.unitt.unitt.designsystem.UniTTRow
import com.unitt.unitt.designsystem.UniTTWordmark
import kotlinx.coroutines.delay

@Composable
fun UserPrototypeApp(viewModel: UserPrototypeViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Box(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage)) {
        when (state.route) {
            PrototypeRoute.Main -> MainTabs(state, viewModel)
            PrototypeRoute.ProductDetail -> ProductDetailScreen(state, viewModel)
            PrototypeRoute.Report -> ReportFlowScreen(state, viewModel)
            PrototypeRoute.Notifications -> NotificationsScreen(state, viewModel)
            PrototypeRoute.BlockList -> BlockListScreen(state, viewModel)
            PrototypeRoute.Settings -> SettingsScreen(state, viewModel)
            PrototypeRoute.History -> HistoryScreen(viewModel)
            PrototypeRoute.ChatRoom -> ChatRoomScreen(state, viewModel)
            PrototypeRoute.TradePanel -> TradePanelScreen(state, viewModel)
            PrototypeRoute.Review -> ReviewScreen(state, viewModel)
            PrototypeRoute.Withdraw -> WithdrawScreen(viewModel)
        }
    }
}

@Composable
private fun MainTabs(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    UniTTScreenScaffold(
        bottomBar = { BottomTabs(state.activeTab, viewModel::openTab) },
    ) {
        Box(Modifier.fillMaxSize()) {
            when (state.activeTab) {
                UserTab.Home -> HomeScreen(state, viewModel)
                UserTab.Search -> SearchScreen(state, viewModel)
                UserTab.Create -> ListingCreateScreen(state, viewModel)
                UserTab.Chat -> ChatListScreen(viewModel)
                UserTab.My -> MyPageScreen(viewModel)
            }
        }
    }
}

@Composable
private fun HomeScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("user-home-screen"),
        contentPadding = PaddingValues(UniTTTheme.spacing.x16),
        verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16),
    ) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                UniTTWordmark(compact = true, modifier = Modifier.weight(1f))
                UniTTIconButton(
                    icon = Icons.Outlined.NotificationsNone,
                    contentDescription = stringResource(R.string.notifications),
                    onClick = viewModel::openNotifications,
                    tint = UniTTTheme.colors.brandPrimary,
                    testTag = "home-notifications-button",
                )
            }
        }
        item {
            Text(stringResource(R.string.home_title), style = UniTTTheme.typography.displayMedium, color = UniTTTheme.colors.textPrimary)
        }
        item {
            Text(stringResource(R.string.home_subtitle), style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
        }
        item {
            UniTTSecondaryButton(
                text = stringResource(R.string.search),
                onClick = { viewModel.openSearch() },
                modifier = Modifier.testTag("home-search-button"),
                leadingIcon = Icons.Outlined.Search,
            )
        }
        item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
                MockData.categories.forEach { category ->
                    UniTTChip(
                        text = category,
                        selected = state.selectedCategory == category,
                        onClick = { viewModel.updateSelectedCategory(category) },
                        modifier = Modifier.testTag(if (category == "교재") "category-textbook-button" else "category-$category"),
                    )
                }
            }
        }
        if (state.selectedCategory == "교재") {
            item {
                UniTTCard(modifier = Modifier.testTag("textbook-filter-row")) {
                    Text("이번 학기 교재 · ISBN 자동완성 · 같은 수업 학생 거래", style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
                }
            }
        }
        if (state.visibleListings.isEmpty()) {
            item {
                UniTTEmptyState(
                    title = stringResource(R.string.empty_feed_title),
                    body = stringResource(R.string.empty_feed_body),
                    modifier = Modifier.testTag("home-empty-feed"),
                )
            }
        } else {
            items(state.visibleListings, key = { it.id }) { listing ->
                ListingCard(
                    listing = listing,
                    onClick = { viewModel.openProductDetail(listing) },
                    modifier = Modifier.testTag("product-card-${listing.id}"),
                )
            }
        }
    }
}

@Composable
private fun SearchScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("search-screen"),
        contentPadding = PaddingValues(UniTTTheme.spacing.x16),
        verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16),
    ) {
        item {
            UniTTTextField(
                value = state.searchText,
                onValueChange = viewModel::updateSearchText,
                label = stringResource(R.string.search),
                placeholder = "찾고 싶은 물건을 검색해 보세요",
                modifier = Modifier.testTag("search-field"),
                leadingIcon = Icons.Outlined.Search,
                leadingIconDescription = stringResource(R.string.search),
            )
        }
        if (state.searchText.isBlank()) {
            item {
                Text("최근 검색", style = UniTTTheme.typography.labelMedium, color = UniTTTheme.colors.textSecondary)
            }
            items(MockData.recentSearches, key = { it }) { item ->
                UniTTRow(
                    title = item,
                    onClick = { viewModel.updateSearchText(item) },
                    modifier = Modifier.testTag(if (item == "에어팟") "recent-search-airpods" else "recent-search-$item"),
                )
            }
        } else if (state.searchResults.isEmpty()) {
            item {
                UniTTEmptyState(
                    title = "검색 결과가 없어요",
                    body = "다른 키워드로 다시 검색해 보세요.",
                    modifier = Modifier.testTag("search-empty-screen"),
                )
            }
        } else {
            items(state.searchResults, key = { it.id }) { listing ->
                ListingCard(
                    listing = listing,
                    onClick = { viewModel.openProductDetail(listing) },
                    modifier = Modifier.testTag("search-result-${listing.id}"),
                )
            }
        }
    }
}

@Composable
private fun ProductDetailScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    val listing = state.selectedListing ?: MockData.listings.first()
    UniTTScreenScaffold(
        modifier = Modifier.testTag("product-detail-screen"),
        topBar = {
            UniTTTopBar(
                title = stringResource(R.string.product_detail),
                onBack = viewModel::backToMain,
                rightText = stringResource(R.string.report),
                onRight = { viewModel.startReport() },
                rightIcon = Icons.Outlined.ErrorOutline,
                rightContentDescription = stringResource(R.string.report),
                rightTestTag = "top-right-button",
            )
        },
        bottomBar = {
            if (!listing.isMine) {
                UniTTBottomActionBar {
                    if (listing.status == ListingStatus.Reserved) {
                        UniTTPrimaryButton(
                            stringResource(R.string.join_waitlist),
                            onClick = {},
                            modifier = Modifier.weight(1f).testTag("join-waitlist-button"),
                            leadingIcon = Icons.Outlined.Schedule,
                        )
                    } else {
                        UniTTPrimaryButton(
                            stringResource(R.string.start_chat),
                            onClick = { viewModel.openTab(UserTab.Chat) },
                            modifier = Modifier.weight(1f).testTag("start-chat-button"),
                            leadingIcon = Icons.Outlined.ChatBubbleOutline,
                        )
                    }
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(UniTTTheme.spacing.x16),
            verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(UniTTTheme.spacing.x64 * 3)
                    .background(UniTTTheme.colors.backgroundCanvas),
                contentAlignment = Alignment.Center,
            ) {
                Text(listing.category, style = UniTTTheme.typography.heading2, color = UniTTTheme.colors.brandPrimary)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text(listing.title, style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
                    Text("${listing.spot} · ${listing.time}", style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
                }
                StatusChip(listing.status)
            }
            Text(listing.price, style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.brandPrimary)
            Text(listing.description, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
            UniTTCard {
                Text("판매자", style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.textTertiary)
                Text(listing.seller, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
                Text("학교 인증 완료 · 최근 응답 빠름", style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
            }
            if (listing.status == ListingStatus.Reserved) {
                UniTTCard(modifier = Modifier.testTag("product-reserved-banner")) {
                    Text("현재 예약 중인 상품입니다.", style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.stateWarningText)
                }
            }
            if (listing.isMine) {
                UniTTCard(modifier = Modifier.testTag("owner-product-actions")) {
                    Text("내가 올린 상품이에요.", style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
                    UniTTSecondaryButton("상태 관리", onClick = {})
                }
            }
        }
    }
}

@Composable
private fun ListingCreateScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    Column(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage).testTag("listing-create-screen")) {
        UniTTTopBar(title = stringResource(R.string.create_listing), applyStatusPadding = false)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(UniTTTheme.spacing.x16),
            verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16),
        ) {
            when (state.createStep) {
                CreateStep.Category -> CreateCategoryStep(state, viewModel)
                CreateStep.Info -> CreateInfoStep(state, viewModel)
                CreateStep.Specific -> CreateSpecificStep(state)
                CreateStep.Pickup -> CreatePickupStep(state, viewModel)
                CreateStep.Preview -> CreatePreviewStep(state)
                CreateStep.Done -> CreateDoneScreen(viewModel)
            }
        }
        if (state.createStep != CreateStep.Done) {
            UniTTBottomActionBar(applyNavigationPadding = false) {
                UniTTPrimaryButton(
                    text = if (state.createStep == CreateStep.Preview) stringResource(R.string.create_submit) else stringResource(R.string.create_next),
                    enabled = state.createStep != CreateStep.Info || state.canSubmitListing,
                    onClick = viewModel::nextCreateStep,
                    modifier = Modifier.weight(1f).testTag(if (state.createStep == CreateStep.Preview) "create-submit" else "create-next"),
                )
            }
        }
    }
}

@Composable
private fun CreateCategoryStep(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    Text("어떤 물건을 등록할까요?", style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
    Column(verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x10)) {
        ListingCreateCategory.entries.forEach { category ->
            val selected = state.selectedCreateCategory == category
            UniTTCard(
                onClick = { viewModel.selectCreateCategory(category) },
                modifier = Modifier.testTag(if (category == ListingCreateCategory.Textbook) "create-category-textbook" else "create-category-${category.label}"),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12),
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .testTag("create-category-icon-${category.name}")
                            .background(if (selected) UniTTTheme.colors.brandPrimarySubtle else UniTTTheme.colors.backgroundSurface),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = createCategoryIcon(category),
                            contentDescription = category.label,
                            tint = if (selected) UniTTTheme.colors.brandPrimary else UniTTTheme.colors.textSecondary,
                        )
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x2)) {
                        Text(category.label, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
                        Text(category.detailHint, style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
                    }
                    if (selected) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "선택됨",
                            tint = UniTTTheme.colors.brandPrimary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateInfoStep(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    UniTTTextField(state.createTitle, viewModel::updateCreateTitle, "제목")
    UniTTTextField(state.createPrice, viewModel::updateCreatePrice, "가격", keyboardType = KeyboardType.Number)
    UniTTTextField(state.createDescription, viewModel::updateCreateDescription, "설명", singleLine = false)
    UniTTSecondaryButton(
        text = "에러 상태 보기",
        onClick = viewModel::toggleCreateError,
        modifier = Modifier.testTag("create-error-toggle"),
    )
    if (state.showingCreateError) {
        UniTTCard(modifier = Modifier.testTag("create-error-panel")) {
            Text("필수 항목을 모두 입력해야 등록할 수 있어요.", color = UniTTTheme.colors.stateDangerText)
        }
    }
}

@Composable
private fun CreateSpecificStep(state: UserPrototypeUiState) {
    UniTTCard(modifier = Modifier.testTag("create-specific-step")) {
        Text("${state.selectedCreateCategory.label} 추가 정보", style = UniTTTheme.typography.heading3, color = UniTTTheme.colors.textPrimary)
        Text(state.selectedCreateCategory.detailHint, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
        if (state.selectedCreateCategory == ListingCreateCategory.Textbook) {
            UniTTRow("자료구조 이론서 9판", subtitle = "ISBN 자동완성", modifier = Modifier.testTag("isbn-autocomplete-row"))
        }
    }
}

@Composable
private fun CreatePickupStep(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    Text("거래 장소를 선택해 주세요", style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
    MockData.pickupSpots.forEach { spot ->
        UniTTRow(
            title = spot,
            trailing = if (state.createPickup == spot) "선택됨" else null,
            onClick = { viewModel.updateCreatePickup(spot) },
            leadingIcon = Icons.Outlined.Place,
        )
    }
    UniTTSecondaryButton("지도 보기", onClick = viewModel::togglePickupMap, leadingIcon = Icons.Outlined.Map)
    if (state.showingMapPickup) {
        Box(Modifier.fillMaxWidth().height(UniTTTheme.spacing.x64 * 2).background(UniTTTheme.colors.backgroundCanvas).testTag("pickup-map-view"), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
                Icon(Icons.Outlined.Map, contentDescription = null, tint = UniTTTheme.colors.brandPrimary)
                Text("캠퍼스 지도", color = UniTTTheme.colors.brandPrimary)
            }
        }
    }
}

@Composable
private fun CreatePreviewStep(state: UserPrototypeUiState) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
        Icon(Icons.Outlined.Visibility, contentDescription = null, tint = UniTTTheme.colors.brandPrimary)
        Text("미리보기", style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
    }
    ListingCard(
        listing = MockListing(
            id = "preview",
            title = state.createTitle,
            category = state.createCategory,
            price = "${state.createPrice}원",
            spot = state.createPickup,
            time = "방금",
            status = ListingStatus.Listed,
            description = state.createDescription,
            seller = "관악구학생",
            isMine = true,
        ),
        onClick = {},
    )
}

@Composable
private fun CreateDoneScreen(viewModel: UserPrototypeViewModel) {
    Column(Modifier.fillMaxWidth().testTag("create-done-screen"), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16)) {
        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = UniTTTheme.colors.brandPrimary, modifier = Modifier.size(48.dp))
        Text(stringResource(R.string.create_done_title), style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
        Text("학교 피드에 등록한 상품이 표시됩니다.", style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
        UniTTPrimaryButton(stringResource(R.string.go_home), onClick = viewModel::resetCreateFlow, modifier = Modifier.testTag("create-done-home"), leadingIcon = Icons.Outlined.Home)
    }
}

@Composable
private fun ChatListScreen(viewModel: UserPrototypeViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("chat-list-screen"),
        contentPadding = PaddingValues(UniTTTheme.spacing.x16),
        verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12),
    ) {
        item {
            Text(stringResource(R.string.chat), style = UniTTTheme.typography.displayMedium, color = UniTTTheme.colors.textPrimary)
        }
        items(MockData.chats, key = { it.id }) { chat ->
            ChatRow(chat, onClick = { viewModel.openChatRoom(chat) })
        }
    }
}

@Composable
private fun ChatRoomScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    val chat = state.selectedChat ?: MockData.chats.first()
    Column(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage).navigationBarsPadding().testTag("chat-room-screen")) {
        UniTTTopBar(
            title = chat.name,
            onBack = viewModel::backToMain,
            rightText = "메뉴",
            onRight = viewModel::toggleChatActions,
            rightIcon = Icons.Outlined.MoreVert,
            rightContentDescription = stringResource(R.string.chat_menu),
            rightTestTag = "top-right-button",
        )
        Column(Modifier.weight(1f).padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16)) {
            UniTTCard {
                Text(chat.listingTitle, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
                Text("오늘 18:00 · 정문에서 만나요", style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
            }
            MessageBubble("안녕하세요. 아직 구매 가능할까요?", false)
            MessageBubble("네 가능해요. 정문에서 거래 괜찮으세요?", true)
            MessageBubble(chat.lastMessage, false)
            if (state.showingChatActions) {
                UniTTCard(modifier = Modifier.testTag("chat-action-menu")) {
                    UniTTSecondaryButton(stringResource(R.string.suggest_appointment), onClick = viewModel::openAppointmentSheet, modifier = Modifier.testTag("appointment-suggest-button"), leadingIcon = Icons.Outlined.CalendarToday)
                    UniTTSecondaryButton(stringResource(R.string.block_user), onClick = viewModel::blockCurrentUser, modifier = Modifier.testTag("block-user-button"), leadingIcon = Icons.Outlined.Block)
                }
            }
            UniTTSecondaryButton(stringResource(R.string.suggest_appointment), onClick = viewModel::openAppointmentSheet, modifier = Modifier.testTag("appointment-menu-button"), leadingIcon = Icons.Outlined.CalendarToday)
            if (state.showingAppointmentSheet) {
                AppointmentSheet(viewModel)
            }
        }
    }
}

@Composable
private fun AppointmentSheet(viewModel: UserPrototypeViewModel) {
    UniTTCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
            Icon(Icons.Outlined.CalendarToday, contentDescription = null, tint = UniTTTheme.colors.brandPrimary)
            Text("약속 제안", style = UniTTTheme.typography.heading3, color = UniTTTheme.colors.textPrimary)
        }
        Text("오늘 18:00 · 정문", style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
        UniTTPrimaryButton(stringResource(R.string.send_appointment), onClick = viewModel::submitAppointment, modifier = Modifier.testTag("appointment-submit-button"), leadingIcon = Icons.Outlined.Schedule)
    }
}

@Composable
private fun TradePanelScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    UniTTScreenScaffold(
        modifier = Modifier.testTag("trade-panel-screen"),
        topBar = { UniTTTopBar(title = stringResource(R.string.trade_status), onBack = viewModel::backToMain) },
        bottomBar = {
            UniTTBottomActionBar {
                UniTTSecondaryButton(
                    stringResource(R.string.open_dispute),
                    onClick = viewModel::disputeTrade,
                    modifier = Modifier.weight(1f).testTag("trade-dispute-button"),
                    leadingIcon = Icons.Outlined.ErrorOutline,
                )
                UniTTPrimaryButton(
                    stringResource(R.string.complete_trade),
                    onClick = viewModel::completeTrade,
                    modifier = Modifier.weight(1f).testTag("trade-complete-button"),
                    leadingIcon = Icons.Outlined.CheckCircle,
                )
            }
        },
    ) {
        Column(Modifier.padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16)) {
            StatusChip(state.tradeStatus)
            Text("약속 시간과 장소를 확인한 뒤 거래 상태를 바꿔 주세요.", style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
        }
    }
}

@Composable
private fun ReviewScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    UniTTScreenScaffold(
        modifier = Modifier.testTag("review-screen"),
        topBar = { UniTTTopBar(title = stringResource(R.string.write_review), onBack = viewModel::backToMain) },
        bottomBar = {
            UniTTBottomActionBar {
                UniTTPrimaryButton(
                    stringResource(R.string.submit_review),
                    enabled = state.canSubmitReview,
                    onClick = viewModel::submitReview,
                    modifier = Modifier.weight(1f).testTag("review-submit-button"),
                    leadingIcon = Icons.Outlined.Star,
                )
            }
        },
    ) {
        Column(Modifier.padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16)) {
            Row(horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
                (1..5).forEach { star ->
                    TextButton(onClick = { viewModel.updateReviewRating(star) }, modifier = Modifier.testTag("review-star-$star")) {
                        Icon(
                            imageVector = if (state.reviewRating >= star) Icons.Outlined.Star else Icons.Outlined.StarBorder,
                            contentDescription = "$star 점",
                            tint = UniTTTheme.colors.brandPrimary,
                            modifier = Modifier.size(30.dp),
                        )
                    }
                }
            }
            UniTTTextField(state.reviewComment, viewModel::updateReviewComment, "후기", singleLine = false)
        }
    }
}

@Composable
private fun ReportFlowScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    Column(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage).navigationBarsPadding().testTag("report-flow-screen")) {
        UniTTTopBar(title = stringResource(R.string.report), onBack = viewModel::backToMain)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16)) {
            when (state.reportStep) {
                ReportStep.Target -> {
                    Text("무엇을 신고할까요?", style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
                    ReportTarget.entries.forEach { target ->
                        UniTTRow(
                            target.label,
                            trailing = if (state.reportTarget == target) "선택됨" else null,
                            onClick = { viewModel.updateReportTarget(target) },
                            leadingIcon = reportTargetIcon(target),
                        )
                    }
                    UniTTPrimaryButton(stringResource(R.string.next), onClick = viewModel::nextReportStep, modifier = Modifier.testTag("report-next"), leadingIcon = Icons.Outlined.CheckCircle)
                }
                ReportStep.Reason -> {
                    Text("신고 사유를 선택해 주세요", style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
                    MockData.reportReasons.forEach { reason ->
                        UniTTRow(
                            title = reason,
                            trailing = if (state.reportReason == reason) "선택됨" else null,
                            onClick = { viewModel.updateReportReason(reason) },
                            modifier = Modifier.testTag(if (reason == "노쇼/약속 불이행") "report-reason-noshow" else "report-reason-$reason"),
                            leadingIcon = Icons.Outlined.ErrorOutline,
                        )
                    }
                    UniTTPrimaryButton(stringResource(R.string.next), enabled = state.canContinueReport, onClick = viewModel::nextReportStep, modifier = Modifier.testTag("report-next"), leadingIcon = Icons.Outlined.CheckCircle)
                }
                ReportStep.Detail -> {
                    Text("상세 내용을 적어 주세요", style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
                    UniTTTextField(state.reportDetail, viewModel::updateReportDetail, "내용", singleLine = false)
                    UniTTPrimaryButton(stringResource(R.string.report), enabled = state.canContinueReport, onClick = viewModel::nextReportStep, modifier = Modifier.testTag("report-next"), leadingIcon = Icons.Outlined.ErrorOutline)
                }
                ReportStep.Done -> {
                    Column(Modifier.fillMaxWidth().testTag("report-done-screen"), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12)) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = UniTTTheme.colors.stateDanger, modifier = Modifier.size(48.dp))
                        Text(stringResource(R.string.report_done_title), style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
                        Text("운영팀이 검토한 뒤 필요한 조치를 진행합니다.", style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
                        UniTTPrimaryButton(stringResource(R.string.go_home), onClick = viewModel::resetToHome, leadingIcon = Icons.Outlined.Home)
                    }
                }
            }
        }
    }
}

@Composable
private fun BlockListScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    LaunchedEffect(state.showingBlockToast) {
        if (state.showingBlockToast) {
            delay(1600)
            viewModel.dismissBlockToast()
        }
    }
    Column(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage).navigationBarsPadding().testTag("block-list-screen")) {
        UniTTTopBar(title = stringResource(R.string.block_list), onBack = viewModel::backToMain)
        Column(Modifier.padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12)) {
            if (state.showingBlockToast) {
                UniTTCard(modifier = Modifier.testTag("block-toast")) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = UniTTTheme.colors.stateSuccessText)
                        Text("사용자를 차단했어요.", color = UniTTTheme.colors.stateSuccessText)
                    }
                }
            }
            MockData.blockedUsers.forEach { user -> UniTTRow(user, subtitle = "채팅과 거래 요청이 제한됩니다.", leadingIcon = Icons.Outlined.Block) }
        }
    }
}

@Composable
private fun NotificationsScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    Column(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage).navigationBarsPadding().testTag("notifications-screen")) {
        UniTTTopBar(
            title = stringResource(R.string.notifications),
            onBack = viewModel::backToMain,
            rightText = stringResource(R.string.settings),
            onRight = viewModel::openNotificationSettings,
            rightIcon = Icons.Outlined.Settings,
            rightContentDescription = stringResource(R.string.settings),
            rightTestTag = "top-settings-button",
        )
        Column(Modifier.padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12)) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x8)) {
                NotificationTab.entries.forEach { tab ->
                    UniTTChip(tab.label, selected = state.notificationTab == tab, onClick = { viewModel.selectNotificationTab(tab) })
                }
            }
            if (state.notificationTab == NotificationTab.System) {
                UniTTCard(modifier = Modifier.testTag("notification-settings-screen")) {
                    UniTTSwitchRow("푸시 알림", state.pushEnabled, viewModel::togglePushEnabled, subtitle = stringResource(R.string.notification_push_subtitle), leadingIcon = Icons.Outlined.Notifications)
                    UniTTSwitchRow("마케팅 알림", state.marketingEnabled, viewModel::toggleMarketingEnabled, subtitle = stringResource(R.string.notification_marketing_subtitle), leadingIcon = Icons.Outlined.NotificationsNone)
                }
            }
            if (state.visibleNotifications.isEmpty()) {
                UniTTEmptyState(
                    "알림이 없어요",
                    "새로운 거래나 채팅 알림이 여기에 표시됩니다.",
                    modifier = Modifier.testTag("notifications-empty-screen"),
                    icon = Icons.Outlined.NotificationsNone,
                    iconDescription = stringResource(R.string.notifications),
                )
            } else {
                state.visibleNotifications.forEach { notification ->
                    NotificationRow(notification)
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(notification: MockNotification) {
    UniTTCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (notification.tab) {
                            NotificationTab.Trade -> UniTTTheme.colors.chipReservedBg
                            NotificationTab.Chat -> UniTTTheme.colors.brandPrimarySubtle
                            NotificationTab.System -> UniTTTheme.colors.backgroundSurface
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = notificationIcon(notification.tab),
                    contentDescription = notification.tab.label,
                    tint = when (notification.tab) {
                        NotificationTab.Trade -> UniTTTheme.colors.chipReservedInk
                        NotificationTab.Chat -> UniTTTheme.colors.brandPrimary
                        NotificationTab.System -> UniTTTheme.colors.textSecondary
                    },
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x4)) {
                Text(notification.title, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
                Text(notification.body, style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
            }
            Text(notification.time, style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.textTertiary)
        }
    }
}

@Composable
private fun MyPageScreen(viewModel: UserPrototypeViewModel) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(UniTTTheme.spacing.x16).testTag("my-page-screen"), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            UniTTAvatar("관", modifier = Modifier.size(UniTTTheme.spacing.x56))
            Column(Modifier.weight(1f).padding(start = UniTTTheme.spacing.x12)) {
                Text("관악구학생", style = UniTTTheme.typography.heading2, color = UniTTTheme.colors.textPrimary)
                Text("서울대학교 인증 완료", style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
            }
            UniTTIconButton(
                icon = Icons.Outlined.Settings,
                contentDescription = stringResource(R.string.settings),
                onClick = viewModel::openSettings,
                tint = UniTTTheme.colors.brandPrimary,
                testTag = "my-settings-button",
            )
        }
        UniTTCard {
            Text("보존형 F", style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.textTertiary)
            Text("Hi-Fi 원본이 나오기 전까지 기존 기능을 유지합니다.", style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
        }
        UniTTRow("내 판매/구매 내역", subtitle = "완료, 취소, 분쟁 상태 확인", onClick = viewModel::openHistory, leadingIcon = Icons.Outlined.History)
        UniTTRow("알림 센터", onClick = viewModel::openNotifications, leadingIcon = Icons.Outlined.Notifications)
        UniTTRow("차단 목록", onClick = { viewModel.blockCurrentUser() }, leadingIcon = Icons.Outlined.Block)
    }
}

@Composable
private fun HistoryScreen(viewModel: UserPrototypeViewModel) {
    Column(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage).navigationBarsPadding().testTag("history-screen")) {
        UniTTTopBar(title = stringResource(R.string.history), onBack = viewModel::backToMain)
        Column(Modifier.verticalScroll(rememberScrollState()).padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12)) {
            MockData.listings.filter { it.isMine }.forEach { listing -> ListingCard(listing, onClick = {}) }
        }
    }
}

@Composable
private fun SettingsScreen(state: UserPrototypeUiState, viewModel: UserPrototypeViewModel) {
    Column(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage).navigationBarsPadding().testTag("settings-screen")) {
        UniTTTopBar(title = stringResource(R.string.settings), onBack = viewModel::backToMain)
        Column(Modifier.verticalScroll(rememberScrollState()).padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x12)) {
            UniTTSwitchRow("푸시 알림", state.pushEnabled, viewModel::togglePushEnabled, leadingIcon = Icons.Outlined.Notifications)
            UniTTSwitchRow("마케팅 알림", state.marketingEnabled, viewModel::toggleMarketingEnabled, leadingIcon = Icons.Outlined.NotificationsNone)
            UniTTRow(stringResource(R.string.withdraw), onClick = viewModel::openWithdraw, leadingIcon = Icons.Outlined.ErrorOutline)
            UniTTSecondaryButton(stringResource(R.string.logout), onClick = viewModel::requestLogout, modifier = Modifier.testTag("settings-logout-button"), leadingIcon = Icons.Outlined.Block)
        }
        if (state.showingLogoutDialog) {
            AlertDialog(
                onDismissRequest = viewModel::dismissLogout,
                title = { Text(stringResource(R.string.logout)) },
                text = { Text("현재 계정에서 로그아웃할까요?") },
                confirmButton = {
                    TextButton(onClick = viewModel::dismissLogout) { Text(stringResource(R.string.confirm)) }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::dismissLogout) { Text(stringResource(R.string.cancel)) }
                },
            )
        }
    }
}

@Composable
private fun WithdrawScreen(viewModel: UserPrototypeViewModel) {
    Column(Modifier.fillMaxSize().background(UniTTTheme.colors.backgroundPage).navigationBarsPadding().testTag("withdraw-screen")) {
        UniTTTopBar(title = stringResource(R.string.withdraw), onBack = viewModel::backToMain)
        Column(Modifier.padding(UniTTTheme.spacing.x16), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x16)) {
            Text("탈퇴 전 확인해 주세요", style = UniTTTheme.typography.heading1, color = UniTTTheme.colors.textPrimary)
            Text("거래 기록과 신고 처리 이력은 운영 정책에 따라 보관될 수 있습니다.", style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textSecondary)
            UniTTSecondaryButton(stringResource(R.string.cancel), onClick = viewModel::backToMain)
        }
    }
}

@Composable
private fun BottomTabs(activeTab: UserTab, onSelect: (UserTab) -> Unit) {
    UniTTBottomTabs(
        items = UserTab.entries.map { tab ->
            UniTTTabItem(
                label = tab.label,
                icon = userTabIcon(tab),
                contentDescription = tab.label,
            )
        },
        selectedLabel = activeTab.label,
        onSelect = { label -> UserTab.entries.firstOrNull { it.label == label }?.let(onSelect) },
    )
}

private fun userTabIcon(tab: UserTab): ImageVector = when (tab) {
    UserTab.Home -> Icons.Outlined.Home
    UserTab.Search -> Icons.Outlined.Search
    UserTab.Create -> Icons.Outlined.Add
    UserTab.Chat -> Icons.Outlined.ChatBubbleOutline
    UserTab.My -> Icons.Outlined.PersonOutline
}

private fun createCategoryIcon(category: ListingCreateCategory): ImageVector = when (category) {
    ListingCreateCategory.Textbook -> Icons.AutoMirrored.Outlined.MenuBook
    ListingCreateCategory.Electronics -> Icons.Outlined.Computer
    ListingCreateCategory.Living -> Icons.Outlined.HomeWork
    ListingCreateCategory.Moving -> Icons.Outlined.LocalShipping
}

private fun reportTargetIcon(target: ReportTarget): ImageVector = when (target) {
    ReportTarget.Listing -> Icons.Outlined.Book
    ReportTarget.User -> Icons.Outlined.Person
    ReportTarget.Chat -> Icons.Outlined.ChatBubbleOutline
    ReportTarget.Trade -> Icons.Outlined.ErrorOutline
}

private fun notificationIcon(tab: NotificationTab): ImageVector = when (tab) {
    NotificationTab.Trade -> Icons.Outlined.CalendarToday
    NotificationTab.Chat -> Icons.Outlined.ChatBubbleOutline
    NotificationTab.System -> Icons.Outlined.ErrorOutline
}

@Composable
private fun ListingCard(listing: MockListing, onClick: () -> Unit, modifier: Modifier = Modifier) {
    UniTTCard(modifier = modifier, onClick = onClick) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(UniTTTheme.spacing.x4)) {
                Text(listing.title, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
                Text("${listing.category} · ${listing.spot} · ${listing.time}", style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
                Text(listing.price, style = UniTTTheme.typography.heading3, color = UniTTTheme.colors.brandPrimary)
            }
            StatusChip(listing.status)
        }
    }
}

@Composable
private fun ChatRow(chat: MockChat, onClick: () -> Unit) {
    UniTTCard(modifier = Modifier.testTag("chat-row-${chat.id}"), onClick = onClick) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text(chat.name, style = UniTTTheme.typography.bodyMedium, color = UniTTTheme.colors.textPrimary)
                Text(chat.listingTitle, style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textTertiary)
                Text(chat.lastMessage, style = UniTTTheme.typography.bodySmall, color = UniTTTheme.colors.textSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(chat.time, style = UniTTTheme.typography.labelSmall, color = UniTTTheme.colors.textTertiary)
                if (chat.unreadCount > 0) UniTTChip(chat.unreadCount.toString(), background = UniTTTheme.colors.brandPrimary, contentColor = UniTTTheme.colors.textOnBrand)
            }
        }
    }
}

@Composable
private fun MessageBubble(text: String, mine: Boolean) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (mine) Arrangement.End else Arrangement.Start) {
        Box(
            modifier = Modifier
                .background(if (mine) UniTTTheme.colors.brandPrimary else UniTTTheme.colors.backgroundSurface)
                .padding(horizontal = UniTTTheme.spacing.x12, vertical = UniTTTheme.spacing.x8),
        ) {
            Text(text, style = UniTTTheme.typography.bodyMedium, color = if (mine) UniTTTheme.colors.textOnBrand else UniTTTheme.colors.textPrimary)
        }
    }
}

@Composable
private fun StatusChip(status: ListingStatus) {
    val (bg, ink) = statusColors(status)
    UniTTChip(text = status.label, background = bg, contentColor = ink)
}

@Composable
private fun statusColors(status: ListingStatus): Pair<Color, Color> = when (status) {
    ListingStatus.Listed -> UniTTTheme.colors.chipListedBg to UniTTTheme.colors.chipListedInk
    ListingStatus.Reserved -> UniTTTheme.colors.chipReservedBg to UniTTTheme.colors.chipReservedInk
    ListingStatus.Completed -> UniTTTheme.colors.chipCompletedBg to UniTTTheme.colors.chipCompletedInk
    ListingStatus.Canceled -> UniTTTheme.colors.chipCanceledBg to UniTTTheme.colors.chipCanceledInk
    ListingStatus.Disputed -> UniTTTheme.colors.chipDisputedBg to UniTTTheme.colors.chipDisputedInk
}
