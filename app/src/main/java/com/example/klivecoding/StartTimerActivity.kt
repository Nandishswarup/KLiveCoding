package com.example.klivecoding

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.klivecoding.Service.CountDownTimerService
import com.example.klivecoding.databinding.ActivityStartTimerBinding


class StartTimerActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStartTimerBinding
    private var TIME_INFO = "time_info"
    lateinit var  brReceiver:BroadcastReceiver

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
            disableButtons(true)



        }
        brReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent != null && !isFinishing) {
                    if(intent.action==TIME_INFO)
                    {
                        var value=intent.getStringExtra("VALUE").toString()
                        var countDownTime=intent.getStringExtra("countDownTime").toString()
                        binding.edtTime.setText(countDownTime)
                        binding.tvCountDown.setText(value)
                        disableButtons(true)

                        if(value=="Completed")
                        {
                            disableButtons(false)
                            binding.tvCountDown.setText("")
                            stopService()
                        }
                    }

                }
            }
        }


    }

    private fun disableButtons(b: Boolean) {

        binding.btnSubmit.isEnabled=!b
        binding.edtTime.isEnabled=!b
    }


    override fun onResume() {
        super.onResume()
        var filter = IntentFilter(TIME_INFO)
        registerReceiver(brReceiver,filter)
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(brReceiver)
    }

    fun stopService()
    {
        val intent = Intent(this, CountDownTimerService::class.java)
        stopService(intent)
    }
}