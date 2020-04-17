package com.aossie.covidapp.ui.statewise

import com.aossie.covidapp.entities.dataholders.StatewiseCase

interface StateDataListener {

    fun onSuccess(stateList:List<StatewiseCase>)

    fun onFailure(message:String)


}