package com.example.klivecoding

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

open class TimerStatusReceiver: BroadcastReceiver() {
    private var TIME_INFO = "time_info"
    lateinit var  callback:Callback

    override fun onReceive(p0: Context?, intent: Intent?) {

        if(intent!=null && intent.action!!.equals(TIME_INFO))
        {
            callback.onUpdate(intent.getStringExtra("VALUE")!!)
        }



    }
    interface Callback{
        fun onUpdate(time:String)
    }
}