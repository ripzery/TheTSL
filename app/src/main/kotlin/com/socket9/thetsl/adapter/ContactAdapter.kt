package com.socket9.thetsl.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.model.Model
import com.socket9.thetsl.viewgroup.ContactViewGroup

/**
 * Created by Euro (ripzery@gmail.com) on 4/20/16 AD.
 */

class ContactAdapter(var contactList: MutableList<Model.ContactEntity>, var contactListener: ContactInteractionListener? = null) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {


    /** Override method zone **/

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder?, position: Int) {
        holder?.contactView?.setModel(contactList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContactViewHolder? {
        val v:View = LayoutInflater.from(parent?.context).inflate(R.layout.item_view_contact, parent, false)

        return ContactViewHolder(v)
    }


    /** Interface zone **/
    interface ContactInteractionListener{
        fun onContactClicked(index: Int, model: Model.ContactEntity)
    }


    /** Inner class zone **/

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /** Variable zone **/
        val contactView: ContactViewGroup

        init{
            contactView = itemView.findViewById(R.id.contactViewGroup) as ContactViewGroup

            contactView.getRowClickedObservable().subscribe {
                contactListener?.onContactClicked(adapterPosition, contactList[adapterPosition])
            }
        }
    }
}