package com.aossie.covidapp.network

import com.aossie.covidapp.entities.Responses.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CovidApi {


    @GET("data.json")
    suspend fun getDailyData():retrofit2.Response<DailyCasesResponse>

    @GET("data.json")
    suspend fun getStateData():retrofit2.Response<StateCasesResponses>

    @GET("resources/resources.json")
    suspend fun getResourcesData():retrofit2.Response<ResourceResponse>

    @GET("travel_history.json")
    suspend fun getHotspotData():retrofit2.Response<HotSpotResponse>

    @GET("data.json")
    suspend fun getWholeData():retrofit2.Response<VariationResponse>

    @GET("states_daily.json")
    suspend fun getDailyStateData():retrofit2.Response<StateDailyResponse>


    companion object{

        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor):CovidApi{

            val okHttpClient:OkHttpClient = OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor).build()
            return Retrofit.Builder().client(okHttpClient)
                .baseUrl("https://api.covid19india.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CovidApi::class.java)
        }


    }




}