package com.atriztech.unsplashtestapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.unsplashtestapplication.R
import kotlinx.android.synthetic.main.item_network_state.view.*
import kotlinx.android.synthetic.main.item_network_state.view.progress_bar

class AdapterLoadState(private val retry: () -> Unit) : LoadStateAdapter<AdapterLoadState.LoadStateViewHolder>() {

    companion object {
        fun create(retry: () -> Unit): AdapterLoadState {
            return AdapterLoadState(retry)
        }
    }

    class LoadStateViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        val progressBar = holder.itemView.progress_bar
        val retryButton = holder.itemView.retry_button

        progressBar.visibility = if (loadState is LoadState.Loading) VISIBLE else GONE
        retryButton.visibility = if (loadState !is LoadState.Loading) VISIBLE else GONE

        retryButton.setOnClickListener {
            retry()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_network_state, parent, false)
        )
    }
}