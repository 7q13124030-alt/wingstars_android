package com.wingstars.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wingstars.base.base.BaseFragment
import com.wingstars.calendar.databinding.FragmentCalendarBinding

class CalendarFragment : BaseFragment(){
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root = binding.root
        initView()
        return root
    }

    private fun initView() {

    }
}