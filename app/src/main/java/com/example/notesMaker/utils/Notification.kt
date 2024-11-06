package com.example.notesMaker.utils

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notesMaker.R
import com.example.notesMaker.worker.LOGGING_OF_APP

@RequiresApi(Build.VERSION_CODES.O)
fun  createNotificationChannel(context: Context) {
        val channel = NotificationChannel( // it provide more control to the user
            "retryAttempt125" , // unique channel id for the reference can say like the name of the channel
            "Upload Notifications" ,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifies when data upload fails" // description about the channel for clarity
        }
        val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun basicNotificationFramework(context : Context , title : String , text : String , notificationId : Int  ) {
    Log.e(LOGGING_OF_APP, "building notification from app.")
    val builder =  NotificationCompat.Builder(context, "retryAttempt125")
        .setSmallIcon(R.drawable.baseline_notifications_24)
        .setContentTitle(title)
        .setContentText(text)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    with(NotificationManagerCompat.from(context)){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(context as Activity , arrayOf(Manifest.permission.POST_NOTIFICATIONS) , 1001)
            notify(notificationId , builder.build())
        } else {
            notify(notificationId , builder.build())
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun showRetryNotification(context: Context, id : Int){
    val retryIntent = Intent(context ,RetryBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context ,
        0 ,
        retryIntent ,
        PendingIntent.FLAG_UPDATE_CURRENT // it ensures if there is a existing notification id then it will be update , not duplicated
    )
    // Building the notification
    val builder = NotificationCompat.Builder(context , "retryAttempt125")
        .setSmallIcon(R.drawable.baseline_notifications_24)
        .setContentTitle("Upload Failed")
        .setContentText("Tap to retry uploading your data.")
        .setPriority(NotificationCompat.PRIORITY_HIGH) // it ensures visibility and  sound too
        .addAction(R.drawable.baseline_redo_24, "Retry" , pendingIntent)
    // displaying the notification
    with(NotificationManagerCompat.from(context)){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS)  , 1001)
        } else {
            // Permission is granted, show the notification
            notify(id, builder.build()) // Shows notification with unique ID
        }
    }
}
class RetryBroadcastReceiver : BroadcastReceiver() { // here broadCastReceiver is used to handle the bg actions like starting uploadWorker again , even if the app isn't in the foreground
    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("Not yet implemented")
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun askForNotification(context: Activity) : Boolean {
    return if  (ActivityCompat.checkSelfPermission(context , Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.POST_NOTIFICATIONS) , 1000)
        false
    } else {
        true
    }
}
