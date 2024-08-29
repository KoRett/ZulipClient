package com.korett.zulip_client.core.data.remote_source

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithHeader = chain.request()
            .newBuilder()
            .addHeader("Authorization", Credentials.basic(USERNAME, PASSWORD))
            .build()
        return chain.proceed(requestWithHeader)
    }

    companion object {
        private const val USERNAME = "kashencevrostik@gmail.com"
        private const val PASSWORD = "1K4I7uYa6orrpfGlDeClfHdmGagkM0il"
    }
}