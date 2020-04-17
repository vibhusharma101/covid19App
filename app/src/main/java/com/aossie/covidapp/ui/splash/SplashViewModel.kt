package com.aossie.covidapp.ui.splash

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel

class SplashViewModel(application: Application) : AndroidViewModel(application) {


    var networkListener:NetworkListener?=null

        fun checkNetworkConnection(){
        val connectivityManager: ConnectivityManager = getApplication<Application>().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
                networkListener!!.proceed(it!=null)
        }
    }





}