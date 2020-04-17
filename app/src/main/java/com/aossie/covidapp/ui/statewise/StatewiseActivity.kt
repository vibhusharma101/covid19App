package com.aossie.covidapp.ui.statewise

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.aossie.covidapp.R
import com.aossie.covidapp.databinding.ActivityStatewiseBinding
import com.aossie.covidapp.entities.dataholders.StatewiseCase
import com.aossie.covidapp.network.CovidApi
import com.aossie.covidapp.network.NetworkConnectionInterceptor
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.ui.home.HomeViewModel
import com.aossie.covidapp.util.toast

class StatewiseActivity : AppCompatActivity(),StateDataListener{


    var binding:ActivityStatewiseBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_statewise)
        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = CovidApi(networkConnectionInterceptor)
        val repository = MyRepository(api)
        var factory:StateWiseViewModelFactory = StateWiseViewModelFactory(repository)
        val viewmodel = ViewModelProviders.of(this,factory).get(StatewiseViewModel::class.java)
        binding!!.stateviewmodel =viewmodel
        viewmodel.stateDataListener =this
        viewmodel.setUi()



    }

    override fun onSuccess(stateList: List<StatewiseCase>) {

        var table:TableLayout = TableLayout(applicationContext)
        table.layoutParams  = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        table.isShrinkAllColumns =true
        table.isStretchAllColumns  =true


        var row:TableRow = TableRow(this)


        var ta :TextView = TextView(applicationContext)
        var tb :TextView = TextView(applicationContext)
        var tc :TextView = TextView(applicationContext)
        var td :TextView = TextView(applicationContext)
        var te:TextView = TextView(applicationContext)
        ta.text ="State"
        ta.setTypeface(null,Typeface.BOLD)
        tb.text ="Co"
        tb.setTextColor(resources.getColor(R.color.totalCasesColor))
        tc.text ="Ac"
        tc.setTextColor(resources.getColor(R.color.activeCasesColor))
        td.text ="Re"
        td.setTextColor(resources.getColor(R.color.recoveredColor))
        te.text ="De"
        te.setTextColor(resources.getColor(R.color.deathColor))

        row.addView(ta)
        row.addView(tb)
        row.addView(tc)
        row.addView(td)
        row.addView(te)
        table.addView(row)

        for(i in 1 until stateList.size-1)
        {
                var row :TableRow = TableRow(this)

                var ta :TextView = TextView(applicationContext)
                var tb :TextView = TextView(applicationContext)
                var tc :TextView = TextView(applicationContext)
                var td :TextView = TextView(applicationContext)
                var te:TextView = TextView(applicationContext)
            ta.text =stateList[i].state
            ta.setTypeface(null,Typeface.BOLD)
            tb.text =stateList[i].confirmed
            tb.setTextColor(resources.getColor(R.color.totalCasesColor))
            tc.text =stateList[i].active
            tc.setTextColor(resources.getColor(R.color.activeCasesColor))
            td.text =stateList[i].recovered
            td.setTextColor(resources.getColor(R.color.recoveredColor))
            te.text =stateList[i].deaths
            te.setTextColor(resources.getColor(R.color.deathColor))

            row.addView(ta)
            row.addView(tb)
            row.addView(tc)
            row.addView(td)
            row.addView(te)
            table.addView(row)
        }

        binding!!.stateTableLayout.addView(table)




    }

    override fun onFailure(message: String) {
       toast(message)
    }
}
