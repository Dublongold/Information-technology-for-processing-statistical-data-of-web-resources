package com.study.infotechforprocstatidataofwebres.network

import android.util.Log
import com.study.infotechforprocstatidataofwebres.models.GeneralInfo
import com.study.infotechforprocstatidataofwebres.models.ImageContainer
import com.study.infotechforprocstatidataofwebres.models.ScriptInfo
import com.study.infotechforprocstatidataofwebres.models.StyleInfo
import com.study.infotechforprocstatidataofwebres.models.TagQuantity
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.Duration
import java.util.concurrent.TimeUnit

class SaveReceivingClient : ReceivingAPI {
    private var wasInstalled = false
    private lateinit var receivingAPI: ReceivingAPI

    override suspend fun getGeneralInfo(url: String): Response<GeneralInfo>? {
        return saveRequest { receivingAPI.getGeneralInfo(url) }
    }

    override suspend fun getMetaInfo(url: String): Response<List<Map<String, String>>>? {
        return saveRequest { receivingAPI.getMetaInfo(url) }
    }

    override suspend fun getAllImages(url: String): Response<List<ImageContainer>>? {
        return saveRequest { receivingAPI.getAllImages(url) }
    }

    override suspend fun getStylesInfo(url: String): Response<List<StyleInfo>>? {
        return saveRequest { receivingAPI.getStylesInfo(url) }
    }

    override suspend fun getScriptsInfo(url: String): Response<List<ScriptInfo>>? {
        return saveRequest { receivingAPI.getScriptsInfo(url) }
    }

    fun wasInstalled() = wasInstalled

    override suspend fun getQuantityTags(
        url: String,
        tags: List<String>
    ): Response<List<TagQuantity>>? {
        return saveRequest { receivingAPI.getQuantityTags(url, tags) }
    }

    fun setOrChangeClient(baseUrl: String) {
        Log.i("Set or change client", "Base url: $baseUrl")
        receivingAPI = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient().newBuilder()
                    .followRedirects(true)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(RedirectInterceptor())
                    .build()
            ).addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReceivingAPI::class.java)
        wasInstalled = true
    }

    private suspend fun<T> saveRequest(request: suspend () -> Response<T>?): Response<T>? {
        while(true) {
            try {

                return request().apply {
                    Log.i("Save request", "Successful! Url: ${this?.raw()?.request?.url}")
                }
            }
            catch (e: UnknownHostException) {
                Log.w("Save request", "Unknown host exception. Try again...")
                delay(2000)
            }
            catch (e: SocketTimeoutException) {
                Log.w("Save request", "Socket timeout exception. Null returned.")
                return null
            }
            catch (e: ConnectException) {
                Log.w("Save request", "Connect exception. Try again...")
                delay(2000)
            }
        }
    }
}