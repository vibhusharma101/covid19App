package com.aossie.covidapp.ui.statewise

import android.view.View
import androidx.lifecycle.ViewModel
import com.aossie.covidapp.entities.Responses.StateCasesResponses
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.util.ApiException
import com.aossie.covidapp.util.Coroutine
import com.aossie.covidapp.util.NoInternetException

class StatewiseViewModel(private val repo:MyRepository):ViewModel() {

    var stateDataListener:StateDataListener?=null


    fun setUi(){

        Coroutine.main {

            try {
                val stateCasesResponse:StateCasesResponses =repo.getStateCases()
                stateCasesResponse.statecases.let {
                    stateDataListener!!.onSuccess(it)
                    return@main
                }
                }
            catch (e: NoInternetException) {
               stateDataListener!!.onFailure(e.message!!)
            }
            catch (e: ApiException)
            {
                stateDataListener!!.onFailure(e.message!!)
            }



        }




    }

    fun goBack(view: View)
    {
      stateDataListener!!.back()
    }





}