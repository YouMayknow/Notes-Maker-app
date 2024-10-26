package com.example.notesMaker.model

import com.example.notesMaker.network.ShortNote
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