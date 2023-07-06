package com.elesely.weatherwizard.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.elesely.weatherwizard.R
import com.elesely.weatherwizard.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        var cName = GET.getString("cityName", "Cairo")?.toLowerCase()

        search_edit.setText(cName)
        viewModel.refreshData(cName!!)

        getLiveData()


        img_search.setOnClickListener {
            val cityName = search_edit.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewModel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: " + cityName)
        }
    }

    private fun getLiveData() {
        viewModel.weather_data.observe(this, Observer { data ->
            data?.let {
                card_data.visibility = View.VISIBLE
                card_data2.visibility = View.VISIBLE
                //Setting date
                val timestamp = data.dt // Assuming data.dt is the timestamp in seconds
                val date = Date(timestamp * 1000L) // Convert seconds to milliseconds
                val sdf =
                    SimpleDateFormat("MMMM dd", Locale.getDefault()) // Format for "September 18"
                val formattedDate = sdf.format(date)

                val dayOfMonth = date.date // Get the day of the month (1-31)
                val daySuffix = getDayOfMonthSuffix(dayOfMonth)
                val finalFormattedDate = "$formattedDate$daySuffix"
                tv_date.text = finalFormattedDate

                //Setting the degree:
                tv_degree.text = data.main.temp.toInt().toString() + "°C"

                //Setting feels like
                feels_like.text = "Feels like " + data.main.feelsLike.toInt().toString() + " °C"

                //Setting the icon

                val weatherCondition = data.weather[0].main.toLowerCase(Locale.getDefault()).trim()
                val weatherConditionImage = when (weatherCondition) {
                    "clear" -> {
                        R.drawable.sun
                    }
                    "thunderstorm" -> {
                        R.drawable.thunderstorm
                    }
                    "rain" -> {
                        R.drawable.rain
                    }
                    "broken clouds" -> {
                        R.drawable.broken_clouds
                    }
                    "few clouds" -> {
                        R.drawable.few_clouds
                    }
                    "scattered clouds" -> {
                        R.drawable.scattered_clouds
                    }
                    "snow" -> {
                        R.drawable.snow
                    }
                    "mist" -> {
                        R.drawable.mist
                    }
                    "shower rain"->{
                        R.drawable.shower_rain
                    }
                    else -> {
                        R.drawable.demo_rain
                    }
                }
                image_weather.setImageResource(weatherConditionImage)

                //Setting wind
                tv_wind.text = data.wind.speed.toString() + " km/h"

                //Setting humditiy
                tv_humdity.text = data.main.humidity.toString() + " %"

                //Setting desc.
                tv_description.text = data.weather[0].description

                //Setting Country
                tv_country.text = data.sys.country
            }

        })

        viewModel.weather_error.observe(this, Observer { error ->
            error?.let {
                if (error) {
                    tv_error.visibility = View.VISIBLE
                    card_data.visibility=View.GONE
                    card_data2.visibility=View.GONE
                } else {
                    tv_error.visibility = View.GONE
                }
            }
        })
    }

    fun getDayOfMonthSuffix(day: Int): String {
        return when (day) {
            1, 21, 31 -> "st"
            2, 22 -> "nd"
            3, 23 -> "rd"
            else -> "th"
        }
    }
}