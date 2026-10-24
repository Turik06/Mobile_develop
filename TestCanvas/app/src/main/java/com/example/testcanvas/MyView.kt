package com.example.testcanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.Random

class MyView(context: Context?) : View(context) {

    private val p = Paint().apply { isAntiAlias = true }
    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 42f
    }
    private val r = Random()

    var layoutWidth = -1
    var layoutHeight = -1

    // Массив из логических значений 4x4
    val tiles = Array(4) { BooleanArray(4) { r.nextBoolean() } }
    val padding = 16f
    var tilesRect: Array<Array<Rect>>? = null

    var centerX = 150f
    var centerY = 150f

    companion object {
        const val TAG = "mytag"
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        layoutWidth = right - left
        layoutHeight = bottom - top

        Log.d(TAG, "=== onLayout ===")
        Log.d(TAG, "onLayout: changed=$changed, left=$left, top=$top, right=$right, bottom=$bottom")
        Log.d(TAG, "onLayout: w $layoutWidth, h $layoutHeight (в пикселях/px)")

        // Вычисляем размер плиток на основе полученных размеров экрана
        val gridSize = 4
        val boardSize = Math.min(layoutWidth, layoutHeight) * 0.7f
        val startX = (layoutWidth - boardSize) / 2f
        val startY = 400f
        val tileSize = (boardSize - padding * (gridSize + 1)) / gridSize

        tilesRect = Array(gridSize) { row ->
            Array(gridSize) { col ->
                val x = startX + padding + col * (tileSize + padding)
                val y = startY + padding + row * (tileSize + padding)
                Rect(x, y, tileSize, tileSize)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val canvasW = canvas.width
        val canvasH = canvas.height

        Log.d(TAG, "=== onDraw ===")
        Log.d(TAG, "onDraw: w $canvasW, h $canvasH (Canvas в пикселях/px)")
        Log.d(TAG, "onLayout: w $layoutWidth, h $layoutHeight (View в пикселях/px)")
        Log.d(TAG, "Сравнение: совпадают? ${layoutWidth == canvasW && layoutHeight == canvasH}")
        Log.d(TAG, "Единицы измерения: физические пиксели (px)")

        // Фон
        canvas.drawColor(Color.rgb(245, 245, 250))

        // Рисуем заголовок и информацию об измерении
        textPaint.textSize = 50f
        textPaint.isFakeBoldText = true
        textPaint.color = Color.rgb(30, 30, 80)
        canvas.drawText("Сравнение размеров View и Canvas", 40f, 90f, textPaint)

        textPaint.textSize = 38f
        textPaint.isFakeBoldText = false
        textPaint.color = Color.BLACK
        canvas.drawText("1. onLayout (View): w = $layoutWidth px, h = $layoutHeight px", 40f, 160f, textPaint)
        canvas.drawText("2. onDraw (Canvas): w = $canvasW px, h = $canvasH px", 40f, 220f, textPaint)

        val match = (layoutWidth == canvasW && layoutHeight == canvasH)
        textPaint.color = if (match) Color.rgb(0, 140, 60) else Color.RED
        textPaint.isFakeBoldText = true
        canvas.drawText("Результат: Размеры совпадают (${if (match) "Да" else "Нет"})", 40f, 280f, textPaint)

        textPaint.color = Color.rgb(80, 80, 80)
        textPaint.isFakeBoldText = false
        canvas.drawText("Единицы измерения: физические пиксели (px)", 40f, 340f, textPaint)

        // Отрисовка плиток 4x4
        tilesRect?.let { grid ->
            for (row in 0 until 4) {
                for (col in 0 until 4) {
                    val rect = grid[row][col]
                    p.color = if (tiles[row][col]) Color.rgb(52, 152, 219) else Color.rgb(231, 76, 60)
                    canvas.drawRect(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, p)
                }
            }
        }

        // Интерактивный кружок по касанию
        p.color = Color.rgb(46, 204, 113)
        canvas.drawCircle(centerX, centerY, 35f, p)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        centerX = event.x
        centerY = event.y

        // Переключение плитки при нажатии
        if (event.action == MotionEvent.ACTION_DOWN) {
            tilesRect?.let { grid ->
                for (row in 0 until 4) {
                    for (col in 0 until 4) {
                        if (grid[row][col].isInto(event.x, event.y)) {
                            tiles[row][col] = !tiles[row][col]
                            Log.d(TAG, "Клик по плитке [$row][$col]")
                        }
                    }
                }
            }
        }

        invalidate()
        return true
    }
}
