package com.aossie.covidapp.ui.timevariation

import com.github.mikephil.charting.data.LineDataSet

interface TrendListener {

    fun onSuccess(listC:List<LineDataSet>,listD:List<LineDataSet>,listR:List<LineDataSet>,conf:String,death:String,recov:String)

    fun setSpinner(statelist:List<String>)

    fun onFailure(message:String)

}