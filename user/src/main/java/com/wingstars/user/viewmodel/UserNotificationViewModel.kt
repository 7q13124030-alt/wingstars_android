package com.wingstars.user.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.wingstars.base.net.API
import com.wingstars.base.net.NetBase
import com.wingstars.base.net.beans.NSInfoRequest
import com.wingstars.base.utils.MMKVManagement
import com.wingstars.base.utils.NotificationHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class UserNotificationViewModel : ViewModel() {

    /**
     * Đồng bộ cài đặt lên Server
     */
    fun syncNotificationSetting(isOn: Boolean) {
        val deviceId = MMKVManagement.getTest() // Placeholder, ideally use a stable device ID
        val fcmToken = MMKVManagement.getFcmToken()
        
        val request = NSInfoRequest(
            deviceId = deviceId.ifEmpty { "android_device" },
            fcmToken = fcmToken,
            deviceIsPush = if (isOn) 0 else 1, // 0: Push, 1: No Push based on your data class comment
            crmMemberToken = MMKVManagement.getCrmMemberAccessToken(),
            userName = MMKVManagement.getMemberName(),
            crmMemberId = MMKVManagement.getCrmMemberId(),
            memberType = if (MMKVManagement.isLogin()) 1 else 0
        )

        API.shared?.api?.let { api ->
            api.nsInfo("${NetBase.HOST_BASE}/api/v1/app/mobile_crm/info", request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("NotificationSync", "Sync success: $isOn")
                }, {
                    Log.e("NotificationSync", "Sync failed", it)
                })
        }
    }

    /**
     * Lấy tin nhắn chưa đọc và hiển thị Local Notification
     */
    fun pushUnreadMessagesLocally(context: Context) {
        val memberId = MMKVManagement.getCrmMemberId()
        if (memberId == "0" || memberId.isEmpty()) return

        API.shared?.api?.let { api ->
            api.getInAppMessages(memberId, "", 1, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) {
                        response.data?.filter { it.status == 0 }?.forEach { msg ->
                            NotificationHelper.showNotification(
                                context,
                                msg.title,
                                msg.content,
                                msg.targetUrl
                            )
                        }
                    }
                }, {
                    Log.e("NotificationLocal", "Fetch unread failed", it)
                })
        }
    }
}