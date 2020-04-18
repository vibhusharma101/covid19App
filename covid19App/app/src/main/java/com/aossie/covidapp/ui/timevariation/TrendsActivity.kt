package com.aossie.covidapp.ui.timevariation

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.aossie.covidapp.R
import com.aossie.covidapp.databinding.ActivityTrendsBinding
import com.aossie.covidapp.network.CovidApi
import com.aossie.covidapp.network.NetworkConnectionInterceptor
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.ui.home.HomeViewModelFactory
import com.aossie.covidapp.util.toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_trends.*

class TrendsActivity : AppCompatActivity(),TrendListener {

    var binding:ActivityTrendsBinding?=null
    var stateSpinner: Spinner?=null
    var statesList:MutableList<String>?=null
    var pIn = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_trends)

        requestedOrientation =ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = CovidApi(networkConnectionInterceptor)
        val repository = MyRepository(api)
        val factory: TrendsActivityViewModelFactory= TrendsActivityViewModelFactory(repository)
        val viewModel = ViewModelProviders.of(this,factory).get(TrendsActivityViewModel::class.java)
        binding!!.trendviewmodel =viewModel
        viewModel.trendListener =this
        viewModel.setUI()

        stateSpinner =binding!!.stateSpinnerTrends

        stateSpinner!!.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if(!(position==pIn)) {
                    viewModel.setStateWise(statesList!![position])
                    binding!!.trendProgressBar.visibility = View.GONE
                    pIn =position
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }






    }

    override fun onSuccess(
        listC: List<LineDataSet>,
        listD: List<LineDataSet>,
        listR: List<LineDataSet>
    ,conf:String,death:String,recovered:String) {

        var chartConfirmed:LineChart =binding!!.confirmedLieChart
        chartConfirmed.data = LineData(listC)
        chartConfirmed.setDrawGridBackground(false)
        chartConfirmed.axisLeft.setDrawGridLines(false)
        chartConfirmed.xAxis.setDrawGridLines(false)
        chartConfirmed.axisRight.setDrawGridLines(false)
        chartConfirmed.axisLeft.isEnabled =false;
        chartConfirmed.invalidate()
        binding!!.confirmedCasesTrenTextView.text ="(+${conf})"


        var chartDeaths:LineChart = binding!!.deathChart
        chartDeaths.data =LineData(listD)
        chartDeaths.setDrawGridBackground(false)
        chartDeaths.axisLeft.setDrawGridLines(false)
        chartDeaths.xAxis.setDrawGridLines(false)
        chartDeaths.axisRight.setDrawGridLines(false)
        chartDeaths.axisLeft.isEnabled =false;
        chartDeaths.invalidate()
        binding!!.deathTrendTextView.text  = "{+${death})"


        var chartRecovered:LineChart =binding!!.recoveredCasesChart
        chartRecovered.data = LineData(listR)
        chartRecovered.setDrawGridBackground(false)
        chartRecovered.axisLeft.setDrawGridLines(false)
        chartRecovered.xAxis.setDrawGridLines(false)
        chartRecovered.axisRight.setDrawGridLines(false)
        chartRecovered.axisLeft.isEnabled =false;
        chartRecovered.invalidate()
        binding!!.recoveredCasesTrend.text ="{+${recovered})"

        binding!!.trendProgressBar.visibility =View.GONE
        binding!!.trendLinearLayout.visibility =View.VISIBLE


    }

    override fun setSpinner(statelist: List<String>) {
        statesList =ArrayList()
        statesList!!.addAll(statelist)
        var stateAdapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,statesList!!)
        stateSpinner!!.adapter =stateAdapter
        binding!!.trendProgressBar.visibility =View.GONE
        binding!!.trendLinearLayout.visibility =View.VISIBLE



    }

    override fun onFailure(message: String) {
       toast(message)
    }

    override fun back() {
        super.onBackPressed()
    }
}
