package com.example.snowflakes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var snowflakesView: Snowflakes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Заменяем XML-разметку на кастомный View
        snowflakesView = Snowflakes(this)
        setContentView(snowflakesView)
    }

    override fun onPause() {
        super.onPause()
        snowflakesView.stopAnimation()
    }

    override fun onResume() {
        super.onResume()
        // Возобновляем анимацию при возврате на экран
        snowflakesView.startAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        snowflakesView.stopAnimation()
    }
}
