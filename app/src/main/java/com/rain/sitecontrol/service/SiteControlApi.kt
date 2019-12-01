package com.rain.sitecontrol.service

import com.google.gson.annotations.SerializedName
import com.rain.sitecontrol.BuildConfig
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

data class PredictRequest(
        @SerializedName("url") val url: String,
        @SerializedName("secret") val secret: String = BuildConfig.ACCESS_TOKEN
)

data class PredictResponse(
        @SerializedName("result") val result: Boolean,
        @SerializedName("negative") val negative: String,
        @SerializedName("positive") val positive: String
)

interface SiteControlApi {

    @POST("validate")
    fun predict(@Body request: PredictRequest): Single<PredictResponse>
}
