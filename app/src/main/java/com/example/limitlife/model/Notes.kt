package com.example.limitlife.model

import kotlinx.serialization.Serializable


@Serializable
data class Notes(
    val heading : String ,
    val dateCreated : String ,
    val id : Int ,
    val lastEdit : String ,
    val content : String ,
)