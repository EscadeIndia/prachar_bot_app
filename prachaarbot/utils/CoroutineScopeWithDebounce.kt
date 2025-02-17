package com.prachaarbot.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun CoroutineScope.launchDebounce(
    delayMillis: Long = 2000L,
    block: suspend () -> Unit
): Job {
    var debounceJob: Job? = null
    debounceJob?.cancel()
    debounceJob = launch {
        delay(delayMillis)
        block()
    }
    return debounceJob
}