package com.susuryo.stockorea.model

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailApi {

    @GET("getStockPriceInfo")
    fun getDetail(
        @Query("serviceKey") serviceKey: String,
        @Query("isinCd") likeItmsNm: String,
        @Query("resultType") resultType: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
    ): Single<StockPriceResponse>

    @GET("getStockPriceInfo")
    fun getDate(
        @Query("serviceKey") serviceKey: String,
        @Query("isinCd") likeItmsNm: String,
        @Query("resultType") resultType: String,
        @Query("endBasDt") endBasDt: Int,
    ): Single<StockPriceResponse>

}