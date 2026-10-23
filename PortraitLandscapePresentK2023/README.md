# PortraitLandscapePresentK2023

Лабораторная работа: **Адаптация разметки для альбомной/портретной ориентации**.  
Проект основан на официальном шаблоне [ipetrushin/PortraitLandscapePresentK2023](https://github.com/ipetrushin/PortraitLandscapePresentK2023).

---

## 📋 Описание задания

1. **Адаптация разметки при повороте устройства:**
   - В портретной ориентации используется вертикальная компоновка: изображение сверху, кнопка смены картинки и выпадающий список снизу ([`res/layout/activity_main.xml`](app/src/main/res/layout/activity_main.xml)).
   - В альбомной ориентации используется горизонтальная компоновка: изображение в левой части экрана, а кнопка и выпадающий список в правой панели ([`res/layout-land/activity_main.xml`](app/src/main/res/layout-land/activity_main.xml)).
2. **Смена картинок по кнопке:**
   - При нажатии на кнопку «Change picture» картинка циклически сменяется на следующую: `car1` &rarr; `car2` &rarr; `car3` &rarr; `car1`...
   - При нажатии кнопки положение в `Spinner` синхронно переключается.
3. **Выбор картинки через выпадающий список (Spinner):**
   - Выпадающий список заполняется из строкового массива `pictures` (`car 1`, `car 2`, `car 3`) с макетом [`res/layout/item.xml`](app/src/main/res/layout/item.xml).
   - Выбор элемента пользователем мгновенно меняет отображаемое изображение и выводит `Toast`.
4. **Сохранение состояния через SharedPreferences:**
   - Текущий индекс выбранной картинки сохраняется в `SharedPreferences` при любом изменении (по кнопке или через `Spinner`).
   - При перевороте экрана (пересоздании активности) или холодном перезапуске приложения сохранённое изображение автоматически восстанавливается.
   - При первом запуске отображается начальное изображение с котиком (`squarecat`).

---

## 💻 Исходный код

### `MainActivity.kt`

```kotlin
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
```

### Портретная разметка (`res/layout/activity_main.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".MainActivity">

    <ImageView
        android:id="@+id/picture"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:adjustViewBounds="true"
        android:scaleType="fitCenter"
        android:src="@drawable/squarecat" />

    <Button
        android:id="@+id/btn_change_picture"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Change picture"
        android:textSize="24sp"
        android:onClick="onChangePictureClick" />

    <Spinner
        android:id="@+id/pictures_list"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:layout_marginBottom="16dp"
        android:minHeight="48dp"
        android:entries="@array/pictures" />

</LinearLayout>
```

### Альбомная разметка (`res/layout-land/activity_main.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal"
    android:padding="16dp"
    tools:context=".MainActivity">

    <ImageView
        android:id="@+id/picture"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1.3"
        android:adjustViewBounds="true"
        android:scaleType="fitCenter"
        android:src="@drawable/squarecat" />

    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:orientation="vertical"
        android:gravity="center"
        android:layout_marginStart="16dp">

        <Button
            android:id="@+id/btn_change_picture"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Change picture"
            android:textSize="22sp"
            android:onClick="onChangePictureClick" />

        <Spinner
            android:id="@+id/pictures_list"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:minHeight="48dp"
            android:entries="@array/pictures" />

    </LinearLayout>

</LinearLayout>
```

### Макет элемента списка (`res/layout/item.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@android:id/text1"
    android:textSize="24sp"
    android:padding="12dp"
    android:textColor="?android:attr/textColorPrimary"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

### Строковые ресурсы (`res/values/strings.xml`)

```xml
<resources>
    <string name="app_name">PortraitLandscapePresentK2023</string>
    <string-array name="pictures">
        <item>car 1</item>
        <item>car 2</item>
        <item>car 3</item>
    </string-array>
</resources>
```

---

## 📱 Демонстрация работы приложения

| Скриншот | Описание действия |
|---|---|
| ![screenshot_1](screenshots/screenshot_1_initial_cat.png) | **1. Начальный запуск (портретная ориентация)**<br>Приложение запущено впервые, отображается исходное изображение с котиком (`squarecat`). |
| ![screenshot_2](screenshots/screenshot_2_click_car1.png) | **2. Нажатие «Change picture»**<br>Переключение на первое изображение машины (`car1`), спиннер установлен на `car 1`. |
| ![screenshot_3](screenshots/screenshot_3_click_car2.png) | **3. Повторное нажатие «Change picture»**<br>Циклическое переключение на следующее изображение (`car2`), спиннер синхронизирован на `car 2`. |
| ![screenshot_4](screenshots/screenshot_4_spinner_open.png) | **4. Открытие выпадающего списка Spinner**<br>Пользователь нажимает на выпадающий список со списком доступных машин. |
| ![screenshot_5](screenshots/screenshot_5_select_car3.png) | **5. Выбор «car 3» из списка**<br>Отображение обновляется на `car3`, выводится всплывающее сообщение `Toast` с индексом. |
| ![screenshot_6](screenshots/screenshot_6_landscape_adapted.png) | **6. Поворот экрана в альбомную ориентацию**<br>Активируется горизонтальная разметка `layout-land`: картинка слева, панель справа. Выбранная картинка (`car3`) сохранена! |
| ![screenshot_7](screenshots/screenshot_7_landscape_click_car1.png) | **7. Нажатие кнопки в альбомной ориентации**<br>Циклический переход на `car1`, разметка и элементы управления работают штатно. |
| ![screenshot_8](screenshots/screenshot_8_portrait_restored.png) | **8. Возврат в портретную ориентацию**<br>Активность пересоздана, состояние `car1` сохранено через `SharedPreferences`. |
| ![screenshot_9](screenshots/screenshot_9_app_reopened.png) | **9. Холодный перезапуск приложения**<br>Процесс был принудительно завершен и запущен заново: состояние `car1` успешно загружено из `SharedPreferences`. |
