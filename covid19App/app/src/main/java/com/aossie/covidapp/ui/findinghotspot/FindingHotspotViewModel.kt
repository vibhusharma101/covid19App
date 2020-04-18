package com.aossie.covidapp.ui.findinghotspot

import android.location.Location
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.aossie.covidapp.entities.Responses.HotSpotResponse
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.util.ApiException
import com.aossie.covidapp.util.Coroutine
import com.aossie.covidapp.util.NoInternetException
import com.google.android.gms.maps.model.LatLng

class FindingHotspotViewModel(private val repo:MyRepository):ViewModel() {

    var hotspotListener:HotspotListener?=null
    var location : Location?=null
    var points :ArrayList<LatLng>?=null
    var addresses :ArrayList<String>?=null

    fun setActivity(){

        location = Location("")

        location!!.latitude =21.146633
        location!!.longitude =79.088860

        Coroutine.main{

            try {

                val hotspotDataResponse:HotSpotResponse = repo.getHotspots()

                hotspotDataResponse.hotspots.let {

                    points  = ArrayList()
                    addresses = ArrayList()
                    for(i in 0 until it.size-1)
                    {
                        var locationString :String  = it[i].latlong!!
                        if(!locationString.equals(""))
                        {
                            for(j in 0 until locationString.length-1)
                            {
                                if(locationString[j] == ',')
                                {
                                    if(!locationString.substring(j+1).equals("76.7879638,18")) {
                                        var loc: LatLng = LatLng(
                                            locationString.substring(0, j).toDouble(),
                                            locationString.substring(j+1).toDouble()
                                        )
                                        points!!.add(loc)
                                        addresses!!.add(it[i].address!!)
                                        break
                                    }
                                    else{
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    hotspotListener!!.onSuccess(points!!,addresses!!,location!!)
                    return@main
                }
            }
            catch (e:ApiException)
            {
                hotspotListener
            }
            catch (e:NoInternetException)
            {

            }


        }


    }


    fun onNearestHotspotClicked(view: View)
    {
        Log.d("nearest hostd",view.tag.toString())
        if(view.tag.equals("nearest")) {
            var minDis: Float = Float.MAX_VALUE
            var index: Int = 0
            for (i in 0 until points!!.size - 1) {
                var tempLocation: Location = Location("")
                tempLocation.latitude = points!![i].latitude
                tempLocation.longitude = points!![i].longitude

                var tempdist = location!!.distanceTo(tempLocation)
                if (tempdist < minDis) {
                    minDis = tempdist
                    index = i
                }

            }
            hotspotListener!!.nearestHotspot(points!![index], addresses!![index], location!!,minDis)
            view.tag ="All"
        }
        else if(view.tag.equals("All"))
        {
            view.tag ="nearest"
            hotspotListener!!.onSuccess(points!!,addresses!!,location!!)
        }









    }

    fun goBack(view:View)
    {
hotspotListener!!.back()
    }

}