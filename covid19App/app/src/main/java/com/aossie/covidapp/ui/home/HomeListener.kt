package com.aossie.covidapp.ui.home

import com.aossie.covidapp.entities.dataholders.DailyCasesInfo

interface HomeListener {

    fun onSuccess(dailyCases : DailyCasesInfo)

    fun onFailure(message:String)

    fun redirect()
}