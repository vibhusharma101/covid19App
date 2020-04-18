package com.aossie.covidapp.ui.essentials

import com.aossie.covidapp.entities.dataholders.ResourceData

interface EssentialListener {

     fun onSuccess(resources:List<ResourceData>)

    fun onFailure(message:String)

    fun giveEntries(states:List<String>,categories:List<String>)

    fun pause()

    fun back()


}