package com.example.weatherproject

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var user_result: TextView
    private lateinit var user_spinner: Spinner
    val cities = mapOf("Москва" to "Moscow", "Лондон" to "London", "Ульяновск" to "Ulyanovsk",
        "Казань" to "Kazan'", "Саранск" to "Saransk", "Санкт-Петербург" to "Saint Petersburg",
        "Минск" to "Minsk", "Киев" to "Kiev", "Париж" to "Paris", "Нью-Йорк" to "New York")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_result = findViewById(R.id.resultView)
        user_spinner = findViewById(R.id.spinner)

        ArrayAdapter.createFromResource(this, R.array.cityNames, android.R.layout.simple_spinner_item
        ).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            user_spinner.adapter = adapter
        }

        user_spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 != null) {
            val city: String = p0.getItemAtPosition(p2).toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val city1: String = cities[city].toString()
            val key = "c8707fe6fb4f4f1ba4917e49370b7e83"
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city1&appid=$key&units=metric&lang=ru"

            doAsync {
                val apiResponse = URL(url).readText()

                val weather = JSONObject(apiResponse).getJSONArray("weather")
                val desc = weather.getJSONObject(0).getString("description")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                val main = JSONObject(apiResponse).getJSONObject("main")
                val temp = main.getString("temp")

                val result_string: String = "$city\nТемпература: $temp°C\n$desc"
                user_result.text = result_string
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}

