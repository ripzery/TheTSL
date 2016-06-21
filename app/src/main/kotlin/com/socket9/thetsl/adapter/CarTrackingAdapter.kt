package com.socket9.thetsl.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.viewgroups.CarTrackingViewGroup

/**
 * Created by Euro (ripzery@gmail.com) on 4/27/16 AD.
 */
class CarTrackingAdapter(var carTrackingList: MutableList<Model.CarTrackingEntity>) : RecyclerView.Adapter<CarTrackingAdapter.CarTrackingViewHolder>() {

    var carTrackingInteractionListener: CarTrackingInteractionListener? = null

    override fun onBindViewHolder(holder: CarTrackingViewHolder?, position: Int) {
        holder?.setModel(carTrackingList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CarTrackingViewHolder? {
        val v:View = LayoutInflater.from(parent?.context).inflate(R.layout.item_view_car_tracking, parent!!, false)
        return CarTrackingViewHolder(v)
    }

    override fun getItemCount(): Int {
        return carTrackingList.size
    }

    fun setListener(carTrackingInteractionListener: CarTrackingInteractionListener){
        this.carTrackingInteractionListener = carTrackingInteractionListener
    }

    /** Listener zone **/

    interface CarTrackingInteractionListener {
        fun onCardClicked(position:Int)
    }

    /** Inner class zone **/

    inner class CarTrackingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var carTrackingViewGroup: CarTrackingViewGroup? = null

        init{
            carTrackingViewGroup = itemView.findViewById(R.id.carTrackingViewGroup) as CarTrackingViewGroup

            carTrackingViewGroup?.getCardClickedObservable()?.subscribe {
                carTrackingInteractionListener?.onCardClicked(adapterPosition)
            }
        }

        fun setModel(carTrackingEntity: Model.CarTrackingEntity) {
            carTrackingViewGroup?.setModel(carTrackingEntity)
        }
    }
}