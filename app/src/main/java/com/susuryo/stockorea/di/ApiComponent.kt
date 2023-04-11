package com.susuryo.stockorea.di

import com.susuryo.stockorea.model.DetailService
import com.susuryo.stockorea.model.EnterpricesService
import com.susuryo.stockorea.viewmodel.DetailViewModel
import com.susuryo.stockorea.viewmodel.EnterpriceViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: EnterpricesService)
    fun inject(viewModel: EnterpriceViewModel)
    fun inject(detail: DetailService)
    fun inject(detailViewModel: DetailViewModel)
}