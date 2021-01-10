package com.atriztech.unsplashtestapplication.view.photolist

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.unsplashtestapplication.CustomNetwork

class AdapterPhotoList(private val openPhoto: (photo: Photo) -> Unit) : PagingDataAdapter<Photo, RecyclerView.ViewHolder>(NewsDiffCallback) {

    companion object {
        const val PHOTO_VIEW_TYPE = 0

        val NewsDiffCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(
                oldItem: Photo,
                newItem: Photo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Photo,
                newItem: Photo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var state: CustomNetwork.NetworkState = CustomNetwork.NetworkState.Loaded

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderPhoto.create(parent, openPhoto)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderPhoto).bind(getItem(position)!!)
    }

    override fun getItemViewType(position: Int): Int {
        return PHOTO_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state is CustomNetwork.NetworkState.Loading || state is CustomNetwork.NetworkState.LoadError)
    }

    fun setState(state: CustomNetwork.NetworkState) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}