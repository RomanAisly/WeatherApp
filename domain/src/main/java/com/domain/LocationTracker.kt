package com.domain

interface LocationTracker {
    suspend fun getCurrentLocation(): LocationResult
}