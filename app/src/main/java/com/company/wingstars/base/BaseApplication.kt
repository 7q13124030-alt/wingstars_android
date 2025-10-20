package com.company.wingstars.base

import android.app.Application
import com.wingstars.base.utils.MMKVManagement

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MMKVManagement.init(this)
    }
}