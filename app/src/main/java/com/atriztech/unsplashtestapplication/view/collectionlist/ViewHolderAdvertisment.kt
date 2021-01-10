package com.atriztech.unsplashtestapplication.view.collectionlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.unsplashtestapplication.R

class ViewHolderAdvertisment(itemGroupView: View): RecyclerView.ViewHolder(itemGroupView){
    companion object {
        fun create(parent: ViewGroup): ViewHolderAdvertisment {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_advertisement, parent, false)
            return ViewHolderAdvertisment(view)
        }
    }
}