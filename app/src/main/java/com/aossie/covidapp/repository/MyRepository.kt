package com.aossie.covidapp.repository

import com.aossie.covidapp.entities.Responses.*
import com.aossie.covidapp.entities.dataholders.ResourceData
import com.aossie.covidapp.network.CovidApi
import com.aossie.covidapp.network.SafeApiRequest
import javax.inject.Inject

class MyRepository (private val api:CovidApi) :SafeApiRequest(){

    suspend fun getDailyCases():DailyCasesResponse
    {
        return apiRequest { api.getDailyData() }
    }

    suspend fun getStateCases():StateCasesResponses
    {
        return apiRequest{api.getStateData()}
    }

    suspend fun getResources():ResourceResponse
    {
        return apiRequest {api.getResourcesData()}
    }

    suspend fun getHotspots():HotSpotResponse{

        return apiRequest { api.getHotspotData() }
    }
    suspend fun getWholeData():VariationResponse
    {
        return apiRequest { api.getWholeData() }
    }
    suspend fun  getStateDailyData():StateDailyResponse{
        return apiRequest { api.getDailyStateData() }
    }


}