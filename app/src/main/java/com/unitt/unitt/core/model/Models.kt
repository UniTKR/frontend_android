package com.unitt.unitt.core.model

enum class AuthRoute {
    Splash,
    Login,
    ForgotEmail,
    ForgotCode,
    ResetPassword,
    Signup,
}

enum class OnboardingStep(val progress: Int) {
    School(1),
    Email(2),
    Code(3),
    Password(4),
    Terms(5),
    Profile(6),
    Completed(6),
}

data class PasswordRule(
    val title: String,
    val satisfied: Boolean,
)

data class University(
    val id: String,
    val name: String,
    val district: String,
    val domain: String,
) {
    companion object {
        val popular = listOf(
            University("snu", "서울대학교", "관악구", "snu.ac.kr"),
            University("yonsei", "연세대학교", "서대문구", "yonsei.ac.kr"),
            University("korea", "고려대학교", "성북구", "korea.ac.kr"),
            University("skku", "성균관대학교", "종로구", "skku.edu"),
            University("hanyang", "한양대학교", "성동구", "hanyang.ac.kr"),
            University("cau", "중앙대학교", "동작구", "cau.ac.kr"),
            University("khu", "경희대학교", "동대문구", "khu.ac.kr"),
        )
    }
}

data class TermAgreement(
    val id: String,
    val title: String,
    val required: Boolean,
    val hasDetail: Boolean,
) {
    companion object {
        val all = listOf(
            TermAgreement("age", "만 14세 이상입니다", true, false),
            TermAgreement("service", "서비스 이용약관", true, true),
            TermAgreement("privacy", "개인정보 수집 및 이용", true, true),
            TermAgreement("location", "위치기반 서비스 약관", true, true),
            TermAgreement("marketing", "마케팅 정보 수신", false, true),
            TermAgreement("night", "야간 알림 수신", false, true),
        )

        val requiredIds = all.filter { it.required }.map { it.id }.toSet()
    }
}

enum class UserTab(val label: String) {
    Home("홈"),
    Search("검색"),
    Create("등록"),
    Chat("채팅"),
    My("마이"),
}

enum class ListingStatus(val label: String) {
    Listed("거래중"),
    Reserved("예약중"),
    Completed("거래완료"),
    Canceled("취소됨"),
    Disputed("분쟁중"),
}

data class MockListing(
    val id: String,
    val title: String,
    val category: String,
    val price: String,
    val spot: String,
    val time: String,
    val status: ListingStatus,
    val description: String,
    val seller: String,
    val isMine: Boolean,
)

data class MockChat(
    val id: String,
    val name: String,
    val listingTitle: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
)

enum class ListingCreateCategory(val label: String, val detailHint: String) {
    Textbook("교재", "ISBN, 과목코드, 학기"),
    Electronics("전자기기", "모델명, 보증, 이상 여부"),
    Living("자취·기숙사", "직접 픽업, 크기"),
    Moving("이사·나눔", "마감일, 일괄 처리, 무료 나눔"),
}

enum class ReportTarget(val label: String) {
    Listing("상품"),
    User("사용자"),
    Chat("채팅방"),
    Trade("거래"),
}

enum class NotificationTab(val label: String) {
    Trade("거래"),
    Chat("채팅"),
    System("시스템"),
}

data class MockNotification(
    val id: String,
    val title: String,
    val body: String,
    val tab: NotificationTab,
    val time: String,
)

object MockData {
    val categories = listOf("전체", "교재", "전자기기", "자취·기숙사", "이사·나눔")
    val reportReasons = listOf("사기 의심", "노쇼/약속 불이행", "금지 물품", "욕설/괴롭힘", "기타")
    val recentSearches = listOf("에어팟", "자료구조", "미니냉장고")
    val pickupSpots = listOf("학생회관", "정문", "중앙도서관", "공대 301동", "후문 GS25")

