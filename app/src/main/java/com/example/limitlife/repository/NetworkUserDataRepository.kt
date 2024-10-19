package com.example.limitlife.repository

import androidx.compose.material3.TimeInput
import com.example.limitlife.network.AppApiService
import com.example.limitlife.network.DetailedNote
import com.example.limitlife.network.LoginResponse
import com.example.limitlife.network.ShortNote
import com.example.limitlife.network.UpdatedShortNote
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject


interface  UserDataRepository : AppApiService {
   override suspend fun registerUser (username: String, password: String) : Response<ResponseBody>
  override suspend fun loginUser(username: String, password: String) : Response<LoginResponse>
}
class NetworkUserDataRepository @Inject constructor(
    tokenRepository: OfflineUserTokenRepository
) : UserDataRepository {
    private var client = OkHttpClient.Builder()
      //  .callTimeout(5 , TimeUnit.SECONDS)
        .addInterceptor(AuthInspector(tokenRepository))
        .build()
    private  val baseUrl = "http://192.168.1.14:8080"
   private val json = Json{ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val retrofitService : AppApiService by lazy {
        retrofit.create(AppApiService::class.java)
    }
    override suspend fun registerUser(username: String, password: String) = retrofitService.registerUser(username, password)
    override suspend fun loginUser(username: String, password: String): Response<LoginResponse> = retrofitService.loginUser(username, password)
    override suspend fun getAllUserNotes(): Response<List<UpdatedShortNote>> =  retrofitService.getAllUserNotes()
    override suspend fun createNewNote(shortNote: ShortNote): Response<ResponseBody> = retrofitService.createNewNote(shortNote)
    override suspend fun isTokenValid(): Response<ResponseBody> = retrofitService.isTokenValid()
    override suspend fun updateNote(updatedShortNote: UpdatedShortNote): Response<ResponseBody>  = retrofitService.updateNote(updatedShortNote)
    override suspend fun deleteSelectedNote(id: Int): Response<ResponseBody>  = retrofitService.deleteSelectedNote(id)
    override suspend fun getSelectedNote(id: Int): Response<DetailedNote> = retrofitService.getSelectedNote(id)
}

class AuthInspector ( private val tokenRepository: OfflineUserTokenRepository ) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request() // intercept the chain and get the original request that is being made
        //create a builder to create a new request with manipulation
        val token =   runBlocking {
              tokenRepository.userToken.first()
}
        val builder = originalRequest.newBuilder()
            .header("Authorization","Bearer $token")
        return chain.proceed(builder.build())
    }
}


