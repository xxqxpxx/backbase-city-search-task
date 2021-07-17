package com.example.backbase.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.backbase.R
import com.example.backbase.presentation.base.newBase.BaseFragmentActivity
import com.example.backbase.presentation.ui.example.FetchDetailsFragment

class MainActivity : BaseFragmentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override val fragment: Fragment
        get() = FetchDetailsFragment()

}