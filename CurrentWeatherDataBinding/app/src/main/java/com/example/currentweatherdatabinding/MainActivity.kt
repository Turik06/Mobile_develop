package com.example.currentweatherdatabinding

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.currentweatherdatabinding.databinding.ActivityMainBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.activity = this
        binding.cities.adapter = ArrayAdapter.createFromResource(
            this, R.array.cities, android.R.layout.simple_spinner_dropdown_item
        )
        binding.cities.setSelection(savedInstanceState?.getInt("city") ?: 0)
        binding.cities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadWeather()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun loadWeather() {
        if (binding.loading == true) return
        binding.weather = null
        binding.message = null
        val apiKey = getString(R.string.open_weather_api_key).trim()
        if (apiKey.isEmpty()) {
            binding.message = getString(R.string.api_key_missing)
            return
        }
        val city = binding.cities.selectedItem?.toString() ?: return
        binding.loading = true
        scope.launch {
            try {
                binding.weather = WeatherApi.loadWeather(city, apiKey)
            } catch (error: CancellationException) {
                throw error
            } catch (error: WeatherHttpException) {
                binding.message = getString(when (error.statusCode) {
                    401 -> R.string.api_key_invalid
                    404 -> R.string.city_not_found
                    else -> R.string.server_error
                })
            } catch (error: IOException) {
                binding.message = getString(R.string.network_error)
            } catch (error: Exception) {
                binding.message = getString(R.string.invalid_response)
            } finally {
                binding.loading = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("city", binding.cities.selectedItemPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
