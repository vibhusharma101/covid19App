package com.aossie.covidapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.aossie.covidapp.R
import com.aossie.covidapp.databinding.ActivityHomeBinding
import com.aossie.covidapp.entities.dataholders.DailyCasesInfo
import com.aossie.covidapp.network.CovidApi
import com.aossie.covidapp.network.NetworkConnectionInterceptor
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.ui.essentials.EssentialActivity
import com.aossie.covidapp.ui.findinghotspot.FindingHotspot
import com.aossie.covidapp.ui.statewise.StatewiseActivity
import com.aossie.covidapp.ui.timevariation.TrendsActivity
import com.aossie.covidapp.util.toast
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity(),HomeListener,NavigationView.OnNavigationItemSelectedListener {


    var binding:ActivityHomeBinding?=null
    var drawer: DrawerLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        drawer  =binding!!.drawerLayoutHome

        var navigationView:NavigationView =binding!!.navView
        navigationView.setNavigationItemSelectedListener(this)

        binding!!.drawerOpener.setOnClickListener{
            drawer!!.openDrawer(Gravity.LEFT)
        }


        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = CovidApi(networkConnectionInterceptor)
        val repository = MyRepository(api)
        val factory:HomeViewModelFactory = HomeViewModelFactory(repository)

        val viewModel = ViewModelProviders.of(this,factory).get(HomeViewModel::class.java)
        binding!!.homeviewmodel =viewModel
        viewModel.homeListener =this
        viewModel.setUi()
    }

    override fun onSuccess(dailyCases: DailyCasesInfo) {

        binding!!.confirmedCasesHome.text =dailyCases.totalconfirmed
        binding!!.confirmedCasesAddHome.text ="(+${dailyCases.dailyconfirmed})"
        binding!!.deceasedCasesHome.text =dailyCases.totaldeceased
        binding!!.deceasedCasesAddHome.text ="(+${dailyCases.dailydeceased})"
        binding!!.recoveredCasesHome.text =dailyCases.totalrecovered
        binding!!.recoveredCasesAddHome.text ="(+${dailyCases.dailyrecovered})"

        var activeCasesTotal :Int =dailyCases.totalconfirmed!!.toInt() - dailyCases.totalrecovered!!.toInt() - dailyCases.totaldeceased!!.toInt()
        var activeCasesDaily:Int =dailyCases.dailyconfirmed!!.toInt() - dailyCases.dailydeceased!!.toInt() - dailyCases.dailyrecovered!!.toInt()
        binding!!.activeCasesHome.text  =activeCasesTotal.toString()
        binding!!.activeCasesAddHome.text = "(+${activeCasesDaily})"

    }

    override fun onFailure(message: String) {
       toast(message)
    }

    override fun redirect() {
        startActivity(Intent(applicationContext,StatewiseActivity::class.java))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var id: Int = item.itemId;

        when (id) {
            R.id.activity_essentials -> startActivity(Intent(this, EssentialActivity::class.java))

            R.id.activity_trends ->startActivity(Intent(this, TrendsActivity::class.java))

            R.id.activity_hotspot ->startActivity(Intent(this, FindingHotspot::class.java))

        }

        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

}
