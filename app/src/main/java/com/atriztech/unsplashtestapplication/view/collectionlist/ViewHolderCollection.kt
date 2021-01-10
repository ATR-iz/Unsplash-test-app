package com.atriztech.unsplashtestapplication.view.collectionlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.unsplashtestapplication.R
import com.atriztech.future_unsplash.api.PhotoCollection
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ViewHolderCollection(view: View, private val openCollection: (photoCollection: PhotoCollection) -> Unit) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup, openCollection: (photoCollection: PhotoCollection) -> Unit): ViewHolderCollection {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
            return ViewHolderCollection(view, openCollection)
        }
    }

    private val txtName: TextView = itemView.findViewById(R.id.collection_name)
    private val txtDescription: TextView = itemView.findViewById(R.id.collection_description)
    private val txtCountPhoto: TextView = itemView.findViewById(R.id.collection_count_photo)
    private val imgImage: ImageView = itemView.findViewById(R.id.collection_image)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar_collection)


    fun bind(photoCollection: PhotoCollection?) {
        if (photoCollection != null) {
            txtName.text = photoCollection.title
            txtDescription.text = photoCollection.description
            txtCountPhoto.text = "${photoCollection.total_photos} photos"
            Picasso.get()
                .load(photoCollection.cover_photo.urls.small)
                .into(imgImage, object : Callback.EmptyCallback() {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }
                })

            itemView.setOnClickListener {
                openCollection(photoCollection)
            }
        }
    }
}