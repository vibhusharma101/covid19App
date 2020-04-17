package com.aossie.covidapp.ui.essentials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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

class EssentialActivity : AppCompatActivity(),EssentialListener{

    var binding:ActivityEssentialBinding?=null
    var recyclerView:RecyclerView?=null
    var myAdapter:RecyclerView.Adapter<EssentialsAdapter.EssentialViewHolder>?=null
    var myLayoutManager:RecyclerView.LayoutManager?=null
    var resourceList:MutableList<ResourceData>?=null
    var stateSpinner: Spinner?=null
    var categorySpinner:Spinner?=null
    var statesList:MutableList<String>?=null
    var categoryList:MutableList<String>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_essential)
        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = CovidApi(networkConnectionInterceptor)
        val repository = MyRepository(api)
        val factory = EssentialViewModeFactory(repository)
        val viewModel = ViewModelProviders.of(this,factory).get(EssentialViewModel::class.java)
        binding!!.essentialviewmodel =viewModel
        viewModel.essentialListener =this

        recyclerView = binding!!.resourcesRecyclerView
        recyclerView!!.isNestedScrollingEnabled = true
        recyclerView!!.setHasFixedSize(true)
        myLayoutManager =LinearLayoutManager(this)
        recyclerView!!.layoutManager =myLayoutManager
        resourceList = ArrayList()
        myAdapter =EssentialsAdapter(resourceList!!)
        recyclerView!!.adapter =myAdapter
        viewModel.setActivity()

        stateSpinner =binding!!.stateSpinner
        categorySpinner = binding!!.categorySpinner

        stateSpinner!!.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                viewModel.state = statesList!![position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        categorySpinner!!.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                viewModel.description= categoryList!![position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }








    }

    override fun onSuccess(resources: List<ResourceData>) {


        resourceList!!.clear()
        resourceList!!.addAll(resources)
        myAdapter!!.notifyDataSetChanged()





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

        binding!!.essentialProgressBar.visibility =View.GONE
        binding!!.essentialLinearLayout.visibility =View.VISIBLE





    }
}
