package com.wingstars.base.utils

import android.content.Context

class DPUtils {
    companion object {

        fun dpToPx(dp: Float, context: Context): Float {
            return dp * (context.resources.displayMetrics.density)
        }
    }
}