package com.aossie.covidapp.ui.findinghotspot


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.aossie.covidapp.databinding.ActivityFindingHotspotBinding
import com.aossie.covidapp.network.CovidApi
import com.aossie.covidapp.network.NetworkConnectionInterceptor
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.util.toast
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.aossie.covidapp.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory


class FindingHotspot : AppCompatActivity(), OnMapReadyCallback,HotspotListener{

    private lateinit var mMap: GoogleMap
    var binding:ActivityFindingHotspotBinding?=null
    var viewmodel:FindingHotspotViewModel?=null
       var  mFusedLocationClient: FusedLocationProviderClient?=null
    
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            viewmodel!!.location =mLastLocation
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =DataBindingUtil.setContentView(this, R.layout.activity_finding_hotspot)
        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = CovidApi(networkConnectionInterceptor)
        val repository = MyRepository(api)
        val factory = FindingHotspotViewModelRepository(repository)

         viewmodel  = ViewModelProviders.of(this,factory).get(FindingHotspotViewModel::class.java)
        binding!!.hotspotviewmodel =viewmodel
        viewmodel!!.hotspotListener =this

        mFusedLocationClient =LocationServices.getFusedLocationProviderClient(this)



        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }




    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setMapStyle(
            MapStyleOptions(
                resources
                    .getString(R.string.style_json)
            )
        )
        mMap = googleMap
        viewmodel!!.setActivity()

        if( !checkPermissions())
        {
            requestPermissions()
        }
        else{
            mMap.isMyLocationEnabled =true
            getLastLocation()



        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener(
                    OnCompleteListener<Location?> { task ->
                        val location = task.result
                        if (location == null) {
                            requestNewLocationData()
                            Log.d("hell",viewmodel!!.location!!.latitude.toString())
                        } else {
                            viewmodel!!.location=location
                            Log.d("hell",viewmodel!!.location!!.latitude.toString())
                        }
                    }
                )
            } else {
                toast("turn on location settings")
            }
        } else {
            requestPermissions()
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }


    override fun onSuccess(hotspotList: List<LatLng>,addresses:List<String>,location:Location) {

        mMap.clear()
        binding!!.nearestHotspotButton.text ="Find Nearest Hotspot"
        for(i in 0 until hotspotList.size-1)
        {
            mMap.addMarker(MarkerOptions().position(hotspotList[i]).title(addresses[i]))

                    }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude,location!!.longitude)))
        mMap.animateCamera( CameraUpdateFactory.zoomTo(3f) );
        binding!!.hotspotprogressBar.visibility = View.GONE
        binding!!.hotspotLinearLayout.visibility =View.VISIBLE


    }

    override fun onFailure(message: String) {
       toast(message)
    }

    override fun nearestHotspot(hotspot: LatLng, address: String,location:Location,distance:Float) {

        mMap.clear()
        mMap.addMarker(MarkerOptions().position(hotspot).title(address))
        mMap.addMarker(MarkerOptions().position(LatLng(location.latitude,location.longitude)).title("My Location").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude,location!!.longitude)))
        toast("Nearest hotspot is ${distance/1000} Km away.")
        mMap.animateCamera( CameraUpdateFactory.zoomTo(4f) );
        binding!!.nearestHotspotButton.text ="Show all hotspots"

    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false

    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
          1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if(requestCode==1)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation()
                viewmodel!!.setActivity()
                mMap.isMyLocationEnabled =true
                viewmodel!!.location =mMap.myLocation

            }
        }
    }
      fun isLocationEnabled() :Boolean{
        var locationManager: LocationManager =  getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }



}
