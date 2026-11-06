package com.example.mvvmcounter

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val viewModel: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvCounter = findViewById<TextView>(R.id.tvCounter)
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnMinus = findViewById<Button>(R.id.btnMinus)
        val btnReset = findViewById<Button>(R.id.btnReset)

        viewModel.counter.observe(this) { count ->
            tvCounter.text = count.toString()
        }

        btnPlus.setOnClickListener {
            viewModel.increment()
        }

        btnMinus.setOnClickListener {
            viewModel.decrement()
        }

        btnReset.setOnClickListener {
            viewModel.reset()
        }
    }
}
