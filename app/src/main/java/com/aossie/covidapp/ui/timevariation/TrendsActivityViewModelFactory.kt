package com.aossie.covidapp.ui.timevariation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aossie.covidapp.repository.MyRepository

class TrendsActivityViewModelFactory (private val repo: MyRepository): ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrendsActivityViewModel(repo) as T
    }
}