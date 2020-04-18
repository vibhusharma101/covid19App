package com.aossie.covidapp.ui.essentials

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aossie.covidapp.R
import com.aossie.covidapp.adapters.EssentialsAdapter
import com.aossie.covidapp.databinding.ActivityEssentialBinding
import com.aossie.covidapp.entities.dataholders.ResourceData
import com.aossie.covidapp.network.CovidApi
import com.aossie.covidapp.network.NetworkConnectionInterceptor
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.util.toast


class EssentialActivity : AppCompatActivity(),EssentialListener,RecyclerViewListener{

    var binding:ActivityEssentialBinding?=null
    var recyclerView:RecyclerView?=null
    var myAdapter:RecyclerView.Adapter<EssentialsAdapter.EssentialViewHolder>?=null
    var myLayoutManager:RecyclerView.LayoutManager?=null
    var resourceList:MutableList<ResourceData>?=null
    var stateSpinner: Spinner?=null
    var categorySpinner:Spinner?=null
    var statesList:MutableList<String>?=null
    var categoryList:MutableList<String>?=null
    var viewModel:EssentialViewModel?=null
    var phoneNuma:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_essential)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = CovidApi(networkConnectionInterceptor)
        val repository = MyRepository(api)
        val factory = EssentialViewModeFactory(repository)
         viewModel = ViewModelProviders.of(this,factory).get(EssentialViewModel::class.java)
        binding!!.essentialviewmodel =viewModel
        viewModel!!.essentialListener =this

        recyclerView = binding!!.resourcesRecyclerView
        recyclerView!!.isNestedScrollingEnabled = true
        recyclerView!!.setHasFixedSize(true)
        myLayoutManager =LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)


        recyclerView!!.layoutManager =myLayoutManager

        resourceList = ArrayList()
        myAdapter = EssentialsAdapter(resourceList!!,this)

        recyclerView!!.adapter =myAdapter
        viewModel!!.setActivity()
        stateSpinner =binding!!.stateSpinner
        categorySpinner = binding!!.categorySpinner






    }

    override fun onSuccess(resources: List<ResourceData>) {


        resourceList!!.clear()
        resourceList!!.addAll(resources)
        myAdapter!!.notifyDataSetChanged()
        binding!!.essentialShimmerLayout.visibility =View.GONE
        binding!!.essentialLinearLayout.visibility =View.VISIBLE
        /* for(i in 0 until resources.size-1) {

            resourceList!!.add(resources[i])
            myAdapter!!.notifyDataSetChanged()

        }
        myAdapter!!.notifyDataSetChanged()*/


    }

    override fun onFailure(message: String) {

        resourceList!!.clear()
        myAdapter!!.notifyDataSetChanged()
       toast(message)
    }

    override fun giveEntries(states: List<String>, categories: List<String>) {


        statesList =ArrayList()
        statesList!!.addAll(states)

        categoryList =ArrayList()
        categoryList!!.addAll(categories)

        var stateAdapter:ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,statesList!!)
        stateSpinner!!.adapter =stateAdapter


        var categoryAdapter:ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,categoryList!!)
        categorySpinner!!.adapter =categoryAdapter

        binding!!.essentialShimmerLayout.visibility =View.GONE
        binding!!.essentialLinearLayout.visibility =View.VISIBLE

    }

    override fun pause() {


        recyclerView!!.viewTreeObserver.addOnGlobalLayoutListener(
                object:ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        recyclerView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        binding!!.essentialShimmerLayout.visibility =View.GONE
                        binding!!.essentialLinearLayout.visibility =View.VISIBLE
                        Log.d("hello","Hi")
                    }
                }
                    )
        binding!!.essentialShimmerLayout.visibility =View.VISIBLE
        binding!!.essentialLinearLayout.visibility =View.GONE
        viewModel!!.state =stateSpinner!!.selectedItem.toString()
        viewModel!!.description =categorySpinner!!.selectedItem.toString()
    }

    override fun back() {
        super.onBackPressed()
    }

    override fun recyclerViewListClicked(phoneNum:String) {

        phoneNuma =phoneNum
        if(phoneNum.length==10) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),1)

            } else {
                var intent: Intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:" + "+91${phoneNum}")
                startActivity(intent)
            }

        }
        else{
            toast("Wrong format of Phone num")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(requestCode==1)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                var intent: Intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:" + "+91${phoneNuma}")

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    toast("Not Granted permission")
                }
                else {
                    startActivity(intent)
                }
            }
            else{
                toast("Not Granted permisssion")
            }
        }





    }


}
