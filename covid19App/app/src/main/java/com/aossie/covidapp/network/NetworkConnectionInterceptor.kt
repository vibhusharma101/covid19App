package com.aossie.covidapp.network

import android.content.Context
import android.net.ConnectivityManager
import com.aossie.covidapp.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(var context: Context) : Interceptor{


    private val appContext = context.applicationContext


    override fun intercept(chain: Interceptor.Chain): Response {

        if(!isInternetAvailable())
        {
            throw NoInternetException("Please Check Your Internet Connection")
        }

       return chain.proceed(chain.request())
    }

    private fun isInternetAvailable():Boolean{

        val connectivityManager:ConnectivityManager =appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it!=null
        }




    }

}