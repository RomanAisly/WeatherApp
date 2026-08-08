package com.domain

enum class AppTheme {
    SYSTEM,
    DARK,
    LIGHT
}

enum class AppLanguage(val localeCode: String) {
    ENGLISH("en"),
    RUSSIAN("ru"),
    CHINESE("zh"),
    SPANISH("es"),
    FRENCH("fr"),
    GERMAN("de"),
    UKRAINIAN("uk"),
    JAPANESE("ja"),
    KOREAN("ko"),
    HINDI("hi"),
    ITALIAN("it")
}
