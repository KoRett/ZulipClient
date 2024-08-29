package com.korett.zulip_client.core.common.extension


fun <T> lazyUnsafe(block: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, block)