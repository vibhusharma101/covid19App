package com.aossie.covidapp.ui.essentials

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.aossie.covidapp.entities.Responses.ResourceResponse
import com.aossie.covidapp.entities.dataholders.ResourceData
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.util.ApiException
import com.aossie.covidapp.util.Coroutine
import com.aossie.covidapp.util.NoInternetException

class EssentialViewModel(private var repo:MyRepository):ViewModel() {

     var state:String ?=null
     var description:String?=null
    private var wholeList :List<ResourceData>?=null
     var essentialListener:EssentialListener?=null
    var stateSet:MutableSet<String>?=null
    var categorySet:MutableSet<String>?=null


    fun setActivity()
    {
        stateSet = mutableSetOf()
        stateSet!!.add("All")
        categorySet = mutableSetOf()
        categorySet!!.add("All")
        state ="All"
        description ="All"
        Coroutine.main{
            try {
                val resourceResponse :ResourceResponse = repo.getResources()
                resourceResponse.resources.let {
                    wholeList =it

                    for(i in 0 until it.size-1)
                    {
                        stateSet!!.add(wholeList!![i].state!!)
                        categorySet!!.add(wholeList!![i].category!!)
                    }

                    essentialListener!!.giveEntries(stateSet!!.toList(),categorySet!!.toList())
                    return@main
                }

            }
            catch(e:ApiException)
            {
                essentialListener!!.onFailure(e.message!!)
            }
            catch (e:NoInternetException)
            {
                essentialListener!!.onFailure(e.message!!)
            }
        }

    }



    fun doSearch(view: View)
    {

        essentialListener!!.pause()

        if(state.equals("All") && description.equals("All"))
        {
            essentialListener!!.onSuccess(wholeList!!)
        }
        else if(state.equals("All"))
        {
            var mList:ArrayList<ResourceData> = ArrayList()
            for(i in 0 until wholeList!!.size-1)
            {
                if(wholeList!![i].category.equals(description))
                {
                    mList.add(wholeList!![i])
                }

            }
            essentialListener!!.onSuccess(mList)

        }
        else if(description.equals("All")) {
            var mList:ArrayList<ResourceData> = ArrayList()
            for(i in 0 until wholeList!!.size-1)
            {
                if(wholeList!![i].state.equals(state))
                {
                    mList.add(wholeList!![i])
                }
                essentialListener!!.onSuccess(mList)
            }
        }
        else{

            var mList:ArrayList<ResourceData> = ArrayList()
            for(i in 0 until wholeList!!.size-1)
            {
                if(wholeList!![i].state.equals(state) && wholeList!![i].category.equals(description))
                {
                    mList.add(wholeList!![i])
                }
                    essentialListener!!.onSuccess(mList)



            }


        }
    }

    fun goBack(view: View)
    {
     essentialListener!!.back()
    }


}