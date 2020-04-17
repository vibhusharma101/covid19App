package com.aossie.covidapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aossie.covidapp.R
import com.aossie.covidapp.entities.dataholders.ResourceData
import kotlinx.android.synthetic.main.essential_single.view.*

class EssentialsAdapter(var essentialList:List<ResourceData>) :RecyclerView.Adapter<EssentialsAdapter.EssentialViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EssentialViewHolder {
        var li: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var essentialView: View = li.inflate(R.layout.essential_single, parent, false)
        return EssentialsAdapter.EssentialViewHolder(essentialView)
    }

    override fun getItemCount(): Int {
       return essentialList!!.size
    }

    override fun onBindViewHolder(holder: EssentialViewHolder, position: Int) {

        holder.cityTextView.text = essentialList[position].city
        holder.categoryTextView.text = essentialList[position].category
        holder.descriptionTextView.text = essentialList[position].descriptionandorserviceprovided
        holder.organizationTextView.text = essentialList[position].nameoftheorganisation
        holder.phoneTextView.text =essentialList[position].phonenumber
    }



    class EssentialViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var cityTextView: TextView = itemView.cityEssential
        var descriptionTextView:TextView = itemView.descriptionEssential
        var categoryTextView:TextView = itemView.categoryEssential
        var organizationTextView :TextView =itemView.orgEssentials
        var phoneTextView :TextView = itemView.phoneEssential


    }


}