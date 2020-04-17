package com.aossie.covidapp.entities.Responses

import com.aossie.covidapp.entities.dataholders.ResourceData
import com.google.gson.annotations.SerializedName

data class ResourceResponse(@SerializedName("resources")var resources:List<ResourceData>) {
}