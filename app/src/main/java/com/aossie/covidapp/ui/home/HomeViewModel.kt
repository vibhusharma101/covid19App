package com.aossie.covidapp.ui.home

import android.view.View
import androidx.lifecycle.ViewModel
import com.aossie.covidapp.entities.Responses.DailyCasesResponse
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.util.ApiException
import com.aossie.covidapp.util.Coroutine
import com.aossie.covidapp.util.NoInternetException

class HomeViewModel(private val repo:MyRepository) : ViewModel() {

    var homeListener:HomeListener?=null

     fun setUi(){

         Coroutine.main {

         try {
             val dailyCasesResponse: DailyCasesResponse = repo.getDailyCases()

             dailyCasesResponse.dailyCases[dailyCasesResponse.dailyCases.size-1].let {
                 homeListener!!.onSuccess(it)
                 return@main
             }
         } catch (e:NoInternetException) {
             homeListener!!.onFailure(e.message!!)
         }
             catch (e:ApiException)
             {
                 homeListener!!.onFailure(e.message!!)
             }
     }

    }

    fun onStateButtonClick(view: View)
    {
        homeListener!!.redirect()
    }




}