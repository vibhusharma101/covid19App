package com.aossie.covidapp.ui.findinghotspot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aossie.covidapp.repository.MyRepository

class FindingHotspotViewModelRepository (private val repo: MyRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FindingHotspotViewModel(repo) as T

    }

}
