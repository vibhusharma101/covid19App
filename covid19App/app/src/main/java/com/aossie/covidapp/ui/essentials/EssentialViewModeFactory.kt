package com.aossie.covidapp.ui.essentials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aossie.covidapp.repository.MyRepository


class EssentialViewModeFactory (private val repo: MyRepository): ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EssentialViewModel(repo) as T
    }



}