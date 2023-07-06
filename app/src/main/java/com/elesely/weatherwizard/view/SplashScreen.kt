package com.elesely.weatherwizard.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elesely.weatherwizard.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Get_Started.setOnClickListener {
            val next_page = Intent(this, MainActivity::class.java)
            startActivity(next_page)
            finish()
        }
    }
}