package com.aossie.covidapp.ui.statewise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aossie.covidapp.repository.MyRepository

class StateWiseViewModelFactory(private val repo:MyRepository):ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StatewiseViewModel(repo)as T
    }




}