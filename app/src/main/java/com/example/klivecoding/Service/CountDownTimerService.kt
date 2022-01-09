package com.example.klivecoding.Service

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
 import com.example.klivecoding.R
 import com.example.klivecoding.StartTimerActivity
import java.util.concurrent.TimeUnit


class CountDownTimerService : Service() {
    private var TIME_INFO = "time_info"
    private lateinit var counterclass: CounterClass
    private lateinit var countDownTime: String
    val NOTIFICATION_CHANNEL_ID = "com.example.klivecoding"
    val channelName = "My Background Service"
    val NOTIFICATION_ID = 201


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

        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        startForeground(NOTIFICATION_ID, CreateUpdateNotification(""))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        countDownTime = intent!!.getStringExtra("countDownTime")!!;
        counterclass = CounterClass(countDownTime.toLong() * 1000 * 60, 1000)
        counterclass.start()
        return START_NOT_STICKY

    }

    private fun CreateUpdateNotification(updateTime: String): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setContentTitle("Countdown Timer")
            .setContentText("TIME- " + updateTime)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(201, notification)
        return notification
    }


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
            sendBroadcast(timerInfoIntent)
            CreateUpdateNotification(hms)
        }

        override fun onFinish() {
            val timerInfoIntent = Intent(TIME_INFO)
            timerInfoIntent.putExtra("VALUE", "Completed")
            LocalBroadcastManager.getInstance(this@CountDownTimerService)
                .sendBroadcast(timerInfoIntent)
        }
    }
}