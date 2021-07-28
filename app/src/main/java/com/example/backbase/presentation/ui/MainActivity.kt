package com.example.backbase.presentation.ui

import androidx.fragment.app.Fragment
import com.example.backbase.presentation.base.BaseFragmentActivity
import com.example.backbase.presentation.ui.citiesList.FetchDetailsFragment

class MainActivity : BaseFragmentActivity() {

    override val fragment: Fragment
        get() = FetchDetailsFragment()

}