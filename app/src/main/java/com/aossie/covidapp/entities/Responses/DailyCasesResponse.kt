package com.aossie.covidapp.entities.Responses

import com.aossie.covidapp.entities.dataholders.DailyCasesInfo
import com.google.gson.annotations.SerializedName

 data class DailyCasesResponse(
     @SerializedName("cases_time_series")var dailyCases: List<DailyCasesInfo>) {

}