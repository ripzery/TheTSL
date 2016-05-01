package com.socket9.thetsl.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.viewgroups.ServiceDetailViewGroup

/**
 * Created by Euro (ripzery@gmail.com) on 4/29/16 AD.
 */

class ServiceDetailAdapter(var serviceTrackingStatus: MutableList<Model.ServiceTrackingStatus>) : RecyclerView.Adapter<ServiceDetailAdapter.ServiceDetailViewHolder>(){

    override fun getItemCount(): Int {
        return serviceTrackingStatus.size
    }

    override fun onBindViewHolder(holder: ServiceDetailViewHolder?, position: Int) {
        holder?.setModel(serviceTrackingStatus[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ServiceDetailViewHolder? {
        val v:View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_view_service_detail, parent, false)

        return ServiceDetailViewHolder(v)
    }

    inner class ServiceDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var serviceDetailViewGroup : ServiceDetailViewGroup? = null

        init{
            serviceDetailViewGroup = itemView.findViewById(R.id.serviceDetailViewGroup) as ServiceDetailViewGroup
        }

        fun setModel(model: Model.ServiceTrackingStatus){
            serviceDetailViewGroup?.setModel(model)
        }

    }

}