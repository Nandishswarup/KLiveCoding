package com.example.klivecoding.Service

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.klivecoding.StartTimerActivity
import java.util.concurrent.TimeUnit


class CountDownTimerService : Service() {
    private var TIME_INFO = "time_info"
    private lateinit var counterclass: CounterClass
    private lateinit var countDownTime: String

    override fun onBind(p0: Intent?): IBinder? {

        return null

    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
            1,
            Notification()
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "com.example.klivecoding"
        val channelName = "My Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_dialog_alert)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        countDownTime = intent!!.getStringExtra("countDownTime")!!;
        counterclass = CounterClass(countDownTime.toLong(), 1000)
        counterclass.start()
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("com.example.klivecoding","KLiveCoding")
        }
        val notificationIntent = Intent(this, StartTimerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification: Notification = NotificationCompat.Builder(this)
            .setContentText("Counter down service")
            .setContentIntent(pendingIntent).build()

        startForeground(202, notification)*/

        return START_NOT_STICKY

    }

   /* @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }*/


    inner class CounterClass(millisInFuture: Long, countDownInterval: Long) :

        CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            Log.e("millisUntilFinished", "" + millisUntilFinished)

            val hms = String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(
                        millisUntilFinished
                    )
                ),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(
                        millisUntilFinished
                    )
                )
            )
            Log.e("hms", "" + hms)

            val timerInfoIntent = Intent(TIME_INFO)
            timerInfoIntent.putExtra("VALUE", hms)
            LocalBroadcastManager.getInstance(this@CountDownTimerService)
                .sendBroadcast(timerInfoIntent)
        }

        override fun onFinish() {
            val timerInfoIntent = Intent(TIME_INFO)
            timerInfoIntent.putExtra("VALUE", "Completed")
            LocalBroadcastManager.getInstance(this@CountDownTimerService)
                .sendBroadcast(timerInfoIntent)
        }
    }
}