package com.example.limitlife.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class OfflineUserTokenRepository(
    private  val dataStore : DataStore<Preferences>

) {
   private companion object {
        val TOKEN = stringPreferencesKey("user_token")
    }
    suspend fun saveUserToken (userToken : String) {
        dataStore.edit { token ->
            token[TOKEN] =  userToken
        }
    }
    val userToken : kotlinx.coroutines.flow.Flow<String> = dataStore.data
        .catch {
            if ( it is IOException){
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