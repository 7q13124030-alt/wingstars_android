package com.wingstars.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wingstars.base.base.BaseFragment
import com.wingstars.home.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment(){
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root
        initView()
        return root
    }

    private fun initView() {

    }
}