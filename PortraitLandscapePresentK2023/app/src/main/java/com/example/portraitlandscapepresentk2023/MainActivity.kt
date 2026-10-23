package com.example.portraitlandscapepresentk2023

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var iv: ImageView
    private lateinit var spinner: Spinner
    private lateinit var prefs: SharedPreferences

    // Массив картинок: car1, car2, car3
    private val pictures = intArrayOf(
        R.drawable.car1,
        R.drawable.car2,
        R.drawable.car3
    )

    // Текущий индекс выбранной картинки:
    // -1: пока картинка не выбрана (отображается исходная с котиком squarecat)
    // 0: car1, 1: car2, 2: car3
    private var currentPictureIndex: Int = -1
    private var isSpinnerTouched: Boolean = false

    companion object {
        private const val PREFS_NAME = "PicturePrefs"
        private const val KEY_SELECTED_PICTURE = "selected_picture"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv = findViewById(R.id.picture)
        spinner = findViewById(R.id.pictures_list)
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Загружаем сохраненный индекс картинки из SharedPreferences
        currentPictureIndex = prefs.getInt(KEY_SELECTED_PICTURE, -1)

        adapter = ArrayAdapter.createFromResource(this, R.array.pictures, R.layout.item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // TODO: заменять картинку на ту, которую установили при нажатии кнопки
        // иначе будет исходная с котиком
        if (currentPictureIndex in pictures.indices) {
            iv.setImageResource(pictures[currentPictureIndex])
            spinner.setSelection(currentPictureIndex)
        } else {
            iv.setImageResource(R.drawable.squarecat)
        }

        // Отслеживаем касание пользователя к Spinner, чтобы при старте активности
        // автоматический вызов onItemSelected не перезаписал исходного котика
        spinner.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_UP) {
                isSpinnerTouched = true
            }
            false
        }

        spinner.onItemSelectedListener = this
    }

    // TODO: картинку менять по очереди на следущую
    fun onChangePictureClick(v: View) {
        currentPictureIndex = if (currentPictureIndex in pictures.indices) {
            (currentPictureIndex + 1) % pictures.size
        } else {
            0
        }

        iv.setImageResource(pictures[currentPictureIndex])
        spinner.setSelection(currentPictureIndex)
        saveCurrentPicture(currentPictureIndex)
    }

    // TODO: заменить картинку в зависимости от выбора пользователя
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Если пользователь ещё не выбирал в Spinner и сохранена картинка котика, не меняем
        if (!isSpinnerTouched && currentPictureIndex == -1) {
            return
        }

        if (position in pictures.indices && position != currentPictureIndex) {
            currentPictureIndex = position
            iv.setImageResource(pictures[currentPictureIndex])
            saveCurrentPicture(currentPictureIndex)
            Toast.makeText(this, "выбран элемент $position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "не выбран элемент", Toast.LENGTH_SHORT).show()
    }

    private fun saveCurrentPicture(index: Int) {
        prefs.edit().putInt(KEY_SELECTED_PICTURE, index).apply()
    }
}
