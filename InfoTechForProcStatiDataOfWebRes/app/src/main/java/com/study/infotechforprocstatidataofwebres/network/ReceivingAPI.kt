package com.study.infotechforprocstatidataofwebres.network

import com.study.infotechforprocstatidataofwebres.models.GeneralInfo
import com.study.infotechforprocstatidataofwebres.models.ImageContainer
import com.study.infotechforprocstatidataofwebres.models.ScriptInfo
import com.study.infotechforprocstatidataofwebres.models.StyleInfo
import com.study.infotechforprocstatidataofwebres.models.TagQuantity
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReceivingAPI {
    @POST("api/get_general_info")
    @FormUrlEncoded
    suspend fun getGeneralInfo(@Field("url") url: String): Response<GeneralInfo>?

    @POST("api/get_meta_info")
    @FormUrlEncoded
    suspend fun getMetaInfo(@Field("url") url: String) : Response<List<Map<String, String>>>?

    @POST("api/get_all_images")
    @FormUrlEncoded
    suspend fun getAllImages(@Field("url") url: String) : Response<List<ImageContainer>>?

    @POST("api/get_styles_info")
    @FormUrlEncoded
    suspend fun getStylesInfo(@Field("url") url: String) : Response<List<StyleInfo>>?

    @POST("api/get_scripts_info")
    @FormUrlEncoded
    suspend fun getScriptsInfo(@Field("url") url: String) : Response<List<ScriptInfo>>?

    @POST("api/get_quantity_tags")
    @FormUrlEncoded
    suspend fun getQuantityTags(
        @Field("url") url: String,
        @Query("tags") tags: List<String>
    ): Response<List<TagQuantity>>?
}