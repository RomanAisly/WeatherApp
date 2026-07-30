package com.domain

enum class AppTheme {
    SYSTEM,
    DARK,
    LIGHT
}

enum class AppLanguage(val localeCode: String) {
    ENGLISH("en-US"),
    RUSSIAN("ru-RU"),
    CHINESE("zh-CN"),
    SPANISH("es-ES"),
    FRENCH("fr-FR"),
    GERMAN("de-DE"),
    UKRAINIAN("uk-UA")
}