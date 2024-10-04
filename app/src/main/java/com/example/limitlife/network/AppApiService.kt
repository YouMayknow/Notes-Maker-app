package com.example.limitlife.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface AppApiService {
    @FormUrlEncoded
    @POST("/register")
    suspend fun registerUser (
        @Field("username") username: String,
        @Field("password") password: String
    ) : retrofit2.Response<ResponseBody>

    @FormUrlEncoded
    @POST("/login")
    suspend fun loginUser (
        @Field("username")username: String ,
        @Field("password")password: String
    ) : retrofit2.Response<LoginResponse>
}

@Serializable
 data class LoginResponse (
        @SerialName("token")val token : String
 )