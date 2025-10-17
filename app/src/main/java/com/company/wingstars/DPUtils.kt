package com.company.wingstars

import android.content.Context

class DPUtils {
    companion object {

        fun dpToPx(dp: Float, context: Context): Float {
            return dp * (context.resources.displayMetrics.density)
        }
    }
}