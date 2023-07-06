package com.elesely.weatherwizard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elesely.weatherwizard.model.WeatherModel
import com.elesely.weatherwizard.service.WeatherApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel: ViewModel() {

    private val weatherApiService = WeatherApiService()
    private val disposable = CompositeDisposable()

    val weather_data = MutableLiveData<WeatherModel>()
    val weather_error = MutableLiveData<Boolean>()

    fun refreshData(cityName:String){

        getDataFromApi(cityName)
    }

    private fun getDataFromApi(cityName: String) {

        disposable.add(
            weatherApiService.getDataService(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>(){
                    override fun onSuccess(t: WeatherModel) {

                        weather_data.value = t
                        weather_error.value = false
                    }

                    override fun onError(e: Throwable) {
                        weather_error.value = true
                    }

                })

        )
    }


}