package com.example.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    // TODO: 1) реализовать проверку на пустые значения в полях numA, numB
    // TODO: 2) исправить разметку, чтобы можно было вводить только числа
    // TODO: 3) сделать поддержку вещественных (не целых чисел)

    fun onClick(v: View) {
        val etA = findViewById<EditText>(R.id.numA)
        val etB = findViewById<EditText>(R.id.numB)
        val tvSum = findViewById<TextView>(R.id.sum)

        val strA = etA.text.toString().trim()
        val strB = etB.text.toString().trim()

        if (strA.isEmpty() || strB.isEmpty()) {
            if (strA.isEmpty()) etA.error = "Введите число"
            if (strB.isEmpty()) etB.error = "Введите число"
            Toast.makeText(this, "Заполните оба поля!", Toast.LENGTH_SHORT).show()
            return
        }

        val numA = strA.toDoubleOrNull()
        val numB = strB.toDoubleOrNull()

        if (numA == null || numB == null) {
            Toast.makeText(this, "Введите корректные числа!", Toast.LENGTH_SHORT).show()
            return
        }

        val sum = numA + numB
        val strSum = if (sum % 1.0 == 0.0) sum.toLong().toString() else sum.toString()
        tvSum.text = strSum
    }
}