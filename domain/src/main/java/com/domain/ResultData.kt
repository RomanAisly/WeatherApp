package com.domain

sealed interface CheckDataResult<out D, out E> {
    data class Success<D>(val data: D) : CheckDataResult<D, Nothing>
    data class Error(val error: AppError) : CheckDataResult<Nothing, AppError>
}

enum class AppError {
    NO_INTERNET,
    TIMEOUT,
    SERVER_ERROR,
    NOT_FOUND,
    UNAUTHORIZED,
    UNKNOWN
}