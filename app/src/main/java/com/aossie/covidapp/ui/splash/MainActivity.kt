package com.aossie.covidapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.aossie.covidapp.R
import com.aossie.covidapp.databinding.ActivityMainBinding
import com.aossie.covidapp.ui.home.HomeActivity
import com.aossie.covidapp.util.toast
import java.lang.Exception

class MainActivity : AppCompatActivity(),NetworkListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding :ActivityMainBinding  =DataBindingUtil.setContentView(this,R.layout.activity_main)
        val viewModel:SplashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        binding.splashviewmodel =viewModel
        viewModel.networkListener =this

        Handler().postDelayed(Runnable {
            viewModel.checkNetworkConnection()
        },1000)

    }

    override fun proceed(move: Boolean) {
        if(!move)
        {
         toast("CheckInternetConnection")
        }
        else{
            startActivity(Intent(applicationContext,HomeActivity::class.java))
            finish()
        }
    }

}
