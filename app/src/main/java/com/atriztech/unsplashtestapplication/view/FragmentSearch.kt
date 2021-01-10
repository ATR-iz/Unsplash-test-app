package com.atriztech.unsplashtestapplication.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.atriztech.unsplashtestapplication.R
import com.atriztech.unsplashtestapplication.databinding.FragmentSearchBinding
import com.atriztech.unsplashtestapplication.di.App
import com.atriztech.unsplashtestapplication.view.collectionlist.AdapterCollectionList
import com.atriztech.unsplashtestapplication.view.photolist.AdapterPhotoList
import com.atriztech.unsplashtestapplication.view.photolist.FragmentPhotoList
import com.atriztech.unsplashtestapplication.viewmodel.ViewModelListCollections
import com.atriztech.unsplashtestapplication.viewmodel.ViewModelListPhoto
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.future_unsplash.api.PhotoCollection
import com.atriztech.future_unsplash.api.UnsplashService
import com.atriztech.unsplashtestapplication.CustomFragment
import com.atriztech.unsplashtestapplication.CustomNetwork
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentSearch : CustomFragment() {

    @Inject lateinit var unsplashService: UnsplashService
    @Inject lateinit var customNetwork: CustomNetwork

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: ViewModel
    private lateinit var adapterPhotoList: AdapterPhotoList
    private lateinit var adapterCollectionList: AdapterCollectionList

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupActionBar(isEnabledOptionMenu = true, isEnabledBackButton = true, isHideActionBar = false)

        if (!this::binding.isInitialized ) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container,false)
            App.component().inject(this)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchManager = this.requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.menu_search_button).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.requireActivity().componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                when (viewModel){
                    is ViewModelListPhoto -> searchPhoto(p0!!)
                    is ViewModelListCollections -> searchCollection(p0!!)
                }

                searchView.setQuery("", true)
                searchView.isIconified = true
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        if (!this::viewModel.isInitialized){
            setupSearchPhoto()
        }

        when (viewModel){
            is ViewModelListPhoto -> searchView.queryHint = getString(R.string.search_hint_search_photo)
            is ViewModelListCollections -> searchView.queryHint = getString(R.string.search_hint_search_collection)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search_in_photo -> setupSearchPhoto()
            R.id.menu_search_in_collection -> setupSearchCollection()
            android.R.id.home -> this.requireActivity().onBackPressed()
        }
        return true
    }

    private fun setupSearchPhoto(){
        viewModel = ViewModelListPhoto(unsplashService, customNetwork)
        adapterPhotoList = AdapterPhotoList {openPhoto(it)}

        binding.listSearchItems.layoutManager = GridLayoutManager(this.requireContext(), 3)
        binding.listSearchItems.adapter = adapterPhotoList.withLoadStateFooter(
            footer = AdapterLoadState { adapterPhotoList.retry() }
        )

        (viewModel as ViewModelListPhoto).title.observe(this.requireActivity(), {
            this.requireActivity().title = it
        })

        searchView.queryHint = getString(R.string.search_hint_search_photo)
        searchView.onActionViewCollapsed()
        searchView.onActionViewExpanded()
    }

    private fun setupSearchCollection(){
        viewModel = ViewModelListCollections(unsplashService, customNetwork)
        adapterCollectionList = AdapterCollectionList {openCollection(it)}

        binding.listSearchItems.layoutManager = LinearLayoutManager(this.requireContext())

        binding.listSearchItems.adapter = adapterCollectionList.withLoadStateFooter(
            footer = AdapterLoadState { adapterCollectionList.retry() }
        )

        (viewModel as ViewModelListCollections).title.observe(this.requireActivity(), {
            this.requireActivity().title = it
        })

        searchView.queryHint = getString(R.string.search_hint_search_collection)
        searchView.onActionViewCollapsed()
        searchView.onActionViewExpanded()
    }

    private fun searchPhoto(currentQuery: String){
        (viewModel as ViewModelListPhoto).findPhoto(currentQuery)

        (viewModel as ViewModelListPhoto).listPhoto.observe(this.requireActivity(), {
            lifecycleScope.launch {
                adapterPhotoList.submitData(it)
            }
        })

        customNetwork.state.observe(this.requireActivity(),{
            adapterPhotoList.setState(it)
        })
    }

    private fun searchCollection(currentQuery: String){
        (viewModel as ViewModelListCollections).searchCollection(currentQuery)

        (viewModel as ViewModelListCollections).collectionsList.observe(this.requireActivity(), {
            lifecycleScope.launch {
                adapterCollectionList.submitData(it)
            }
        })

        customNetwork.state.observe(this.requireActivity(),{
            adapterCollectionList.setState(it)
        })
    }

    private fun openPhoto(item: Photo){
        val bundle = FragmentPhotoView.bundleFor(item)
        this.findNavController().navigate(R.id.action_fragmentSearch_to_photoViewFragment, bundle)
    }

    private fun openCollection(item: PhotoCollection){
        val bundle = FragmentPhotoList.bundleFor(type = FragmentPhotoList.KEY_TYPE_COLLECTION_PHOTO, collection = item)
        this.findNavController().navigate(R.id.action_fragmentSearch_to_listPhotoFragment, bundle)
    }
}