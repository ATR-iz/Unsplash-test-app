package com.atriztech.unsplashtestapplication.view.photolist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.atriztech.unsplashtestapplication.R
import com.atriztech.unsplashtestapplication.databinding.FragmentPhotoListBinding
import com.atriztech.unsplashtestapplication.di.App
import com.atriztech.unsplashtestapplication.view.FragmentPhotoView
import com.atriztech.unsplashtestapplication.view.AdapterLoadState
import com.atriztech.unsplashtestapplication.viewmodel.ViewModelListPhoto
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.future_unsplash.api.PhotoCollection
import com.atriztech.unsplashtestapplication.CustomFragment
import com.atriztech.unsplashtestapplication.CustomNetwork
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentPhotoList : CustomFragment() {

    companion object{
        const val KEY_TYPE = "type"
        const val KEY_TYPE_COLLECTION_PHOTO = "collection_photo"
        const val KEY_TYPE_SEARCH_PHOTO = "search_photo"
        const val KEY_TYPE_SEARCH_PHOTO_IN_COLLECTION = "search_photo_in_collection"

        const val KEY_COLLECTION = "collection"
        const val KEY_SEARCH_STRING = "search"

        fun bundleFor(type: String, collection: PhotoCollection? = null, searchString: String = ""): Bundle{
            val bundle = Bundle()
            bundle.putString(KEY_TYPE, type)
            bundle.putParcelable(KEY_COLLECTION, collection)
            bundle.putString(KEY_SEARCH_STRING, searchString)
            return bundle
        }
    }

    @Inject lateinit var viewModelListPhoto: ViewModelListPhoto
    private lateinit var recyclerView: AdapterPhotoList
    private lateinit var binding: FragmentPhotoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupActionBar(isEnabledOptionMenu = true, isEnabledBackButton = true, isHideActionBar = false)

        if (!this::binding.isInitialized ) {
            App.component().inject(this)

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_list, container,false)

            viewModelListPhoto.setData(requireArguments())

            viewModelListPhoto.title.observe(this.requireActivity(), {
                when(viewModelListPhoto.state){
                    is ViewModelListPhoto.State.CollectionPhoto -> this.requireActivity().title = it
                    is ViewModelListPhoto.State.SearchPhoto -> this.requireActivity().title = getString(R.string.find_photo) + it
                    is ViewModelListPhoto.State.SearchPhotoInCollection -> this.requireActivity().title = getString(R.string.find_photo) + it
                }
            })

            viewModelListPhoto.customNetwork.state.observe(this.requireActivity(), {
                when(it){
                    is CustomNetwork.NetworkState.LoadError -> {
                        Toast.makeText(this.requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })

            initRecyclerView()
            initSwipe()
        }

        return binding.root
    }

    private fun initRecyclerView(){
        recyclerView = AdapterPhotoList { openPhoto(it) }

        binding.listPhoto.layoutManager = GridLayoutManager(this.requireContext(), 3)

        binding.listPhoto.adapter = recyclerView.withLoadStateFooter(
            footer = AdapterLoadState { recyclerView.retry() }
        )

        viewModelListPhoto.listPhoto.observe(this.requireActivity(), {
            lifecycleScope.launch {
                recyclerView.submitData(it)
            }
        })

        viewModelListPhoto.customNetwork.state.observe(this.requireActivity(),{
            recyclerView.setState(it)
        })
    }

    private fun initSwipe(){
        binding.swipeUpdateListPhoto.setOnRefreshListener {
            viewModelListPhoto.updateData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_photo, menu)

        val searchManager = this.requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search_photo_button).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.requireActivity().componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchPhotoInCollection(p0!!)
                searchView.setQuery("", true)
                searchView.isIconified = true
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> this.requireActivity().onBackPressed()
        }
        return true
    }

    fun searchPhotoInCollection(txt: String){
        val photoCollection = requireArguments().getParcelable<PhotoCollection>(KEY_COLLECTION) as PhotoCollection
        val bundle = FragmentPhotoList.bundleFor(type = KEY_TYPE_SEARCH_PHOTO_IN_COLLECTION, collection = photoCollection, searchString = txt)
        this.findNavController().navigate(R.id.action_listPhotoFragment_self, bundle)
    }

    private fun openPhoto(item: Photo){
        val bundle = FragmentPhotoView.bundleFor(item)
        this.findNavController().navigate(R.id.action_listPhotoFragment_to_photoViewFragment, bundle)
    }
}