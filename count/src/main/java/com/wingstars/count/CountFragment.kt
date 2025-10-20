package com.wingstars.count

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wingstars.base.base.BaseFragment
import com.wingstars.count.databinding.FragmentCountBinding

class CountFragment : BaseFragment(){
    private lateinit var binding: FragmentCountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountBinding.inflate(inflater, container, false)
        val root = binding.root
        initView()
        return root
    }

    private fun initView() {

    }
}