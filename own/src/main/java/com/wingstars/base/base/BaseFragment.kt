package com.wingstars.base.base

import android.view.View
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.wingstars.base.R

open class BaseFragment : Fragment(){


    fun getStatusBarHeight(): Int {
        return ImmersionBar.getStatusBarHeight(requireActivity())
    }

    fun setStatusBarColor(colorRes: Int = R.color.color_DE9DBA, darkFont: Boolean = true) {
        immersionBar {
            statusBarColor(colorRes)
            statusBarDarkFont(darkFont)
        }
    }


}