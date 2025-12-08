package com.wingstars.base.net.beans

import java.io.Serializable

data class WSCalendarResponse(
    val id: Int,
    val title: Title,                   // 活動標題
    val content: Content,               // 活動資訊
    val calendar_category: List<Int>?,  // 活動分類
    val acf: Acf?,                      // (Nullable)
    //  改為 Any? 可以同時取得 Object {} 和 Array []
    val yoast_head_json: Any?
) : Serializable {

    val titleF: String
        get() = title.rendered

    val contentF: String
        get() = content.rendered

    val calendar_categoryF: Int
        get() {
            return if (calendar_category != null && calendar_category.isNotEmpty()) {
                calendar_category[0]
            } else {
                0
            }
        }

    val dateF: String
        get() = acf?.date ?: ""

    val mapF: String
        get() = acf?.map ?: ""

    val PrecautionsF: String
        get() = acf?.Precautions ?: ""

    val urlF: String
        get() {
            try {
                if (yoast_head_json is Map<*, *>) {
                    val ogImage = yoast_head_json["og_image"]

                    if (ogImage is List<*> && ogImage.isNotEmpty()) {
                        val firstItem = ogImage[0]
                        if (firstItem is Map<*, *>) {
                            return firstItem["url"]?.toString() ?: ""
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

    data class Title(
        val rendered: String,
    ) : Serializable

    data class Content(
        val rendered: String,
    ) : Serializable

    data class Acf(
        val date: String?,
        val map: String?,
        val Precautions: String?
    ) : Serializable
    val contentRaw: String
        get() = content.rendered
}
