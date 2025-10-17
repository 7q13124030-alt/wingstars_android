package com.company.wingstars

import android.os.Bundle
import com.company.wingstars.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTitleFoot(view1=binding.root,statusBarColor=R.color.color_DFE0E2)
        initView()

    }

    override fun initView() {

    }


}