    val listings = listOf(
        MockListing(
            id = "data-structure",
            title = "자료구조 이론서 9판",
            category = "교재",
            price = "8,000원",
            spot = "학생회관",
            time = "2분 전",
            status = ListingStatus.Listed,
            description = "중간고사 전까지 사용했고 필기는 거의 없어요. 학생회관 1층에서 거래 가능해요.",
            seller = "관악구학생",
            isMine = false,
        ),
        MockListing(
            id = "airpods",
            title = "에어팟 4세대 USB-C",
            category = "전자기기",
            price = "130,000원",
            spot = "정문",
            time = "12분 전",
            status = ListingStatus.Listed,
            description = "구성품 모두 있고 케이스 생활기스만 조금 있어요.",
            seller = "공대박학생",
            isMine = false,
        ),
        MockListing(
            id = "fridge",
            title = "미니냉장고",
            category = "자취·기숙사",
            price = "50,000원",
            spot = "후문 GS25",
            time = "1시간 전",
            status = ListingStatus.Reserved,
            description = "소음 적고 냉장 잘 됩니다. 직접 픽업만 가능해요.",
            seller = "후문정리중",
            isMine = false,
        ),
        MockListing(
            id = "physics",
            title = "일반물리학 13판",
            category = "교재",
            price = "12,000원",
            spot = "도서관 입구",
            time = "3시간 전",
            status = ListingStatus.Listed,
            description = "표지 모서리 사용감 있고 내부는 깨끗합니다.",
            seller = "자연대학생",
            isMine = false,
        ),
        MockListing(
            id = "chair",
            title = "책상 의자",
            category = "이사·나눔",
            price = "무료 나눔",
            spot = "27동 1층",
            time = "5시간 전",
            status = ListingStatus.Listed,
            description = "사용감 있지만 튼튼합니다. 오늘 픽업 가능해요.",
            seller = "27동이사",
            isMine = true,
        ),
        MockListing(
            id = "monitor",
            title = "LG 모니터 24인치",
            category = "전자기기",
            price = "80,000원",
            spot = "공대",
            time = "어제",
            status = ListingStatus.Completed,
            description = "거래 완료된 상품입니다.",
            seller = "관악구학생",
            isMine = true,
        ),
        MockListing(
            id = "calculator",
            title = "공학용 계산기 FX-570ES",
            category = "전자기기",
            price = "18,000원",
            spot = "중앙도서관",
            time = "어제",
            status = ListingStatus.Canceled,
            description = "거래가 취소된 상품입니다.",
            seller = "관악구학생",
            isMine = true,
        ),
    )

    val chats = listOf(
        MockChat(
            id = "chat-airpods",
            name = "공대박학생",
            listingTitle = "에어팟 4세대 USB-C",
            lastMessage = "그럼 정문에서 6시에 뵐게요",
            time = "방금",
            unreadCount = 2,
        ),
        MockChat(
            id = "chat-fridge",
            name = "후문정리중",
            listingTitle = "미니냉장고",
            lastMessage = "픽업은 오늘 저녁 가능합니다.",
            time = "12분 전",
            unreadCount = 0,
        ),
    )

    val notifications = listOf(
        MockNotification(
            id = "trade",
            title = "거래 약속이 확정됐어요",
            body = "에어팟 4세대 · 정문 · 오늘 18:00",
            tab = NotificationTab.Trade,
            time = "방금",
        ),
        MockNotification(
            id = "chat",
            title = "새 채팅 메시지",
            body = "공대박학생: 그럼 정문에서 6시에 뵐게요",
            tab = NotificationTab.Chat,
            time = "2분 전",
        ),
        MockNotification(
            id = "system",
            title = "신고 접수 안내",
            body = "접수하신 신고는 운영팀 검토 중입니다.",
            tab = NotificationTab.System,
            time = "어제",
        ),
    )

    val blockedUsers = listOf("노쇼상습러", "스팸계정12", "허위거래유도", "비매너거래러")
}
