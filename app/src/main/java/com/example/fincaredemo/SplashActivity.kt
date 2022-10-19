package com.example.fincaredemo

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.fincaredemo.databinding.ActivitySplashBinding

class SplashActivity : Activity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        try {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor =
                ContextCompat.getColor(this@SplashActivity, R.color.teal_600)
            window.navigationBarColor =
                ContextCompat.getColor(this@SplashActivity, R.color.black)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            launchNextActivity()
        }, 500)
    }

    private fun launchNextActivity() {
        try {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}