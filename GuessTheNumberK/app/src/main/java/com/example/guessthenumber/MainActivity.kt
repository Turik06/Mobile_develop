package com.example.guessthenumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onGuessClick(view: View) {
        val etBegin = findViewById<EditText>(R.id.begin)
        val etEnd = findViewById<EditText>(R.id.end)

        var begin = etBegin.text.toString().toIntOrNull() ?: 0
        var end = etEnd.text.toString().toIntOrNull() ?: 100

        if (begin > end) {
            Toast.makeText(this, "Левая граница больше правой, меняем местами", Toast.LENGTH_SHORT).show()
            val temp = begin
            begin = end
            end = temp
        }

        val intent = Intent(this, GameActivity::class.java)
        // Получаем данные из полей ввода и передаем во вторую активность
        intent.putExtra("begin", begin)
        intent.putExtra("end", end)
        startActivity(intent)
    }
}