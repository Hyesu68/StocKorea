package com.susuryo.stockorea.model

import com.google.gson.annotations.SerializedName

data class StockPriceResponse(
    @SerializedName("response")
    val response: StockPriceInfo
)

data class StockPriceInfo(
    @SerializedName("body")
    val body: StockPriceInfoBody
)

data class StockPriceInfoBody(
    @SerializedName("items")
    val items: StockPriceInfoItem,
    @SerializedName("numOfRows")
    val numOfRows: Int,
    @SerializedName("pageNo")
    val pageNo: Int,
    @SerializedName("totalCount")
    val totalCount: Int
)

data class StockPriceInfoItem(
    @SerializedName("item")
    val item: List<StockPrice>,
)

data class StockPrice(
    @SerializedName("basDt")
    val basDt: String,
    @SerializedName("srtnCd")
    val srtnCd: String,
    @SerializedName("isinCd")
    val isinCd: String,
    @SerializedName("itmsNm")
    val itmsNm: String,
    @SerializedName("mrktCtg")
    val mrktCtg: String,
    @SerializedName("clpr")
    val clpr: String,
    @SerializedName("vs")
    val vs: String,
    @SerializedName("fltRt")
    val fltRt: String,
    @SerializedName("mkp")
    val mkp: String,
    @SerializedName("hipr")
    val hipr: String,
    @SerializedName("lopr")
    val lopr: String,
    @SerializedName("trqu")
    val trqu: String,
    @SerializedName("trPrc")
    val trPrc: String,
    @SerializedName("lstgStCnt")
    val lstgStCnt: String,
    @SerializedName("mrktTotAmt")
    val mrktTotAmt: String,
)
