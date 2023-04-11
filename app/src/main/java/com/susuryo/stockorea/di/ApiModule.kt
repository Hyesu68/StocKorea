package com.susuryo.stockorea.di

import com.susuryo.stockorea.model.DetailApi
import com.susuryo.stockorea.model.DetailService
import com.susuryo.stockorea.model.EnterpricesApi
import com.susuryo.stockorea.model.EnterpricesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private val BASE_URL = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/"

    @Provides
    fun provideEnterpricesApi(): EnterpricesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(EnterpricesApi::class.java)
    }

    @Provides
    fun provideEnterpricesService(): EnterpricesService {
        return EnterpricesService()
    }

    @Provides
    fun provideDetailApi(): DetailApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(DetailApi::class.java)
    }

    @Provides
    fun provideDetailService(): DetailService {
        return DetailService()
    }
}