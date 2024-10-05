package com.example.limitlife.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    @GET("/notes/all")
    suspend fun  getAllUserNotes() : retrofit2.Response<List<ShortNote>>

    @POST("/new/notes")
    suspend fun createNewNote(
      @Body shortNote: ShortNote
    )
}

@Serializable
 data class LoginResponse (
        @SerialName("token")val token : String
 )

@Serializable
data class ShortNote (
    val content : String,
    val heading: String,
)
