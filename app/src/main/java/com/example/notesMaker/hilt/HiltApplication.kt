package com.example.notesMaker.hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*this works the same way as we do with the application but here we are using the
hilt annotation that take code generation and code base that made the dependency
available throughout
 */
@HiltAndroidApp
class HiltApplication : Application() {

}