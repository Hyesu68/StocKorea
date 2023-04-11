package com.susuryo.stockorea.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.susuryo.stockorea.di.DaggerApiComponent
import com.susuryo.stockorea.model.EnterpricesService
import com.susuryo.stockorea.model.StockPriceResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EnterpriceViewModel: ViewModel() {
    private val disposable = CompositeDisposable()
    fun refresh(name: String) {
        fetchEnterprices(name)
    }

    @Inject
    lateinit var enterpricesService: EnterpricesService
    init {
        DaggerApiComponent.create().inject(this)
    }

    val stockPrices = MutableLiveData<StockPriceResponse>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private fun fetchEnterprices(name: String) {
        loading.value = true
        disposable.add(
            enterpricesService.getEnterprices(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<StockPriceResponse>() {
                    override fun onSuccess(value: StockPriceResponse) {
                        stockPrices.value = value
                        countryLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        countryLoadError.value = true
                        loading.value = false
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}