package com.wingstars.member.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wingstars.member.R
import com.wingstars.member.adapter.BasicIntroductionFunBean


class BasicInformationViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var introductionList = MutableLiveData<MutableList<BasicIntroductionFunBean>>()
    var hobbyLists = MutableLiveData<MutableList<String>>()

    fun getBasicIntroductionList() {
        var itemList: MutableList<BasicIntroductionFunBean> = mutableListOf()
        var bean = BasicIntroductionFunBean("身高", R.drawable.ic_introduce_heigh, "169 cm")
        itemList.add(bean)
        bean = BasicIntroductionFunBean("體重", R.drawable.ic_introduce_weight, "50 kg")
        itemList.add(bean)
        bean = BasicIntroductionFunBean("生日", R.drawable.ic_introduce_birthday, "1997/10/03")
        itemList.add(bean)
        bean = BasicIntroductionFunBean("星座", R.drawable.ic_introduce_constellation, "天秤座")
        itemList.add(bean)
        bean = BasicIntroductionFunBean("血型", R.drawable.ic_introduce_blood_type, "AB 型")
        itemList.add(bean)

        introductionList.postValue(itemList)
    }

    fun getHobbyLists() {
        val itemList = mutableListOf<String>(
            "黑色",
            "白色",
            "收藏",
            "睡覺",
            "運動",
            "吃東西",
            "吃",
            "好吃",
            "擅长吃喝",
            "尽情的吃喝",
            "享受的人生",
            "喝",
            "人生得意须尽欢",
            "玩",
            "独处是一道最美的风景线",
            "乐"
        )
        hobbyLists.postValue(itemList)
    }
}