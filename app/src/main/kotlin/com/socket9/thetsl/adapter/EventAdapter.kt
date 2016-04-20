package com.socket9.thetsl.adapter

import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.model.Model
import com.socket9.thetsl.viewgroup.EventViewGroup
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Euro (ripzery@gmail.com) on 4/19/16 AD.
 */

class EventAdapter(var eventNewsList: MutableList<Model.NewsEventEntity>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(), AnkoLogger {

    var eventInteractionListener: EventInteractionListener? = null

    /** Override method zone **/

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventViewHolder? {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_view_news_event, parent, false)
        return EventViewHolder(v)
    }

    override fun getItemCount(): Int {
        return eventNewsList.size
    }

    override fun onBindViewHolder(holder: EventViewHolder?, position: Int) {
        holder?.setModel(eventNewsList[position])
    }

    fun setListener(eventInteractionListener: EventInteractionListener){
        this.eventInteractionListener = eventInteractionListener
    }

    fun setList(eventNewsList: MutableList<Model.NewsEventEntity>){
        this.eventNewsList = eventNewsList
        notifyDataSetChanged()
    }

    /** Inner class zone **/

    inner class EventViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var eventViewGroup: EventViewGroup

        init{
            eventViewGroup = itemView?.findViewById(R.id.eventViewGroup) as EventViewGroup

            eventViewGroup.getCardClickedObservable().subscribe{
                eventInteractionListener?.onClickedEvent(adapterPosition, eventNewsList[adapterPosition])
            }
        }

        fun setModel(eventModel: Model.NewsEventEntity){
            eventViewGroup.setModel(eventModel)
        }
    }

    /** Listener zone **/
    interface EventInteractionListener {
        fun onClickedEvent(index:Int, model: Model.NewsEventEntity)
    }

}