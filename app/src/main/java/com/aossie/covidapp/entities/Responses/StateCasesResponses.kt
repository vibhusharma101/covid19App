package com.aossie.covidapp.entities.Responses

import com.aossie.covidapp.entities.dataholders.StatewiseCase
import com.google.gson.annotations.SerializedName

data class StateCasesResponses(@SerializedName("statewise")var statecases:List<StatewiseCase>){
}