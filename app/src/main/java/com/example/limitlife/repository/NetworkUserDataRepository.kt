package com.example.limitlife.repository

import com.example.limitlife.network.AppApiService
import com.example.limitlife.network.LoginResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


interface  UserDataRepository : AppApiService {
   override suspend fun registerUser (username: String, password: String) : Response<ResponseBody>
  override suspend fun loginUser(username: String, password: String) : Response<LoginResponse>
}
class NetworkUserDataRepository @Inject constructor(
  //  private  val appApiService: AppApiService
) : UserDataRepository {
    private  val baseUrl =" http://192.168.1.12:8080"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val retrofitService : AppApiService by lazy {
        retrofit.create(AppApiService::class.java)
    }
    override suspend fun registerUser(username: String, password: String) = retrofitService.registerUser(username, password)
    override suspend fun loginUser(username: String, password: String): Response<LoginResponse> = retrofitService.loginUser(username, password)
}