package com.example.aviaseils

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Инициализация элементов интерфейса
        val spinnerDeparture = findViewById<Spinner>(R.id.spinnerDeparture)
        val spinnerArrival = findViewById<Spinner>(R.id.spinnerArrival)
        val etDepartureDate = findViewById<EditText>(R.id.etDepartureDate)
        val etReturnDate = findViewById<EditText>(R.id.etReturnDate)
        val etAdults = findViewById<EditText>(R.id.etAdults)
        val etChildren = findViewById<EditText>(R.id.etChildren)
        val etInfants = findViewById<EditText>(R.id.etInfants)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        // По умолчанию выберем разные города (0 и 1)
        if (spinnerArrival.adapter != null && spinnerArrival.adapter.count > 1) {
            spinnerArrival.setSelection(1)
        }

        // Значения пассажиров по умолчанию
        etAdults.setText("1")
        etChildren.setText("0")
        etInfants.setText("0")

        // Выбор даты вылета через DatePickerDialog (виджет выбора даты)
        etDepartureDate.setOnClickListener {
            showDatePickerDialog(etDepartureDate)
        }

        // Выбор даты возврата через DatePickerDialog (виджет выбора даты)
        etReturnDate.setOnClickListener {
            showDatePickerDialog(etReturnDate)
        }

        // Обработка нажатия кнопки поиска
        btnSearch.setOnClickListener {
            val departureCity = spinnerDeparture.selectedItem?.toString().orEmpty()
            val arrivalCity = spinnerArrival.selectedItem?.toString().orEmpty()
            val departureDate = etDepartureDate.text.toString().trim()
            val returnDate = etReturnDate.text.toString().trim()
            val adults = etAdults.text.toString().trim().ifEmpty { "1" }
            val children = etChildren.text.toString().trim().ifEmpty { "0" }
            val infants = etInfants.text.toString().trim().ifEmpty { "0" }

            if (departureCity == arrivalCity) {
                Toast.makeText(this, getString(R.string.error_same_cities), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (departureDate.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_no_departure_date), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val returnDateFormatted = if (returnDate.isNotEmpty()) "\nОбратно: $returnDate" else ""
            val totalPassengers = "взр: $adults, дет: $children, млад: $infants"

            val summaryMessage = getString(
                R.string.search_success_format,
                departureCity,
                arrivalCity,
                departureDate,
                returnDateFormatted,
                totalPassengers
            )

            Toast.makeText(this, summaryMessage, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Отображает диалоговый виджет выбора даты (DatePickerDialog)
     * и записывает выбранную дату в переданный EditText.
     */
    private fun showDatePickerDialog(targetField: EditText) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format(
                    Locale.getDefault(),
                    "%02d.%02d.%04d",
                    dayOfMonth,
                    month + 1,
                    year
                )
                targetField.setText(formattedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Ограничиваем выбор дат: нельзя выбрать прошедшую дату
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }
}
