package com.aossie.covidapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aossie.covidapp.repository.MyRepository
import javax.inject.Inject

class HomeViewModelFactory (private val repo:MyRepository):ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repo)as T
    }




}