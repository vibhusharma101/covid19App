package com.aossie.covidapp.entities.Responses

import com.aossie.covidapp.entities.dataholders.StateDaily
import com.google.gson.annotations.SerializedName

class StateDailyResponse (@SerializedName("states_daily")var dailyStateCases:List<StateDaily>){
}