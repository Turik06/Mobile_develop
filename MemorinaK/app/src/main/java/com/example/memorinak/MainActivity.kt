package com.example.memorinak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // картинки для карточек
    private val images = listOf(
        R.drawable.card_apple,
        R.drawable.card_star,
        R.drawable.card_heart,
        R.drawable.card_diamond,
        R.drawable.card_tree,
        R.drawable.card_cross,
        R.drawable.card_sun,
        R.drawable.card_cloud
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(16, 16, 16, 16)
        }

        // создаем колоду из пар картинок и перемешиваем
        val deck = ArrayList<Int>()
        for (img in images) {
            deck.add(img)
            deck.add(img)
        }
        deck.shuffle()

        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            weight = 1f
            setMargins(8, 8, 8, 8)
        }

        // создаем 16 карт, изначально рубашкой вверх
        val cardViews = ArrayList<ImageView>()
        for (i in 0 until 16) {
            cardViews.add(
                ImageView(this).apply {
                    setImageResource(R.drawable.card_back)
                    layoutParams = params
                    adjustViewBounds = true
                    tag = deck[i] // в тег кладем ресурс лицевой стороны
                    setOnClickListener(colorListener)
                }
            )
        }

        // раскладываем по 4 карты в 4 строки
        val rows = Array(4) { LinearLayout(this) }
        var count = 0
        for (view in cardViews) {
            val row = count / 4
            rows[row].addView(view)
            count++
        }

        for (row in rows) {
            layout.addView(row)
        }

        setContentView(layout)
    }

    // переворот карты с задержкой
    suspend fun setBackgroundWithDelay(v: View) {
        val iv = v as? ImageView ?: return
        val faceRes = iv.tag as? Int ?: return

        // открываем карту
        iv.setImageResource(faceRes)
        iv.isClickable = false

        delay(1000)

        // закрываем обратно
        iv.setImageResource(R.drawable.card_back)
        iv.isClickable = true
    }

    val colorListener = View.OnClickListener {
        GlobalScope.launch(Dispatchers.Main) {
            setBackgroundWithDelay(it)
        }
    }
}