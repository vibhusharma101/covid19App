package com.aossie.covidapp.entities.Responses

import com.aossie.covidapp.entities.dataholders.DailyCasesInfo
import com.aossie.covidapp.entities.dataholders.StatewiseCase
import com.google.gson.annotations.SerializedName

class VariationResponse( @SerializedName("cases_time_series")var dailyCases: List<DailyCasesInfo>,
                         @SerializedName("statewise")var statecases:List<StatewiseCase>) {
}