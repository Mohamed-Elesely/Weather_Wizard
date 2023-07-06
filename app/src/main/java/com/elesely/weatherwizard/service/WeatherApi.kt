package com.elesely.weatherwizard.service

import com.elesely.weatherwizard.model.WeatherModel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather?&units=metric&APPID=72e343cb3ed01bfb8be8c71b1e74154d")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>
}