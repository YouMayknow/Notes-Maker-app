package com.example.limitlife.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limitlife.repository.NetworkUserDataRepository
import com.example.limitlife.repository.OfflineUserTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private  val userTokenRepository: OfflineUserTokenRepository ,
    private val userDataRepository: NetworkUserDataRepository ,

) : ViewModel() {
    private val _isTokenValid = MutableStateFlow<Boolean?>(null)
    val isTokenValid : StateFlow<Boolean?> =  _isTokenValid

    private val _isNewUser = MutableStateFlow<Boolean?>(null)
    val isNewUser : StateFlow<Boolean?> =  _isNewUser

    fun checkLoginRequirement() = viewModelScope.launch {
        val token = userTokenRepository.userToken.first()
        _isNewUser.value = token.isEmpty()

        val tokenResponse = userDataRepository.isTokenValid()
        _isTokenValid.value = tokenResponse.isSuccessful
    }
}