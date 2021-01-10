package com.atriztech.unsplashtestapplication

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class CustomFragment: Fragment() {
    fun setupActionBar(isEnabledOptionMenu: Boolean = true, isEnabledBackButton: Boolean = false, isHideActionBar: Boolean = false){
        setHasOptionsMenu(isEnabledOptionMenu)

        if (this.requireActivity() is AppCompatActivity){

            val currentActivity = this.requireActivity() as AppCompatActivity

            currentActivity.supportActionBar?.setDisplayHomeAsUpEnabled(isEnabledBackButton)

            when(isHideActionBar){
                true -> currentActivity.supportActionBar?.hide()
                false -> currentActivity.supportActionBar?.show()
            }
        }
    }
}