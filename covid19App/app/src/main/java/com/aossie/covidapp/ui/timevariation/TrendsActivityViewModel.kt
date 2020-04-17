package com.aossie.covidapp.ui.timevariation

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import com.aossie.covidapp.entities.Responses.DailyCasesResponse
import com.aossie.covidapp.entities.Responses.StateCasesResponses
import com.aossie.covidapp.entities.Responses.StateDailyResponse
import com.aossie.covidapp.entities.Responses.VariationResponse
import com.aossie.covidapp.entities.dataholders.StateDaily
import com.aossie.covidapp.repository.MyRepository
import com.aossie.covidapp.util.Coroutine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

class TrendsActivityViewModel(private val repo:MyRepository) :ViewModel() {

var trendListener :TrendListener?=null
    var statelist:MutableList<String>?=null
    var statecodeList:MutableList<String>?=null

    fun setUI()
    {
        statelist =ArrayList()
        statecodeList =ArrayList()
        statelist!!.add("All")
        statecodeList!!.add("All")
        setAll()

    }

    fun setAll(){

        Coroutine.main {

            val trendResponse:DailyCasesResponse = repo.getDailyCases()
            val stateResponse:StateCasesResponses =repo.getStateCases()

            trendResponse.dailyCases.let {

                val dataC: ArrayList<Entry>  = ArrayList()
                val dataD :ArrayList<Entry> = ArrayList()
                val dataR:ArrayList<Entry> = ArrayList()

                for(i in 0 until it.size-1)
                {
                    dataC.add(Entry(i.plus(0.0f),it[i].totalconfirmed!!.toFloat()))
                    dataD.add(Entry(i.plus(0.0f),it[i].totaldeceased!!.toFloat()))
                    dataR.add(Entry(i.plus(0.0f),it[i].totalrecovered!!.toFloat()))
                }

                val  aDataC :ArrayList<LineDataSet> = ArrayList()
                val dataSetC:LineDataSet = LineDataSet(dataC,"Confirmed Cases")
                dataSetC.setDrawCircles(false)
                dataSetC.lineWidth =3f
                dataSetC.valueTextSize =0f
                dataSetC.setColor(Color.RED)
                aDataC.add(dataSetC)

                val aDataD:ArrayList<LineDataSet> = ArrayList()
                val dataSetD:LineDataSet = LineDataSet(dataD,"Total Deaths")
                dataSetD.setDrawCircles(false)
                dataSetD.lineWidth =3f
                dataSetD.valueTextSize=0f
                dataSetD.setColor(Color.DKGRAY)
                aDataD.add(dataSetD)

                val aDataR:ArrayList<LineDataSet> = ArrayList()
                val dataSetR:LineDataSet = LineDataSet(dataR,"Total Recovered")
                dataSetR.setDrawCircles(false)
                dataSetR.lineWidth =3f
                dataSetR.valueTextSize = 0f
                dataSetR.setColor(Color.GREEN)
                aDataR.add(dataSetR)

                trendListener!!.onSuccess(aDataC,aDataD,aDataR,it[it.size-1].dailyconfirmed!!,it[it.size-1].dailydeceased!!,it[it.size-1].dailyrecovered!!)
            }

            stateResponse.statecases.let {

                for(i in 1 until it.size-1)
                {
                    statelist!!.add(it[i].state!!)
                    if(it[i]!!.statecode.equals("as")|| it[i]!!.statecode.equals("br"))
                    {
                        if(it[i]!!.statecode.equals("as"))
                        {
                            statecodeList!!.add("ass")
                        }
                        else
                        {
                            statecodeList!!.add("bih")
                        }

                    }
                    else {
                        statecodeList!!.add(it[i].statecode!!)
                    }
                }
                trendListener!!.setSpinner(statelist!!)

            }





        }

    }

