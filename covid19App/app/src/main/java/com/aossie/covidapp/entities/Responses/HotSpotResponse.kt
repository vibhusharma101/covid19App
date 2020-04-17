package com.aossie.covidapp.entities.Responses

import com.aossie.covidapp.entities.dataholders.HotSpotData
import com.google.gson.annotations.SerializedName

class HotSpotResponse(@SerializedName("travel_history")var hotspots:List<HotSpotData> ) {
}