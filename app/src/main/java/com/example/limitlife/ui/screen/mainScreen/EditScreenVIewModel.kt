package com.example.limitlife.ui.screen.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limitlife.network.ShortNote
import com.example.limitlife.network.UpdatedShortNote
import com.example.limitlife.repository.NetworkUserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditScreenVIewModel @Inject constructor(
   private val userDataRepository: NetworkUserDataRepository
) : ViewModel() {
    var  response  =  MutableStateFlow("")
    fun createNote(shortNote: ShortNote) = viewModelScope.launch{
        try {
           val reply   =  userDataRepository.createNewNote(shortNote)
          response.value =   reply.message()
        } catch ( e : Exception ){
            response.value  = (e.message + response )
            Log.e("response","${e.message}")
        }
    }
    fun updateNote(updatedShortNote: UpdatedShortNote) = viewModelScope.launch {
        userDataRepository.updateNote(updatedShortNote)
    }
}