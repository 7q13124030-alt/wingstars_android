package com.wingstars.user.code

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsControllerCompat
import com.wingstars.base.base.BaseActivity
import com.wingstars.user.BaseApplication
import com.wingstars.user.R
import com.wingstars.user.databinding.ActivityMemBarCodeBinding

class MemBarCodeActivity : BaseActivity(){
    private lateinit var binding: ActivityMemBarCodeBinding
    private var phone: String?=null
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMemBarCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        phone = intent.getStringExtra("phone")
        initView()


    }

    override fun initView() {
        binding.includeTop.ivClose.setOnClickListener { finish() }
    }


}