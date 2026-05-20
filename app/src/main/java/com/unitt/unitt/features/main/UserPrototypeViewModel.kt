package com.unitt.unitt.features.main

import androidx.lifecycle.ViewModel
import com.unitt.unitt.core.model.ListingCreateCategory
import com.unitt.unitt.core.model.ListingStatus
import com.unitt.unitt.core.model.MockChat
import com.unitt.unitt.core.model.MockData
import com.unitt.unitt.core.model.MockListing
import com.unitt.unitt.core.model.MockNotification
import com.unitt.unitt.core.model.NotificationTab
import com.unitt.unitt.core.model.ReportTarget
import com.unitt.unitt.core.model.UserTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class PrototypeRoute {
    Main,
    ProductDetail,
    Report,
    Notifications,
    BlockList,
    Settings,
    History,
    ChatRoom,
    TradePanel,
    Review,
    Withdraw,
}

enum class CreateStep {
    Category,
    Info,
    Specific,
    Pickup,
    Preview,
    Done,
}

enum class ReportStep {
    Target,
    Reason,
    Detail,
    Done,
}

data class UserPrototypeUiState(
    val activeTab: UserTab = UserTab.Home,
    val route: PrototypeRoute = PrototypeRoute.Main,
    val selectedListingId: String? = null,
    val selectedChatId: String? = null,
    val selectedCategory: String = "전체",
    val searchText: String = "",
    val createStep: CreateStep = CreateStep.Category,
    val createCategory: String = ListingCreateCategory.Textbook.label,
    val createTitle: String = "자료구조 이론서 9판",
    val createPrice: String = "8000",
    val createDescription: String = "필기 거의 없고 깨끗해요. 학생회관에서 거래 가능합니다.",
    val createCondition: String = "상",
    val createPickup: String = "학생회관",
    val reportStep: ReportStep = ReportStep.Target,
    val reportTarget: ReportTarget = ReportTarget.Listing,
    val reportReason: String = "",
    val reportDetail: String = "거래 약속 후 반복적으로 연락을 받지 못했습니다.",
    val tradeStatus: ListingStatus = ListingStatus.Reserved,
    val reviewRating: Int = 0,
    val reviewComment: String = "약속 시간을 잘 지켜주셔서 거래가 깔끔했습니다.",
    val showingAppointmentSheet: Boolean = false,
    val showingHistoryActions: Boolean = false,
    val showingLogoutDialog: Boolean = false,
    val showingEmptyFeed: Boolean = false,
    val showingCreateError: Boolean = false,
    val showingMapPickup: Boolean = false,
    val showingChatActions: Boolean = false,
    val showingBlockToast: Boolean = false,
    val notificationTab: NotificationTab = NotificationTab.Trade,
    val pushEnabled: Boolean = true,
    val marketingEnabled: Boolean = false,
) {
    val selectedListing: MockListing?
        get() = selectedListingId?.let { id -> MockData.listings.firstOrNull { it.id == id } }

    val selectedChat: MockChat?
        get() = selectedChatId?.let { id -> MockData.chats.firstOrNull { it.id == id } }

    val visibleListings: List<MockListing>
        get() = if (showingEmptyFeed) {
            emptyList()
        } else {
            MockData.listings.filter { selectedCategory == "전체" || it.category == selectedCategory }
        }

    val searchResults: List<MockListing>
        get() {
            val query = searchText.trim()
            return if (query.isEmpty()) {
                emptyList()
            } else {
                MockData.listings.filter {
                    it.title.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
                }
            }
        }

    val canSubmitListing: Boolean
        get() = !showingCreateError &&
            createCategory.isNotEmpty() &&
            createTitle.trim().isNotEmpty() &&
            createPrice.trim().isNotEmpty() &&
            createDescription.trim().isNotEmpty() &&
            createPickup.isNotEmpty()

    val canContinueReport: Boolean
        get() = when (reportStep) {
            ReportStep.Target -> true
            ReportStep.Reason -> reportReason.isNotEmpty()
            ReportStep.Detail -> reportDetail.trim().length >= 10
            ReportStep.Done -> false
        }

    val canSubmitReview: Boolean
        get() = reviewRating > 0

    val selectedCreateCategory: ListingCreateCategory
        get() = ListingCreateCategory.entries.firstOrNull { it.label == createCategory } ?: ListingCreateCategory.Textbook

    val visibleNotifications: List<MockNotification>
        get() = MockData.notifications.filter { it.tab == notificationTab }
}

class UserPrototypeViewModel : ViewModel() {
    private val _state = MutableStateFlow(UserPrototypeUiState())
    val state: StateFlow<UserPrototypeUiState> = _state.asStateFlow()

    fun resetToHome() {
        _state.update { it.copy(route = PrototypeRoute.Main, activeTab = UserTab.Home, showingEmptyFeed = false) }
    }

    fun openTab(tab: UserTab) {
        _state.update { it.copy(route = PrototypeRoute.Main, activeTab = tab) }
    }

    fun updateSelectedCategory(category: String) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchText(value: String) {
        _state.update { it.copy(searchText = value) }
    }

    fun openSearch(query: String = "") {
        _state.update { it.copy(route = PrototypeRoute.Main, activeTab = UserTab.Search, searchText = query) }
    }

