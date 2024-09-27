package com.example.limitlife.data.token

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.concurrent.Flow

class UserTokenRepository(
    private  val dataStore : DataStore<Preferences>

) {
   private companion object {
        val TOKEN = stringPreferencesKey("")
    }
    suspend fun saveUserToken (userToken : String) {
        dataStore.edit { token ->
            token[TOKEN] =  userToken
        }
    }
    val userToken : kotlinx.coroutines.flow.Flow<String> = dataStore.data
        .catch {
            if ( it is IOException){
                Log.d("UserTokenRepository" , "Error reading the toekn from database ", it )
                emit(emptyPreferences())
            }
            else {
                throw it
            }
        }
        .map { token ->
        token[TOKEN] ?: ""
    }
}