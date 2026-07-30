package com.ui.components

import com.domain.AppTheme
import com.weatherapp.ui.R

//fun AppError.getIconRes(): Int = when (this) {
//    AppError.NO_INTERNET -> R.drawable.signal_disconnected
//    AppError.TIMEOUT -> R.drawable.hourglass_disabled
//    AppError.SERVER_ERROR -> R.drawable.cloud_off
//    AppError.NOT_FOUND -> R.drawable.search_off
//    AppError.UNAUTHORIZED -> R.drawable.no_encryption
//    AppError.UNKNOWN -> R.drawable.error
//}
//
//fun AppError.getMessageRes(): Int = when (this) {
//    AppError.NO_INTERNET -> R.string.error_no_internet
//    AppError.TIMEOUT -> R.string.error_timeout
//    AppError.SERVER_ERROR -> R.string.error_server
//    AppError.NOT_FOUND -> R.string.error_not_found
//    AppError.UNAUTHORIZED -> R.string.error_unauthorized
//    AppError.UNKNOWN -> R.string.error_unknown
//}

fun AppTheme.getTitleRes(): Int = when (this) {
    AppTheme.SYSTEM -> R.string.theme_system
    AppTheme.DARK -> R.string.theme_dark
    AppTheme.LIGHT -> R.string.theme_light
}

fun AppTheme.getIconRes(): Int = when (this) {
    AppTheme.SYSTEM -> R.drawable.theme_system
    AppTheme.DARK -> R.drawable.theme_dark
    AppTheme.LIGHT -> R.drawable.theme_light
}

//fun AppLanguage.getTitleRes(): Int = when (this) {
//    AppLanguage.ENGLISH -> R.string.en
//    AppLanguage.RUSSIAN -> R.string.ru
//    AppLanguage.CHINESE -> R.string.zh
//    AppLanguage.SPANISH -> R.string.es
//    AppLanguage.FRENCH -> R.string.fr
//    AppLanguage.GERMAN -> R.string.de
//    AppLanguage.UKRAINIAN -> R.string.uk
//}