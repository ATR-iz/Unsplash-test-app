package com.atriztech.unsplashtestapplication.view

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.atriztech.unsplashtestapplication.CustomFragment
import com.atriztech.unsplashtestapplication.FragmentAdapter
import com.atriztech.unsplashtestapplication.R
import com.atriztech.unsplashtestapplication.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout

class FragmentMain : CustomFragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        setupActionBar(isEnabledOptionMenu = true, isEnabledBackButton = false, isHideActionBar = false)
        setupMenu()
        setupTabLayout()
        return binding.root
    }

    private fun setupTabLayout(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.tab_photo)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.tab_collection)))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = FragmentAdapter(this)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.getTabAt(position)!!.select()
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //БАГ: При скроле списков не блокируется свайп viewPager. Из-за этого плохо отрабатывает swipe to update и сам скрол
        binding.viewPager.isUserInputEnabled = false
    }

    private fun setupMenu(){
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.search_button -> this.findNavController()
                .navigate(R.id.action_mainFragment_to_fragmentSearch)
        }
        return true
    }
}
