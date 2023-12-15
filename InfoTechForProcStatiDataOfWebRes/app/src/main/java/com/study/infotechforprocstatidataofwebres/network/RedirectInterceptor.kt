package com.study.infotechforprocstatidataofwebres.network

import okhttp3.Interceptor
import okhttp3.Response

class RedirectInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response = chain.proceed(chain.request())
        if (response.code == 307) {
            request = request.newBuilder()
                .url(response.header("Location")!!)
                .build()
            response = chain.proceed(request)
        }
        return response
    }
}