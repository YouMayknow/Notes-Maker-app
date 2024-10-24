package com.example.limitlife.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


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
    suspend fun  getAllUserNotes() : retrofit2.Response<List<UpdatedShortNote>>

    @POST("/notes/new")
    suspend fun createNewNote(
      @Body shortNote: ShortNote
    ) : retrofit2.Response<ResponseBody>


    @GET("/token/verifier")
    suspend fun isTokenValid(
    ) : Response<ResponseBody>

    @PATCH("/notes/update")
    suspend fun updateNote(
        @Body updatedShortNote: UpdatedShortNote
    ) : Response<ResponseBody>

    @DELETE("/notes/delete/{id}")
    suspend fun  deleteSelectedNote(
         @Path("id") id : Int
    ) : Response<ResponseBody>

    @GET("/notes/{id}")
    suspend fun  getSelectedNote(
        @Path("id") id : Int
    ) : Response<DetailedNote>
}

@Serializable
 data class LoginResponse (
        @SerialName("token")val token : String
 )

@Serializable
data class ShortNote(
    val content : String,
    val heading: String,
)

@Serializable
data class UpdatedShortNote(
    val content : String,
    val heading: String,
    val  id : Int ,
    val localNoteId : Int? = 0
)

@Serializable
data class DetailedNote(
    val dateCreated : String,
    val lastCreated : String,
)