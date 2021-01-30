package com.atriztech.unsplashtestapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.unsplashtestapplication.CustomNetwork
import com.atriztech.unsplashtestapplication.R
import com.atriztech.unsplashtestapplication.databinding.FragmentRandomPhotoBinding
import com.atriztech.unsplashtestapplication.di.App
import com.atriztech.unsplashtestapplication.viewmodel.ViewModelRandomPhoto
import com.squareup.picasso.Callback.EmptyCallback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_random_photo.view.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class FragmentRandomPhoto  : Fragment() {
    private lateinit var binding: FragmentRandomPhotoBinding
    @Inject lateinit var viewModel: ViewModelRandomPhoto

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.requireActivity().title = getString(R.string.app_name)

        if (!this::binding.isInitialized) {
            App.component().inject(this)

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_random_photo, container, false)
            binding.fragment = this

            lifecycleScope.launch {
                viewModel.getRandomPhoto()
            }

            viewModel.randomPhoto.observe(this.requireActivity(), {
                setImage(it)
            })

            initNetworkState()
            initSwipe()
        }

        return binding.root
    }

    private fun initNetworkState(){
        viewModel.customNetwork.state.observe(this.requireActivity(), {
            when (it) {
                is CustomNetwork.NetworkState.Loading -> {
                    binding.swipeUpdateRandomPhoto.isRefreshing = true
                }
                is CustomNetwork.NetworkState.Loaded -> {
                    binding.swipeUpdateRandomPhoto.isRefreshing = false
                }
                is CustomNetwork.NetworkState.LoadError -> {
                    Toast.makeText(this.requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    binding.swipeUpdateRandomPhoto.isRefreshing = false
                }
            }
        })
    }

    private fun initSwipe(){
        binding.swipeUpdateRandomPhoto.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getRandomPhoto()
            }
        }
    }

    private fun setImage(listPhoto: List<Photo>){
        loadImage(listPhoto[0].urls.small, binding.randomImage1)
        loadImage(listPhoto[1].urls.small, binding.randomImage2)
        loadImage(listPhoto[2].urls.small, binding.randomImage3)
        loadImage(listPhoto[3].urls.small, binding.randomImage4)
        loadImage(listPhoto[4].urls.small, binding.randomImage5)
        loadImage(listPhoto[5].urls.small, binding.randomImage6)
        loadImage(listPhoto[6].urls.small, binding.randomImage7)
        loadImage(listPhoto[7].urls.small, binding.randomImage8)
        loadImage(listPhoto[8].urls.small, binding.randomImage9)
        loadImage(listPhoto[9].urls.small, binding.randomImage10)
        loadImage(listPhoto[10].urls.small, binding.randomImage11)
        loadImage(listPhoto[11].urls.small, binding.randomImage12)
        loadImage(listPhoto[12].urls.small, binding.randomImage13)
        loadImage(listPhoto[13].urls.small, binding.randomImage14)
        loadImage(listPhoto[14].urls.small, binding.randomImage15)
    }

    private fun loadImage(url: String, imageView: View){
        imageView.progress_bar_random_photo.visibility = View.VISIBLE

        Picasso.get()
            .load(url)
            .into(imageView.image, object : EmptyCallback() {
                override fun onSuccess() {
                    imageView.progress_bar_random_photo.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    imageView.progress_bar_random_photo.visibility = View.GONE
                }
            })
    }

    fun onClickImageView(view: View){
        if (!viewModel.randomPhoto.value.isNullOrEmpty())
            when (view){
                binding.randomImage1 -> openPhoto(viewModel.randomPhoto.value!![0])
                binding.randomImage2 -> openPhoto(viewModel.randomPhoto.value!![1])
                binding.randomImage3 -> openPhoto(viewModel.randomPhoto.value!![2])
                binding.randomImage4 -> openPhoto(viewModel.randomPhoto.value!![3])
                binding.randomImage5 -> openPhoto(viewModel.randomPhoto.value!![4])
                binding.randomImage6 -> openPhoto(viewModel.randomPhoto.value!![5])
                binding.randomImage7 -> openPhoto(viewModel.randomPhoto.value!![6])
                binding.randomImage8 -> openPhoto(viewModel.randomPhoto.value!![7])
                binding.randomImage9 -> openPhoto(viewModel.randomPhoto.value!![8])
                binding.randomImage10 -> openPhoto(viewModel.randomPhoto.value!![9])
                binding.randomImage11 -> openPhoto(viewModel.randomPhoto.value!![10])
                binding.randomImage12 -> openPhoto(viewModel.randomPhoto.value!![11])
                binding.randomImage13 -> openPhoto(viewModel.randomPhoto.value!![12])
                binding.randomImage14 -> openPhoto(viewModel.randomPhoto.value!![13])
                binding.randomImage15 -> openPhoto(viewModel.randomPhoto.value!![14])
            }
    }

    private fun openPhoto(item: Photo){
        val bundle = FragmentPhotoView.bundleFor(item)
        this.findNavController().navigate(R.id.action_mainFragment_to_photoViewFragment, bundle)
    }
}