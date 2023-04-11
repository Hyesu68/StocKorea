package com.susuryo.stockorea.model

import com.susuryo.stockorea.BuildConfig
import com.susuryo.stockorea.di.DaggerApiComponent
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class EnterpricesService {

    @Inject
    lateinit var api: EnterpricesApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getEnterprices(name: String): Single<StockPriceResponse> {
        return api.getEnterprices(BuildConfig.STOCK_API_KEY, name, "json", 100)
    }

}