package com.example.limitlife

import android.app.Application
import com.example.limitlife.repository.OfflineUserTokenRepository

 // here we are assinging the name and space to teh datastore using key value pairs
class LimitLifeApplication : Application() {
    lateinit var offlineUserTokenRepository : OfflineUserTokenRepository
    override fun onCreate() {
        super.onCreate()
      // offlineUserTokenRepository = OfflineUserTokenRepository(dataStore = datastore)
    }
}