package com.example.snowflakes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.AsyncTask
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.sin
import kotlin.random.Random

/**
 * Модель снежинки.
 * @param x текущая координата X
 * @param y текущая координата Y
 * @param baseX базовая координата X, относительно которой происходит блуждание
 * @param baseVelocity базовая скорость падения в верхней части экрана (px/кадр)
 * @param radius радиус снежинки (px)
 * @param color цвет снежинки, полученный с помощью Color.rgb()
 * @param amplitude амплитуда колебаний по горизонтали (px)
 * @param frequency частота колебаний синусоиды
 * @param phase начальный сдвиг фазы
 */
data class Snowflake(
    var x: Float,
    var y: Float,
    var baseX: Float,
    val baseVelocity: Float,
    val radius: Float,
    val color: Int,
    val amplitude: Float,
    val frequency: Float,
    val phase: Float
)

@Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
open class Snowflakes(ctx: Context) : View(ctx) {

    private val tag = "Snowflakes"
    private var snow: Array<Snowflake> = emptyArray()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var h: Int = 1000
    private var w: Int = 1000
    private val random = Random(System.currentTimeMillis())

    private var moveTask: MoveTask? = null
    var isRunning: Boolean = false
        private set

    init {
        // Устанавливаем сглаживание для красивой отрисовки кругов
        paint.isAntiAlias = true
    }

    /**
     * Создание различных оттенков снежинок с использованием Color.rgb().
     */
    private fun createSnowflakeColor(r: Random): Int {
        val red = r.nextInt(190, 256)
        val green = r.nextInt(215, 256)
        val blue = 255 // высокий синий канал для морозного зимнего оттенка
        return Color.rgb(red, green, blue)
    }

    /**
     * Генерация новой снежинки сверху экрана.
     */
    private fun createSnowflake(r: Random, screenW: Int, screenH: Int, initialY: Float? = null): Snowflake {
        val radius = r.nextFloat() * 14f + 6f // радиус от 6 до 20 px
        val baseX = r.nextFloat() * screenW
        val y = initialY ?: (r.nextFloat() * screenH)
        val baseVelocity = r.nextFloat() * 8f + 6f // базовая скорость от 6 до 14 px
        val amplitude = r.nextFloat() * 25f + 10f // амплитуда синуса от 10 до 35 px
        val frequency = r.nextFloat() * 0.015f + 0.005f // частота блуждания
        val phase = r.nextFloat() * (2f * Math.PI.toFloat())
        val color = createSnowflakeColor(r)

        val x = (baseX + amplitude * sin(phase + frequency * y)).coerceIn(0f, screenW.toFloat())

        return Snowflake(
            x = x,
            y = y,
            baseX = baseX,
            baseVelocity = baseVelocity,
            radius = radius,
            color = color,
            amplitude = amplitude,
            frequency = frequency,
            phase = phase
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        w = right - left
        h = bottom - top

        if (w > 0 && h > 0) {
            // Создаем массив снежинок (50 шт) с разными размерами, скоростями и оттенками
            snow = Array(50) {
                createSnowflake(random, w, h)
            }
            Log.d(tag, "onLayout: экран ${w}x${h} px, создано снежинок: ${snow.size}")

            // Запускаем фоновую анимацию, если еще не запущена
            if (!isRunning) {
                startAnimation()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Тёмно-синий фон ночного зимнего неба
        canvas.drawColor(Color.rgb(15, 25, 55))

        // Отрисовка снежинок с их индивидуальными цветами и радиусами
        for (s in snow) {
            paint.color = s.color
            canvas.drawCircle(s.x, s.y, s.radius, paint)
        }
    }

    /**
     * Перемещение снежинок:
     * 1. Замедление падения к нижней границе экрана (вверху быстрее, внизу медленнее).
     * 2. Блуждания по горизонтали с использованием функции синуса.
     * 3. Респавн снежинки наверху при достижении нижней границы экрана.
     */
    fun moveSnowflakes() {
        if (h <= 0 || w <= 0) return

        for (s in snow) {
            // Фактор замедления: вверху (y ≈ 0) равен 1.0, внизу (y ≈ h) падает до 0.12
            val decelerationFactor = (1f - (s.y / h.toFloat())).coerceIn(0.12f, 1f)
            val currentVelocity = s.baseVelocity * decelerationFactor
            s.y += currentVelocity

            // Блуждание по горизонтали с помощью функции синуса
            s.x = s.baseX + s.amplitude * sin(s.phase + s.frequency * s.y)

            // Если снежинка достигла нижней границы экрана — перерождаем её вверху
            if (s.y >= h + s.radius) {
                s.y = -s.radius * 2f
                s.baseX = random.nextFloat() * w
                s.x = s.baseX
            }
        }

        // Запрос на перерисовку View
        invalidate()
    }

    /**
     * Запуск фонового потока анимации.
     */
    fun startAnimation() {
        if (isRunning) return
        isRunning = true
        Log.d(tag, "startAnimation: запуск MoveTask")
        moveTask = MoveTask(this)
        moveTask?.execute(33) // ~30 кадров в секунду (задержка 33 мс)
    }

    /**
     * Остановка фонового потока анимации.
     */
    fun stopAnimation() {
        if (!isRunning) return
        Log.d(tag, "stopAnimation: остановка MoveTask")
        isRunning = false
        moveTask?.cancel(true)
        moveTask = null
    }

    /**
     * Обработка нажатия на экран: пауза / возобновление падения снежинок.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isRunning) {
                stopAnimation()
                Log.d(tag, "onTouchEvent: пауза анимации по тапу")
            } else {
                startAnimation()
                Log.d(tag, "onTouchEvent: запуск анимации по тапу")
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    /**
     * Вторая часть задания: реализация фонового потока с помощью AsyncTask.
     * doInBackground выполняется в отдельном фоновом потоке,
     * а onProgressUpdate безопасно обновляет координаты снежинок на UI-потоке.
     */
    class MoveTask(private val snowflakesView: Snowflakes) : AsyncTask<Int, Unit, Unit>() {

        override fun doInBackground(vararg params: Int?) {
            val delay = (params.getOrNull(0) ?: 33).toLong()
            while (!isCancelled) {
                try {
                    Thread.sleep(delay)
                } catch (e: InterruptedException) {
                    break
                }
                publishProgress()
            }
        }

        override fun onProgressUpdate(vararg values: Unit?) {
            super.onProgressUpdate(*values)
            if (!isCancelled && snowflakesView.isRunning) {
                snowflakesView.moveSnowflakes()
            }
        }
    }
}
