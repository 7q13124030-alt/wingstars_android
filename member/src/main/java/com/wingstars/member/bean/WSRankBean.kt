package com.wingstars.member.bean

data class WSRankBean(
    var title: String,
    var acf: MutableList<ACFBean>?=null,
    var content: String?=null
){
    data class ACFBean(
        var name: String,
        var volume: String,
        var image: String=""
    )
}
