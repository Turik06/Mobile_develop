package com.example.guessthenumber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class GameActivity : AppCompatActivity() {
    var begin: Int = 0
    var end: Int = 100
    private var isGameOver: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        begin = intent.getIntExtra("begin", 0)
        end = intent.getIntExtra("end", 100)
        Log.d("mytag", "begin = $begin")
        Log.d("mytag", "end = $end")

        updateQuestion()
    }

    private fun updateQuestion() {
        val tvQuestion = findViewById<TextView>(R.id.question)
        if (begin == end) {
            showResult(begin)
        } else if (end - begin > 1) {
            val mid = (begin + end) / 2
            tvQuestion.text = "Ваше число больше $mid?"
        } else {
            // если осталось два соседних числа, спрашиваем Ваше число ... ?
            tvQuestion.text = "Ваше число $begin?"
        }
    }

    fun onYesNoClick(view: View) {
        if (isGameOver) {
            finish()
            return
        }

        // Проверяем, что разница между begin end > 1
        if (end - begin > 1) {
            val mid = (begin + end) / 2
            // В зависимости от ответа пользователя изменяем границы диапазона
            when (view.id) {
                R.id.yes -> {
                    Log.d("mytag", "yes: число > $mid")
                    begin = mid
                }
                R.id.no -> {
                    Log.d("mytag", "no: число <= $mid")
                    end = mid
                }
            }
            updateQuestion()
        } else {
            // Если осталось два соседних числа, спрашиваем: Ваше число begin?
            when (view.id) {
                R.id.yes -> {
                    Log.d("mytag", "yes: число = $begin")
                    showResult(begin)
                }
                R.id.no -> {
                    Log.d("mytag", "no: число = $end")
                    showResult(end)
                }
            }
        }
    }

    private fun showResult(result: Int) {
        isGameOver = true
        val tvQuestion = findViewById<TextView>(R.id.question)
        val btnYes = findViewById<Button>(R.id.yes)
        val btnNo = findViewById<Button>(R.id.no)

        tvQuestion.text = "Ваше число: $result!"
        btnYes.text = "Заново"
        btnNo.visibility = View.GONE
    }
}