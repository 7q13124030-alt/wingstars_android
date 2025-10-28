package com.wingstars.member.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class HighlightsType {
    HT_WONDERFUL_VIDEOS,      //精彩影片
    HT_FLASH_SHORT_FILM,      //快閃短片
    HT_DAILY_VLOG             //日常Vlog
}

class HighlightsViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var highlightsList = MutableLiveData<MutableList<Int>>()

    fun getHighlightsList(highlightsType: HighlightsType) {
        when (highlightsType) {
            HighlightsType.HT_WONDERFUL_VIDEOS -> {//精彩影片
                val arrayList = mutableListOf(1,2,3,4,5,6)
                highlightsList.postValue(arrayList)
            }

            HighlightsType.HT_FLASH_SHORT_FILM -> {//快閃短片
                val arrayList = mutableListOf(1, 2,3)
                highlightsList.postValue(arrayList)
            }

            HighlightsType.HT_DAILY_VLOG -> {//日常Vlog
                val arrayList = mutableListOf(1, 2)
                highlightsList.postValue(arrayList)
            }
        }
    }
}