package com.aossie.covidapp.ui.findinghotspot
import android.location.Location
import com.google.android.gms.maps.model.LatLng

interface HotspotListener {

    fun onSuccess(hotspotList:List<LatLng>,addressList :List<String>,location:Location)

    fun onFailure(message:String)

    fun nearestHotspot(hotspot:LatLng,address:String,location: Location,distance:Float)

    fun back()


}