package com.atriztech.unsplashtestapplication.view.photolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.unsplashtestapplication.R
import com.atriztech.future_unsplash.api.Photo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ViewHolderPhoto(itemGroupView: View, private val openPhoto: (photo: Photo) -> Unit): RecyclerView.ViewHolder(itemGroupView) {

    companion object {
        fun create(parent: ViewGroup, openPhoto: (photo: Photo) -> Unit): ViewHolderPhoto {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
            return ViewHolderPhoto(view, openPhoto)
        }
    }

    private val imgImage: ImageView = itemGroupView.findViewById(R.id.item_photo)
    private val progressBar: ProgressBar = itemGroupView.findViewById(R.id.progress_bar_photo)

    fun bind(model: Photo) {
        Picasso.get()
            .load(model.urls.thumb)
            .into(imgImage, object : Callback.EmptyCallback() {
                override fun onSuccess() {
                    progressBar.visibility = View.GONE
                }
            })

        itemView.setOnClickListener {
            openPhoto(model)
        }
    }
}