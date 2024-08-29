package com.korett.zulip_client.core.common.extension

import kotlinx.coroutines.CancellationException

inline fun <T> runCatchingNonCancellation(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}