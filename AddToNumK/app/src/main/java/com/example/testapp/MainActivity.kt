package com.example.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var numA: EditText
    lateinit var numB: EditText
    lateinit var tvSum: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numA = findViewById(R.id.numA)
        numB = findViewById(R.id.numB)
        tvSum = findViewById(R.id.sum)
    }

    // TODO: 1) реализовать проверку на пустые значения в полях numA, numB
    // TODO: 2) исправить разметку, чтобы можно было вводить только числа
    // TODO: 3) сделать поддержку вещественных (не целых чисел)

    fun onClick(v: View) {
        val a = numA.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0
        val b = numB.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0
        val sum = a + b
        tvSum.text = sum.toString()
    }
}