package com.study.infotechforprocstatidataofwebres.models

import retrofit2.Response

data class ReceiveDataButtonCondition<T> (
    val request: suspend () -> Response<T>?,
    val condition: Boolean,
    val onFalse: () -> Unit
)