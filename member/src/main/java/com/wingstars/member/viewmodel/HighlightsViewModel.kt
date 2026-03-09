package com.wingstars.member.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wingstars.base.net.API
import com.wingstars.base.net.NetBase
import com.wingstars.base.net.beans.YoutubeListResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

enum class HighlightsType {
    HT_WONDERFUL_VIDEOS,      // 精彩影片
    HT_FLASH_SHORT_FILM,      // 快閃短片
    HT_DAILY_VLOG             // 日常Vlog
}

class HighlightsViewModel : ViewModel() {
    var highlightsList = MutableLiveData<MutableList<YoutubeListResponse.Item>>()
    var isLoading = MutableLiveData<Boolean>()

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.postValue(isLoading)
    }

    fun getHighlightsList(highlightsType: HighlightsType) {
        setIsLoading(true)
        val arrayList = mutableListOf<YoutubeListResponse.Item>()

        // Xác định Playlist ID dựa theo Tab đang bấm
        val playlistId = when (highlightsType) {
//            HighlightsType.HT_WONDERFUL_VIDEOS -> "UUSEI3nk0QSGcQKR75O6vM6Q"
            HighlightsType.HT_WONDERFUL_VIDEOS -> "PLTYHsJxRmtwZT2vFEnqXlCVDwiIhSliGf"
            HighlightsType.HT_FLASH_SHORT_FILM -> "PLTYHsJxRmtwYWMPUQbN2MizeS35YkyTFy"         // Tab 2: TODO - Thay bằng ID Playlist Shorts
            HighlightsType.HT_DAILY_VLOG -> "PLTYHsJxRmtwa8CotI9T53I1TOWro2IiSA"               // Tab 3: TODO - Thay bằng ID Playlist Vlog
        }

        API.shared?.api?.let { api ->
            // Gọi hàm API trực tiếp đến Google với playlistId tương ứng của từng Tab
            api.getYoutubePlaylistItemsDirect(
                "snippet",
                playlistId,
                20,
                NetBase.YOUTUBE_API_KEY
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        setIsLoading(false)
                        response.items?.let { items ->
                            arrayList.addAll(items)
                        }
                        highlightsList.postValue(arrayList)
                    },
                    { error ->
                        setIsLoading(false)
                        highlightsList.postValue(arrayList)
                        Log.e("YoutubeError", "Error in GOOGLE: ${error.message}", error)
                    }
                )
        }
    }
}