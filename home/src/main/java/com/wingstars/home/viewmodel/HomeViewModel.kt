package com.wingstars.home.viewmodel // Đặt package name cho đúng

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wingstars.base.net.API
import com.wingstars.base.net.NetBase
import com.wingstars.base.net.beans.FashionResponse
import com.wingstars.base.net.beans.IteneraryResponse
import com.wingstars.base.net.beans.LatestNewsResponse
import com.wingstars.base.net.beans.ProductsResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    // LiveData cũ
    val homeDataList = MutableLiveData<MutableList<Int>>()
    val newsDataList = MutableLiveData<MutableList<LatestNewsResponse>>()
    val memberDataList = MutableLiveData<MutableList<Int>>()
    val calendarDataList = MutableLiveData<MutableList<IteneraryResponse>>()
    val productDataList = MutableLiveData<MutableList<ProductsResponse>>()
    val fashionDataList = MutableLiveData<MutableList<FashionResponse>>()


    var isLoading = MutableLiveData<Boolean>()





    public fun getHomeData() {
        // Dữ liệu cho 5 list cũ (sản phẩm, thành viên, v.v.)
        val dummyList = mutableListOf(1, 2, 3, 4)
        homeDataList.postValue(dummyList)

//        val newList = mutableListOf(1, 2, 3)
//        newsDataList.postValue(newList)

        val memberList = mutableListOf(1, 2, 3, 4, 5)
        memberDataList.postValue(memberList)
        getLatestNewsData()
        getCalendarData()
        getProductsData()
        getFashionsData()
    }
    fun getLatestNewsData() {
        isLoading.postValue(true)
        API.shared?.api?.let {
            val observerT =
                it.latestNews(
                    "${NetBase.HOST_HAWKS}/wp-json/wp/v2/posts",
                    4,
                    "date",
                    "desc"
                )
            observerT?.subscribeOn(Schedulers.io())?.unsubscribeOn(Schedulers.io())?.observeOn(
                AndroidSchedulers.mainThread()
            )?.subscribe(
                { next ->
                    isLoading.postValue(false)
                    var itemTypeList: MutableList<LatestNewsResponse> = mutableListOf()
                    itemTypeList.clear()
                    itemTypeList.addAll(next)
                    newsDataList.postValue(itemTypeList)
                },
                { error ->
                    isLoading.postValue(false)
//                    error.message?.let { it1 ->
//                        Toast.makeText(
//                            BaseApplication.shared()!!,
//                            it1?.toString(),
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
                }
            )
        }

        //utApi()
    }
    fun getCalendarData() {
        val fullUrl = "${NetBase.HOST_HAWKS}/wp-json/wp/v2/calendar?_fields=id,title.rendered,acf,content.rendered,yoast_head_json.og_image,calendar_category"
        API.shared?.api?.let { api ->
            val observer = api.getItineraryList(fullUrl)
            observer
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { next ->
                        val list = mutableListOf<IteneraryResponse>()
                        list.addAll(next)
                        calendarDataList.postValue(list)
                    },
                    { error ->
                        error.printStackTrace()
                    }
                )
        }
    }
    fun getProductsData(){
        val fullUrl = "${NetBase.HOST_HAWKS}/wp-json/wc/v3/products"
        API.shared?.api?.let { api ->
            val observer = api.getProducts(fullUrl, 4, "desc")
            observer
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {next ->
                            val list = mutableListOf<ProductsResponse>()
                            list.addAll(next)
                        productDataList.postValue(list)
                    },
                    {error ->
                        error.printStackTrace()
                    }
                )
        }
    }
    fun getFashionsData(){
        val fullUrl = "${NetBase.HOST_HAWKS}/wp-json/wp/v2/fashion?_fields=id,title,yoast_head_json.og_image,fashion_category&orderby=date&order=desc"
        API.shared?.api?.let { api ->
            val observer = api.getFashion(fullUrl)
            observer
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {next ->
                        val list = mutableListOf<FashionResponse>()
                        list.addAll(next)
                        fashionDataList.postValue(list)
                    },
                    {error ->
                        error.printStackTrace()
                    }
                )
        }
    }
}