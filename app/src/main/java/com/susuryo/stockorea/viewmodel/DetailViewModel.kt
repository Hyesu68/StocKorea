package com.susuryo.stockorea.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.susuryo.stockorea.di.DaggerApiComponent
import com.susuryo.stockorea.model.DetailService
import com.susuryo.stockorea.model.StockPriceResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel: ViewModel() {

    private val disposable = CompositeDisposable()
    fun refresh(id: String, numOfRows: Int, pageNo: Int) {
        fetchDetail(id, numOfRows, pageNo)
    }

    fun getFromDate(id: String, endBasDt: Int) {
        fetchDate(id, endBasDt)
    }

    @Inject
    lateinit var detailService: DetailService
    init {
        DaggerApiComponent.create().inject(this)
    }

    val detailResponse = MutableLiveData<StockPriceResponse>()
    val loading = MutableLiveData<Boolean>()

    private fun fetchDetail(id: String, numOfRows: Int, pageNo: Int) {
        loading.value = true
        disposable.add(
            detailService.getDetail(id, numOfRows, pageNo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<StockPriceResponse>() {
                    override fun onSuccess(value: StockPriceResponse) {
                        detailResponse.value = value
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                    }
                })
        )
    }

    private fun fetchDate(id: String, endBasDt: Int) {
        loading.value = true
        disposable.add(
            detailService.getDate(id, endBasDt)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<StockPriceResponse>() {
                    override fun onSuccess(value: StockPriceResponse) {
                        detailResponse.value = value
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
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