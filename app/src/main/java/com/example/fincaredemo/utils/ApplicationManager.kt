package com.example.fincaredemo.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.multidex.MultiDex


class ApplicationManager : Application() {
    private var context: Context = this@ApplicationManager

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        mContext = this
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        lateinit var instance: ApplicationManager
    }
}