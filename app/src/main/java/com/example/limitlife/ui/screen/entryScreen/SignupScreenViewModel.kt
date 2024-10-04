package com.example.limitlife.ui.screen.entryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limitlife.R
import com.example.limitlife.repository.OfflineUserTokenRepository
import com.example.limitlife.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class  SignupScreenViewModel @Inject constructor(
    private  val userDataRepository: UserDataRepository,
    private val userTokenRepository: OfflineUserTokenRepository ,
) : ViewModel() {
    private var _uiState =  MutableStateFlow(SignupScreenUiState())
    val uiState = _uiState.asStateFlow()
    fun aheadActionButton (username: String , password: String)= viewModelScope.launch{
        try {
            if (_uiState.value.currentScreen == AuthenticationScreen.Signup) {
                val response = userDataRepository.registerUser(username, password)
                _uiState.update {
                    it.copy(
                        responseToDisplay = if (response.isSuccessful) {
                            response.body()?.string()
                        } else {
                            response.errorBody()?.string() ?: "Unknown error occurred"
                        }
                    )
                }
            }
            else {
                val response = userDataRepository.loginUser(username, password)
                if (response.isSuccessful){
                    _uiState.update {
                        it.copy(
                            responseToDisplay = "Successful ,logging....."
                        )
                    }
                    userTokenRepository.saveUserToken(response.body()?.token ?: "")
                    _uiState.update {
                        it.copy(
                            responseToDisplay = userTokenRepository.userToken.toString()
                        )
                    }
                    val userToken = userTokenRepository.userToken.map {
                        it
                    }.stateIn(viewModelScope , SharingStarted.WhileSubscribed(5000L),"" )
                } else {
                    _uiState.update {
                        it.copy(
                            responseToDisplay = response.errorBody()?.string()
                        )
                    }
                }
            }
        } catch (e : IOException) {
            _uiState.update {
                it.copy(
                    responseToDisplay = "Showing IO Exception $e"
                )
            }
        } catch (e : HttpException) {
            _uiState.update {
                it.copy(
                    /* for wrong id password = unauthorised
                    For empty request = Bad Request
                    For login , without signup 1st time = Internal server occured
                    For success = Token
                     */
                    responseToDisplay = e.message()
                )
            }
        }

    }

    fun navigateScreenButtonAction (){
        _uiState.update {
            it.copy(
                currentScreen = when (it.currentScreen){
                    AuthenticationScreen.Signup -> AuthenticationScreen.Login
                    else -> AuthenticationScreen.Signup
                }
            )
        }
    }
}

data class  SignupScreenUiState(
    val responseToDisplay : String? = "" ,
    val currentScreen : AuthenticationScreen = AuthenticationScreen.Login ,
)

enum class  AuthenticationScreen(
    val heading: Int ,
    val aheadActionButton: Int ,
    val navigateScreenButtonHeading : Int ,
){
    Signup(heading = R.string.signUp , aheadActionButton = R.string.create_account  , navigateScreenButtonHeading = R.string.navigate_to_login) ,
    Login(heading = R.string.Login , aheadActionButton = R.string.login_account , navigateScreenButtonHeading = R.string.navigate_to_signup)
}