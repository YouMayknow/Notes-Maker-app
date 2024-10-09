package com.example.limitlife.model

import com.example.limitlife.network.ShortNote
import kotlinx.serialization.Serializable


@Serializable
data class Notes(
    val heading : String ,
    val dateCreated : String ,
    val id : Int ,
    val lastEdit : String ,
    val content : String ,
    val image : String  = "null"
)


val defaultNote = ShortNote(
    content =  "",
    heading =  ""
)