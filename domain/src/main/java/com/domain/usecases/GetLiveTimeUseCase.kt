package com.domain.usecases

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds

class GetLiveTimeUseCase {

    @OptIn(kotlin.time.ExperimentalTime::class)
    operator fun invoke(timezone: String): Flow<String> = flow {
        if (timezone.isBlank()) {
            emit("--:--")
            return@flow
        }
        val timeZone = TimeZone.of(timezone)

        while (true) {
            val now = kotlin.time.Clock.System.now()
            val localTime = now.toLocalDateTime(timeZone)
            val formattedTime = "${localTime.hour.toString().padStart(2, '0')}:${
                localTime.minute.toString().padStart(2, '0')
            }"
            emit(formattedTime)
            val secondsToNextMinute = 60 - localTime.second
            delay((secondsToNextMinute * 1000L).milliseconds)
        }
    }
}