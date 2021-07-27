package com.example.backbase.presentation.base

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.backbase.R
import com.example.backbase.presentation.base.BaseActivity

/**
 * Created by ahmed on 10/20/2016.
 */
abstract class BaseFragmentActivity : BaseActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_fragment)
        if (supportFragmentManager
                .findFragmentById(R.id.fragment_container) == null
        ) {
            val fragment = fragment
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
        }
    }

    protected abstract val fragment: Fragment
    protected val alreadyAddedFragment: Fragment?
        get() = supportFragmentManager
            .findFragmentById(R.id.fragment_container)

    override fun onBackPressed() {
        if (supportFragmentManager
                .backStackEntryCount <= 1
        ) finish() else super.onBackPressed()
    }

    fun hideKeyboard() {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    fun replaceMainFragmentAndAddToBackStack(newFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, newFragment, newFragment.javaClass.simpleName)
            .addToBackStack(newFragment.javaClass.simpleName)
            .commit()
    }

    fun replaceMainFragment(newFragment: Fragment?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, newFragment!!)
            .commit()
    }
}