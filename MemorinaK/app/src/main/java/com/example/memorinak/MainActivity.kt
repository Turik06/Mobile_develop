package com.example.memorinak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
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

    val cardViews = ArrayList<ImageView>()
    var firstCard: ImageView? = null
    var openCardsCount = 0

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
        Log.d("Memorina", "deck=$deck")

        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            weight = 1f
            setMargins(8, 8, 8, 8)
        }

        // создаем 16 карт, изначально рубашкой вверх
        for (i in 0 until 16) {
            val cardView = ImageView(this).apply {
                setImageResource(R.drawable.card_back)
                layoutParams = params
                adjustViewBounds = true
                tag = deck[i] // в тег кладем ресурс лицевой стороны
                setOnClickListener(colorListener)
            }
            cardViews.add(cardView)
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

    // проверка двух открытых карт
    suspend fun openCards(secondCard: ImageView) {
        val first = firstCard ?: return

        if (first.tag == secondCard.tag) {
            // если совпали - убираем с поля
            delay(500)
            first.visibility = View.INVISIBLE
            secondCard.visibility = View.INVISIBLE
            checkWin()
        } else {
            // если разные - ждем секунду и закрываем
            delay(1000)
            first.setImageResource(R.drawable.card_back)
            secondCard.setImageResource(R.drawable.card_back)
            first.isClickable = true
            secondCard.isClickable = true
        }

        firstCard = null
        openCardsCount = 0
    }

    // обработчик клика по карте
    val colorListener = View.OnClickListener { v ->
        val card = v as? ImageView ?: return@OnClickListener

        when (openCardsCount) {
            0 -> {
                // открыли первую карту
                firstCard = card
                card.setImageResource(card.tag as Int)
                card.isClickable = false
                openCardsCount = 1
            }
            1 -> {
                // открыли вторую карту
                card.setImageResource(card.tag as Int)
                card.isClickable = false
                openCardsCount = 2

                GlobalScope.launch(Dispatchers.Main) {
                    openCards(card)
                }
            }
            else -> {
                // две карты уже открыты, ничего не делаем
            }
        }
    }

    // проверка на выигрыш (когда все карты исчезли)
    fun checkWin() {
        if (cardViews.isNotEmpty() && cardViews.all { it.visibility == View.INVISIBLE }) {
            Toast.makeText(this, "Вы выиграли!", Toast.LENGTH_SHORT).show()
        }
    }
}