    fun openProductDetail(listing: MockListing) {
        _state.update { it.copy(route = PrototypeRoute.ProductDetail, selectedListingId = listing.id) }
    }

    fun openChatRoom(chat: MockChat) {
        _state.update { it.copy(route = PrototypeRoute.ChatRoom, selectedChatId = chat.id) }
    }

    fun backToMain() {
        _state.update { it.copy(route = PrototypeRoute.Main, showingChatActions = false, showingAppointmentSheet = false) }
    }

    fun selectCreateCategory(category: ListingCreateCategory) {
        _state.update { it.copy(createCategory = category.label) }
    }

    fun updateCreateTitle(value: String) {
        _state.update { it.copy(createTitle = value) }
    }

    fun updateCreatePrice(value: String) {
        _state.update { it.copy(createPrice = value) }
    }

    fun updateCreateDescription(value: String) {
        _state.update { it.copy(createDescription = value) }
    }

    fun updateCreatePickup(value: String) {
        _state.update { it.copy(createPickup = value) }
    }

    fun toggleCreateError() {
        _state.update { it.copy(showingCreateError = !it.showingCreateError) }
    }

    fun togglePickupMap() {
        _state.update { it.copy(showingMapPickup = !it.showingMapPickup) }
    }

    fun nextCreateStep() {
        _state.update { state ->
            if (state.createStep == CreateStep.Done) {
                state
            } else {
                val next = CreateStep.entries[state.createStep.ordinal + 1]
                state.copy(createStep = next)
            }
        }
    }

    fun resetCreateFlow() {
        _state.update {
            it.copy(
                createStep = CreateStep.Category,
                showingCreateError = false,
                showingMapPickup = false,
                activeTab = UserTab.Home,
                route = PrototypeRoute.Main,
            )
        }
    }

    fun startReport(target: ReportTarget = ReportTarget.Listing) {
        _state.update {
            it.copy(
                route = PrototypeRoute.Report,
                reportStep = ReportStep.Target,
                reportTarget = target,
                reportReason = "",
            )
        }
    }

    fun updateReportTarget(target: ReportTarget) {
        _state.update { it.copy(reportTarget = target) }
    }

    fun updateReportReason(reason: String) {
        _state.update { it.copy(reportReason = reason) }
    }

    fun updateReportDetail(value: String) {
        _state.update { it.copy(reportDetail = value) }
    }

    fun nextReportStep() {
        _state.update { state ->
            val next = when {
                state.reportStep == ReportStep.Target -> ReportStep.Reason
                state.reportStep == ReportStep.Reason && state.canContinueReport -> ReportStep.Detail
                state.reportStep == ReportStep.Detail && state.canContinueReport -> ReportStep.Done
                else -> state.reportStep
            }
            state.copy(reportStep = next)
        }
    }

    fun openNotifications() {
        _state.update { it.copy(route = PrototypeRoute.Notifications) }
    }

    fun selectNotificationTab(tab: NotificationTab) {
        _state.update { it.copy(notificationTab = tab) }
    }

    fun openNotificationSettings() {
        _state.update { it.copy(route = PrototypeRoute.Notifications, notificationTab = NotificationTab.System) }
    }

    fun togglePushEnabled(value: Boolean) {
        _state.update { it.copy(pushEnabled = value) }
    }

    fun toggleMarketingEnabled(value: Boolean) {
        _state.update { it.copy(marketingEnabled = value) }
    }

    fun toggleChatActions() {
        _state.update { it.copy(showingChatActions = !it.showingChatActions) }
    }

    fun openAppointmentSheet() {
        _state.update { it.copy(showingAppointmentSheet = true, showingChatActions = false) }
    }

    fun submitAppointment() {
        _state.update { it.copy(showingAppointmentSheet = false, route = PrototypeRoute.TradePanel) }
    }

    fun completeTrade() {
        _state.update { it.copy(tradeStatus = ListingStatus.Completed, route = PrototypeRoute.Review) }
    }

    fun disputeTrade() {
        _state.update {
            it.copy(
                tradeStatus = ListingStatus.Disputed,
                route = PrototypeRoute.Report,
                reportStep = ReportStep.Target,
                reportTarget = ReportTarget.Trade,
                reportReason = "",
            )
        }
    }

    fun updateReviewRating(rating: Int) {
        _state.update { it.copy(reviewRating = rating) }
    }

    fun updateReviewComment(value: String) {
        _state.update { it.copy(reviewComment = value) }
    }

    fun submitReview() {
        _state.update { if (it.canSubmitReview) it.copy(route = PrototypeRoute.Main, activeTab = UserTab.My) else it }
    }

    fun blockCurrentUser() {
        _state.update { it.copy(showingBlockToast = true, route = PrototypeRoute.BlockList, showingChatActions = false) }
    }

    fun dismissBlockToast() {
        _state.update { it.copy(showingBlockToast = false) }
    }

    fun openHistory() {
        _state.update { it.copy(route = PrototypeRoute.History) }
    }

    fun openSettings() {
        _state.update { it.copy(route = PrototypeRoute.Settings) }
    }

    fun openWithdraw() {
        _state.update { it.copy(route = PrototypeRoute.Withdraw) }
    }

    fun requestLogout() {
        _state.update { it.copy(showingLogoutDialog = true) }
    }

    fun dismissLogout() {
        _state.update { it.copy(showingLogoutDialog = false) }
    }
}
