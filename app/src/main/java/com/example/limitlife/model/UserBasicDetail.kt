package com.example.limitlife.model

import kotlinx.serialization.Serializable

//interface AppApiService :  {
//    val tokenRepository : UserDataRepository
//}

@Serializable
data class UserBasicDetail(
    val username : String ,
    val password : String ,
    val token : String ,
)