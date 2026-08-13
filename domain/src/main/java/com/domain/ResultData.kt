package com.domain

import com.domain.models.CityItem

sealed interface CheckDataResult<out D, out E> {
    data class Success<D>(val data: D) : CheckDataResult<D, Nothing>
    data class Error(val error: AppError) : CheckDataResult<Nothing, AppError>
}

sealed interface LocationResult {
    data class Success(val city: CityItem) : LocationResult
    data object NoPermission : LocationResult
    data object GpsDisabled : LocationResult
    data object Error : LocationResult
}

enum class AppError {
    NO_INTERNET,
    TIMEOUT,
    SERVER_ERROR,
    NOT_FOUND,
    UNAUTHORIZED,
    UNKNOWN
}