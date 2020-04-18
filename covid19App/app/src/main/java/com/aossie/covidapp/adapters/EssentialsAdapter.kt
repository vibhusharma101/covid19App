package com.aossie.covidapp.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aossie.covidapp.R
import com.aossie.covidapp.entities.dataholders.ResourceData
import com.aossie.covidapp.ui.essentials.RecyclerViewListener
import kotlinx.android.synthetic.main.essential_single.view.*

class EssentialsAdapter(var essentialList:List<ResourceData>,var itemListener:RecyclerViewListener) :RecyclerView.Adapter<EssentialsAdapter.EssentialViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EssentialViewHolder {


        var essentialView: View = LayoutInflater.from(parent.context).inflate(R.layout.essential_single, parent, false)
        return EssentialsAdapter.EssentialViewHolder(essentialView)
    }

    override fun getItemCount(): Int {
       return essentialList.size
    }

    override fun onBindViewHolder(holder: EssentialViewHolder, position: Int) {

       holder.bindItems(essentialList[position],itemListener)
    }



    class EssentialViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
         fun bindItems(data:ResourceData,itemListener: RecyclerViewListener) {

             Log.d("binding","binding")
             var cityTextView: TextView  = itemView.cityEssential
             cityTextView.text =data.city
             var descriptionTextView: TextView = itemView.descriptionEssential
             descriptionTextView.text =data.descriptionandorserviceprovided
             var categoryTextView: TextView  = itemView.categoryEssential
             categoryTextView.text =data.category
             var organizationTextView: TextView = itemView.orgEssentials
             organizationTextView.text =data.nameoftheorganisation
             var phoneTextView: TextView  = itemView.phoneEssential
             phoneTextView.text =data.phonenumber
             phoneTextView.setOnClickListener{
                 itemListener.recyclerViewListClicked(data.phonenumber!!)
             }
        }




    }


}