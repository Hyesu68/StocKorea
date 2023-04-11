package com.susuryo.stockorea.model

import com.susuryo.stockorea.BuildConfig
import com.susuryo.stockorea.di.DaggerApiComponent
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DetailService {

    @Inject
    lateinit var api: DetailApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getDetail(id: String, numOfRows: Int, pageNo: Int): Single<StockPriceResponse> {
        return api.getDetail(BuildConfig.STOCK_API_KEY, id, "json", numOfRows, pageNo)
    }

    fun getDate(id: String, endBasDt: Int): Single<StockPriceResponse> {
        return api.getDate(BuildConfig.STOCK_API_KEY, id, "json", endBasDt)
    }

}