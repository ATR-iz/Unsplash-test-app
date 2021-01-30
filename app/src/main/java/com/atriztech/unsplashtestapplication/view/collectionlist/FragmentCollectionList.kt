package com.atriztech.unsplashtestapplication.view.collectionlist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.unsplashtestapplication.R
import com.atriztech.unsplashtestapplication.databinding.FragmentCollectionListBinding
import com.atriztech.unsplashtestapplication.di.App
import com.atriztech.unsplashtestapplication.view.AdapterLoadState
import com.atriztech.unsplashtestapplication.view.photolist.FragmentPhotoList
import com.atriztech.unsplashtestapplication.viewmodel.ViewModelListCollections
import com.atriztech.future_unsplash.api.PhotoCollection
import com.atriztech.unsplashtestapplication.CustomNetwork
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentCollectionList : Fragment() {

    private lateinit var binding: FragmentCollectionListBinding
    @Inject lateinit var viewModelListCollections: ViewModelListCollections
    private lateinit var recyclerView: AdapterCollectionList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized ) {
            App.component().inject(this)

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection_list, container, false)

            viewModelListCollections.getCollectionList()

            viewModelListCollections.title.observe(this.requireActivity(), {
                this.requireActivity().title = getString(R.string.find_collections) + it
            })

            initRecyclerView()
            initSwipe()
            initNetworkState()
        }

        if (viewModelListCollections.title.value == "") viewModelListCollections.title.postValue(getString(R.string.app_name))

        return binding.root
    }

    private fun initRecyclerView(){
        recyclerView = AdapterCollectionList {openCollection(it)}

        binding.listCollections.layoutManager = LinearLayoutManager(
            this.requireContext(),
            RecyclerView.VERTICAL,
            false
        )

        binding.listCollections.adapter = recyclerView.withLoadStateFooter(
            footer = AdapterLoadState { recyclerView.retry() }
        )

        viewModelListCollections.collectionsList.observe(this.requireActivity(), {
            lifecycleScope.launch {
                recyclerView.submitData(it)
            }
        })

        recyclerView.refresh()
    }

    private fun initSwipe(){
        binding.swipeUpdateListCollections.setOnRefreshListener {
            lifecycleScope.launch {
                viewModelListCollections.customNetwork.state.postValue(CustomNetwork.NetworkState.Loading)
                recyclerView.refresh()
                viewModelListCollections.customNetwork.state.postValue(CustomNetwork.NetworkState.Loaded)
            }
        }
    }

    private fun initNetworkState(){
        viewModelListCollections.customNetwork.state.observe(this.requireActivity(), {
            when (it) {
                is CustomNetwork.NetworkState.Loading -> {}
                is CustomNetwork.NetworkState.Loaded -> {
                    binding.swipeUpdateListCollections.isRefreshing = false
                }
                is CustomNetwork.NetworkState.LoadError -> {
                    Toast.makeText(this.requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    binding.swipeUpdateListCollections.isRefreshing = false
                }
            }
        })
    }

    private fun openCollection(item: PhotoCollection){
        val bundle = FragmentPhotoList.bundleFor(type = FragmentPhotoList.KEY_TYPE_COLLECTION_PHOTO, collection = item)
        this.findNavController().navigate(R.id.action_mainFragment_to_listPhotoFragment, bundle)
    }
}