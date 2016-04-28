package com.socket9.thetsl.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.viewgroups.ServiceViewGroup

/**
 * Created by Euro (ripzery@gmail.com) on 4/27/16 AD.
 */
class ServiceAdapter(var serviceList: MutableList<Model.ServiceBookingEntity>) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    var serviceInteractionListener: ServiceInteractionListener? = null

    override fun onBindViewHolder(holder: ServiceViewHolder?, position: Int) {
        holder?.setModel(serviceList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ServiceViewHolder? {
        val v:View = LayoutInflater.from(parent?.context).inflate(R.layout.item_view_service, parent!!, false)
        return ServiceViewHolder(v)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    fun setListener(serviceInteractionListener: ServiceInteractionListener){
        this.serviceInteractionListener = serviceInteractionListener
    }

    /** Listener zone **/

    interface ServiceInteractionListener {
        fun onCardClicked(position:Int)
    }

    /** Inner class zone **/

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var serviceViewGroup: ServiceViewGroup? = null

        init{
            serviceViewGroup = itemView.findViewById(R.id.serviceViewGroup) as ServiceViewGroup

            serviceViewGroup?.getCardClickedObservable()?.subscribe {
                serviceInteractionListener?.onCardClicked(position)
            }
        }

        fun setModel(serviceBookingEntity: Model.ServiceBookingEntity) {
            serviceViewGroup?.setModel(serviceBookingEntity)
        }
    }
}