package com.wingstars.member.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wingstars.member.R
import com.wingstars.member.adapter.ScheduleFunBean
import com.wingstars.member.adapter.SelectTeamFunBean
import java.util.Date

class PersonalScheduleViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var teamCategoryList = MutableLiveData<MutableList<SelectTeamFunBean>>()
    var personalScheduleList = MutableLiveData<MutableList<ScheduleFunBean>>()

    val dataDTOArrayList=MutableLiveData<MutableList<String>>()

    public fun getTeamCategoryList() {
        val itemList: MutableList<SelectTeamFunBean> = mutableListOf()
        var bean =
            SelectTeamFunBean("雄鷹", R.drawable.ic_baseball_dark, R.drawable.ic_baseball_pink)
        itemList.add(bean)
        bean =
            SelectTeamFunBean("獵鷹", R.drawable.ic_basketball_dark, R.drawable.ic_basketball_pink)
        itemList.add(bean)
        bean =
            SelectTeamFunBean("天鷹", R.drawable.ic_volleyball_dark, R.drawable.ic_volleyball_pink)
        itemList.add(bean)

        teamCategoryList.postValue(itemList)
    }

    public fun getPersonalScheduleList() {
        val itemList: MutableList<ScheduleFunBean> = mutableListOf()
        var bean = ScheduleFunBean("9/5 (五)", "Stars House 一日店長")
        itemList.add(bean)
        bean = ScheduleFunBean("9/9 (二)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/10 (三)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/12 (五)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/13 (六)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/14 (日)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/16 (二)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/20 (六)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/25 (四)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/26 (五)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/27 (六)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/28 (日)", "雄鷹")
        itemList.add(bean)
        bean = ScheduleFunBean("9/29 (一)", "雄鷹")
        itemList.add(bean)

        personalScheduleList.postValue(itemList)
    }

    fun getOtherMonthData(selectMonth: String){

    }
    fun getWingStarsScheduleJson(selectMonth: String) {
        val tempList = mutableListOf<String>(selectMonth)

        dataDTOArrayList.postValue(tempList)
    }
}