package com.wingstars.base.net.beans


data class WSProductResponse(
    val id: Int,
    val name: String,                   //產品名稱
    val permalink: String,              //產品網址
    val price: String,                  //當前產品價格
    val date_on_sale_from: String?,     //上市时间开始
    val date_on_sale_to: String?,       //上市时间结束
    val images: List<Image>,            //圖片
    val yoast_head_json: YoastHeadJson, //圖片
) {
    //imageF和urlF都可以取得图片地址，应该是一样的
    val imageF: String                  //images[0].src format
        get() {
            return if(images.count() > 0){
                images[0].src
            } else {
                ""
            }
        }

    val urlF: String                    //yoast_head_json.og_image[0].url format
        get() {
            return if(yoast_head_json.og_image.count() > 0){
                yoast_head_json.og_image[0].url
            } else {
                ""
            }
        }

    val date_on_sale_fromF: String                    //date_on_sale_from format
        get() {
            return date_on_sale_from?.replace("-", "/")?.replace("T", " ") ?: ""
        }

    val date_on_sale_toF: String                    //date_on_sale_to format
        get() {
            return date_on_sale_to?.replace("-", "/")?.replace("T", " ") ?: ""
        }

    data class Image(
        val src: String,                //图像
    ) : java.io.Serializable

    data class YoastHeadJson(
        val og_image: List<OGImage>,    //圖片
    ) : java.io.Serializable {
        data class OGImage(
            val width: Int,
            val height: Int,
            val url: String,            //圖片地址
            val type: String,
        ) : java.io.Serializable
    }
}