    fun setStateWise(state:String) {

        Log.d("select ",state)
        if (state.equals("All")) {
            setAll()
        } else {

            var indexOfCode = statelist!!.indexOf(state)

            if (indexOfCode != -1) {
                var code: String = statecodeList!![indexOfCode]

                Coroutine.main {

                    val trendStateResponse: StateDailyResponse = repo.getStateDailyData()

                    trendStateResponse.dailyStateCases.let {

                        val dataC: ArrayList<Entry> = ArrayList()
                        val dataD: ArrayList<Entry> = ArrayList()
                        val dataR: ArrayList<Entry> = ArrayList()


                        var totalC: Float = 0.0f
                        var totalD: Float = 0.0f
                        var totalR: Float = 0.0f
                        var dC: String = "0.0"
                        var dD: String = "0.0"
                        var dR: String = "0.0"

                        for (i in 0 until it.size - 1) {


                            Log.d("Code",code)
                            if (it[i].status.equals("Confirmed")) {

                                dC = getStateCodeData(it[i], code)
                                if(dC.equals(""))
                                {
                                    dC ="0.0"
                                }

                                totalC += dC.toFloat()

                                dataC.add(Entry(i.plus(0.0f), totalC))
                            } else if (it[i].status.equals("Recovered")) {

                                dR = getStateCodeData(it[i], code)
                                if(dR.equals(""))
                                {
                                    dR ="0.0"
                                }
                                totalR += dR.toFloat()
                                dataR.add(Entry(i.plus(0.0f), totalR))

                            } else if (it[i].status.equals("Deceased")) {

                                dD = getStateCodeData(it[i], code)
                                if(dD.equals(""))
                                {
                                    dD ="0.0"
                                }
                                totalD += dD.toFloat()
                                dataD.add(Entry(i.plus(0.0f), totalD))
                            }


                        }

                        val aDataC: ArrayList<LineDataSet> = ArrayList()
                        val dataSetC: LineDataSet = LineDataSet(dataC, "Confirmed Cases")
                        dataSetC.setDrawCircles(false)
                        dataSetC.lineWidth = 3f
                        dataSetC.valueTextSize = 0f
                        dataSetC.setColor(Color.RED)
                        aDataC.add(dataSetC)


                        val aDataD: ArrayList<LineDataSet> = ArrayList()
                        val dataSetD: LineDataSet = LineDataSet(dataD, "Total Deaths")
                        dataSetD.setDrawCircles(false)
                        dataSetD.lineWidth = 3f
                        dataSetD.valueTextSize = 0f
                        dataSetD.setColor(Color.DKGRAY)
                        aDataD.add(dataSetD)

                        val aDataR: ArrayList<LineDataSet> = ArrayList()
                        val dataSetR: LineDataSet = LineDataSet(dataR, "Total Recovered")
                        dataSetR.setDrawCircles(false)
                        dataSetR.lineWidth = 3f
                        dataSetR.valueTextSize = 0f
                        dataSetR.setColor(Color.GREEN)
                        aDataR.add(dataSetR)

                        trendListener!!.onSuccess(
                            aDataC,
                            aDataD,
                            aDataR,
                            dC,
                            dD,
                            dR
                        )
                    }

                }
            } else {
                trendListener!!.onFailure("Failed")
            }


        }

    }



    fun getStateCodeData(dailyclass:StateDaily,code:String):String
    {


        when(code.toLowerCase())
        {
            "an"->  return dailyclass.an!!
        "ap"-> return dailyclass.ap!!
        "ar" -> return dailyclass.ar!!
        "ass" -> return dailyclass.ass!!
        "bih" -> return dailyclass.bih!!
        "ch"  -> return dailyclass.ch!!
        "ct"   -> return dailyclass.ct!!
        "dd" -> return dailyclass.dd!!
        "dl"  -> return dailyclass.dl!!
        "dn"  -> return dailyclass.dn!!
        "ga"   -> return dailyclass.ga!!
        "gj" -> return dailyclass.gj!!
        "hp" -> return dailyclass.hp!!
        "hr" -> return dailyclass.hr!!
        "jh" -> return dailyclass.jh!!
        "jk" -> return dailyclass.jk!!
        "ka"-> return dailyclass.ka!!
        "kl"-> return dailyclass.kl!!
        "la" -> return dailyclass.la!!
        "ld" -> return dailyclass.ld!!
        "mh"-> return dailyclass.mh!!
        "ml"->return  dailyclass.ml!!
        "mn" -> return dailyclass.mn!!
        "mp"-> return dailyclass.mp!!
        "mz" -> return dailyclass.mz!!
        "nl" -> return dailyclass.nl!!
        "or" -> return dailyclass.or!!
        "pb" -> return dailyclass.pb!!
        "py" -> return dailyclass.py!!
        "rj" -> return dailyclass.rj!!
        "sk" -> return dailyclass.sk!!
        "tg" -> return dailyclass.tg!!
        "tn" -> return dailyclass.tn!!
        "tr" -> return dailyclass.tr!!
        "tt" -> return dailyclass.tt!!
        "up" -> return dailyclass.up!!
        "ut" -> return dailyclass.ut!!
        "wb" -> return dailyclass.wb!!
        }


return dailyclass.an!!;

    }


}

