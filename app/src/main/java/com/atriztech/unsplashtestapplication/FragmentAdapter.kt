package com.atriztech.unsplashtestapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.atriztech.unsplashtestapplication.view.collectionlist.FragmentCollectionList
import com.atriztech.unsplashtestapplication.view.FragmentRandomPhoto

class FragmentAdapter(fm: Fragment) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FragmentRandomPhoto()
            }
            1 -> {
                FragmentCollectionList()
            }
            else -> Fragment()
        }
    }
}