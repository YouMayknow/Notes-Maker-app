package com.example.notesMaker.hilt

import android.app.Application
import android.content.res.Configuration
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/*this works the same way as we do with the application but here we are using the
hilt annotation that take code generation and code base that made the dependency
available throughout
 */
@HiltAndroidApp
class HiltApplication : Application(), androidx.work.Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration() =
        androidx.work.Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    // Inject your custom WorkerFactory, which will be provided by Hilt . 
}