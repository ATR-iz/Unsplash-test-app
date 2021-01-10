package com.atriztech.unsplashtestapplication.view.collectionlist

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.future_unsplash.api.PhotoCollection
import com.atriztech.unsplashtestapplication.CustomNetwork

class AdapterCollectionList(private val openCollection: (photoCollection: PhotoCollection) -> Unit) : PagingDataAdapter<PhotoCollection, RecyclerView.ViewHolder>(NewsDiffCallback) {

    companion object {
        const val COLLECTION_VIEW_TYPE = 0
        const val ADVERTISMENT_VIEW_TYPE = 1

        val NewsDiffCallback = object : DiffUtil.ItemCallback<PhotoCollection>() {
            override fun areItemsTheSame(
                oldItem: PhotoCollection,
                newItem: PhotoCollection
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: PhotoCollection,
                newItem: PhotoCollection
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var state = MutableLiveData<CustomNetwork.NetworkState>(CustomNetwork.NetworkState.Loading)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            COLLECTION_VIEW_TYPE -> ViewHolderCollection.create(parent, openCollection)
            else -> ViewHolderAdvertisment.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(this.getItemViewType(position)){
            COLLECTION_VIEW_TYPE -> (holder as ViewHolderCollection).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return COLLECTION_VIEW_TYPE
        return when (position % 7){
            0 -> ADVERTISMENT_VIEW_TYPE
            else -> COLLECTION_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state.value is CustomNetwork.NetworkState.Loading || state.value is CustomNetwork.NetworkState.LoadError)
    }

    fun setState(state: CustomNetwork.NetworkState) {
        this.state.postValue(state)
        notifyItemChanged(super.getItemCount())
    }
}