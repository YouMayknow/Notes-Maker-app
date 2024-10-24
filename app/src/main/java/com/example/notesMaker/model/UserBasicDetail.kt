package com.example.notesMaker.model

import kotlinx.serialization.Serializable


@Serializable
data class UserBasicDetail(
    val username : String ,
    val password : String ,
    val token : String ,
)