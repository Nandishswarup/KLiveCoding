package com.example.klivecoding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.klivecoding.Service.CountDownTimerService
import com.example.klivecoding.databinding.ActivityStartTimerBinding


class StartTimerActivity : AppCompatActivity(), TimerStatusReceiver.Callback {
    private lateinit var binding:ActivityStartTimerBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStartTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSubmit.setOnClickListener {
            var countDownTime=binding.edtTime.text.toString()
            val intent = Intent(this, CountDownTimerService::class.java)
            intent.putExtra("countDownTime",countDownTime)
            startForegroundService(intent)


        }



    }

    override fun onUpdate(time: String) {
        Log.e("onUpdate ! !",time)
    }

    fun stopService()
    {
        val intent = Intent(this, CountDownTimerService::class.java)
        stopService(intent)
    }
}