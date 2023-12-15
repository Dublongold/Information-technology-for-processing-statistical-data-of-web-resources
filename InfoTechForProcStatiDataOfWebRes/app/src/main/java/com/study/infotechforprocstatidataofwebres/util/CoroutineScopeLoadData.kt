package com.study.infotechforprocstatidataofwebres.util

import android.util.Log
import com.study.infotechforprocstatidataofwebres.models.DataReceivingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

fun<T, V> CoroutineScope.loadData(
    setDataReceivingState: (DataReceivingState<V>) -> Unit,
    badResponse: String,
    response: suspend () -> Response<T>?,
    onSuccessful: (T) -> V
) = launch {
    setDataReceivingState(DataReceivingState.Loading())
    val responseResult = response()
    if (responseResult != null) {
        if (responseResult.isSuccessful) {
            setDataReceivingState(
                DataReceivingState.Finish(
                    onSuccessful(responseResult.body()!!)
                )
            )
        } else {
            val message = responseResult.errorBody()?.string() ?: responseResult.message()
            val code = responseResult.code()
            val changeBadResponse = if (message == null)
                badResponse.substring(0, badResponse.indexOf("%s") + 2).format(code)
            else
                badResponse.format(code, message)
            setDataReceivingState(
                DataReceivingState.Error(changeBadResponse)
            )
            Log.i(
                "Bad request", "${responseResult.raw().request.url}, " +
                        "${responseResult.code()}, ${responseResult.message()}, " +
                        "${responseResult.body()}, ${responseResult.errorBody()?.string()}"
            )
        }
    }
    else {
        setDataReceivingState(DataReceivingState.Empty())
    }
}