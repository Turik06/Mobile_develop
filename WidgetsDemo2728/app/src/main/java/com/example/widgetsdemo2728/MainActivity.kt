package com.example.widgetsdemo2728

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.widgetsdemo2728.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentWeather: WeatherInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCitySpinner(savedInstanceState)
        setupTemperatureScale()
        setupWindDirectionCheckbox()
        setupRefreshButton()
    }

    private fun setupCitySpinner(savedInstanceState: Bundle?) {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.cities,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.citySpinner.adapter = adapter

        val savedCityPosition = savedInstanceState?.getInt(KEY_CITY_POSITION, 0) ?: 0
        binding.citySpinner.setSelection(savedCityPosition)

        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = parent?.getItemAtPosition(position).toString()
                loadWeatherData(selectedCity)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupTemperatureScale() {
        binding.tempScaleRadioGroup.setOnCheckedChangeListener { _, _ ->
            updateTemperatureDisplay()
        }
    }

    private fun setupWindDirectionCheckbox() {
        binding.cbWindDirection.setOnCheckedChangeListener { _, isChecked ->
            binding.tvWindDirection.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun setupRefreshButton() {
        binding.btnRefresh.setOnClickListener {
            val city = binding.citySpinner.selectedItem?.toString() ?: return@setOnClickListener
            loadWeatherData(city)
        }
    }

    private fun loadWeatherData(city: String) {
        binding.progressBar.visibility = View.VISIBLE
        val apiKey = getString(R.string.open_weather_api_key)

        lifecycleScope.launch {
            try {
                val weather = WeatherRepository.loadWeather(this@MainActivity, city, apiKey)
                currentWeather = weather
                displayWeatherData(weather)
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun displayWeatherData(weather: WeatherInfo) {
        binding.tvCityName.text = weather.city
        binding.tvDescription.text = weather.description
        binding.tvWindSpeed.text = getString(R.string.wind_speed_text, weather.windSpeed)
        binding.tvWindDirection.text = getString(R.string.wind_direction_text, weather.windDirectionText)
        binding.tvWindDirection.visibility = if (binding.cbWindDirection.isChecked) View.VISIBLE else View.GONE
        binding.tvDataSource.text = weather.source

        updateTemperatureDisplay()
    }

    private fun updateTemperatureDisplay() {
        val weather = currentWeather ?: return
        val isCelsius = binding.rbCelsius.isChecked
        binding.tvTemperature.text = weather.formattedTemperature(isCelsius)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CITY_POSITION, binding.citySpinner.selectedItemPosition)
        outState.putBoolean(KEY_CELSIUS_CHECKED, binding.rbCelsius.isChecked)
        outState.putBoolean(KEY_WIND_CHECKED, binding.cbWindDirection.isChecked)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val isCelsius = savedInstanceState.getBoolean(KEY_CELSIUS_CHECKED, true)
        if (isCelsius) {
            binding.rbCelsius.isChecked = true
        } else {
            binding.rbFahrenheit.isChecked = true
        }
        val isWindChecked = savedInstanceState.getBoolean(KEY_WIND_CHECKED, true)
        binding.cbWindDirection.isChecked = isWindChecked
        binding.tvWindDirection.visibility = if (isWindChecked) View.VISIBLE else View.GONE
        updateTemperatureDisplay()
    }

    companion object {
        private const val KEY_CITY_POSITION = "key_city_position"
        private const val KEY_CELSIUS_CHECKED = "key_celsius_checked"
        private const val KEY_WIND_CHECKED = "key_wind_checked"
    }
}
