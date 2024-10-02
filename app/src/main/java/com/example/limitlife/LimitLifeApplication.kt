package com.example.limitlife

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.limitlife.data.token.UserTokenRepository

 // here we are assinging the name and space to teh datastore using key value pairs
private const val USER_PREFERENCE_NAME = "token_preferences"
private val Context.datastore : DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCE_NAME
)
class LimitLifeApplication : Application() {
    lateinit var userTokenRepository : UserTokenRepository
    override fun onCreate() {
        super.onCreate()
        userTokenRepository = UserTokenRepository(dataStore = datastore)
    }
}