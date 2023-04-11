package com.susuryo.stockorea.model

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface EnterpricesApi {

    @GET("getStockPriceInfo")
    fun getEnterprices(
        @Query("serviceKey") serviceKey: String,
        @Query("likeItmsNm") likeItmsNm: String,
        @Query("resultType") resultType: String,
        @Query("numOfRows") numOfRows: Int
    ): Single<StockPriceResponse>

}