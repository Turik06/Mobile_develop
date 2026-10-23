package com.example.randomfilmk

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.util.Locale
import java.util.Random

class MainActivity : AppCompatActivity() {

    lateinit var movies: Array<String>
    private val random = Random()

    private val remainingIndices = mutableListOf<Int>()
    private var currentMovieIndex: Int? = null
    private var isFinished: Boolean = false

    private lateinit var tvTitle: TextView
    private lateinit var tvRemaining: TextView
    private lateinit var btnNext: MaterialButton
    private lateinit var btnReset: MaterialButton
    private lateinit var btnToggleLang: MaterialButton
    private lateinit var tvHeaderTitle: TextView
    private lateinit var tvMovieLabel: TextView

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val PREF_KEY_LANG = "app_lang"
        private const val KEY_REMAINING_INDICES = "KEY_REMAINING_INDICES"
        private const val KEY_CURRENT_INDEX = "KEY_CURRENT_INDEX"
        private const val KEY_IS_FINISHED = "KEY_IS_FINISHED"
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedLang = prefs.getString(PREF_KEY_LANG, null)
        if (savedLang != null) {
            val locale = Locale(savedLang)
            Locale.setDefault(locale)
            val config = Configuration(newBase.resources.configuration)
            config.setLocale(locale)
            val context = newBase.createConfigurationContext(config)
            super.attachBaseContext(context)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTitle = findViewById(R.id.title)
        tvRemaining = findViewById(R.id.tvRemaining)
        btnNext = findViewById(R.id.btnNext)
        btnReset = findViewById(R.id.btnReset)
        btnToggleLang = findViewById(R.id.btnToggleLang)
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle)
        tvMovieLabel = findViewById(R.id.tvMovieLabel)

        movies = resources.getStringArray(R.array.movies)

        if (savedInstanceState != null) {
            val savedList = savedInstanceState.getIntegerArrayList(KEY_REMAINING_INDICES)
            remainingIndices.clear()
            if (savedList != null) {
                remainingIndices.addAll(savedList)
            }
            if (savedInstanceState.containsKey(KEY_CURRENT_INDEX)) {
                currentMovieIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX)
            }
            isFinished = savedInstanceState.getBoolean(KEY_IS_FINISHED, false)
            updateUIState()
        } else {
            resetMovies()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList(KEY_REMAINING_INDICES, ArrayList(remainingIndices))
        currentMovieIndex?.let { outState.putInt(KEY_CURRENT_INDEX, it) }
        outState.putBoolean(KEY_IS_FINISHED, isFinished)
    }

    private fun resetMovies() {
        movies = resources.getStringArray(R.array.movies)
        remainingIndices.clear()
        for (i in movies.indices) {
            remainingIndices.add(i)
        }
        currentMovieIndex = null
        isFinished = false

        tvTitle.text = getString(R.string.initial_hint)
        tvRemaining.text = getString(R.string.remaining_count, remainingIndices.size, movies.size)
        btnNext.isEnabled = true
    }

    private fun updateUIState() {
        movies = resources.getStringArray(R.array.movies)
        if (isFinished) {
            tvTitle.text = getString(R.string.all_movies_shown)
            btnNext.isEnabled = false
        } else if (currentMovieIndex != null && currentMovieIndex!! in movies.indices) {
            tvTitle.text = movies[currentMovieIndex!!]
            btnNext.isEnabled = true
        } else {
            tvTitle.text = getString(R.string.initial_hint)
            btnNext.isEnabled = true
        }
        tvRemaining.text = getString(R.string.remaining_count, remainingIndices.size, movies.size)
    }

    fun onNextClick(view: View) {
        if (remainingIndices.isEmpty()) {
            isFinished = true
            tvTitle.text = getString(R.string.all_movies_shown)
            btnNext.isEnabled = false
            return
        }

        val randomIndexInList = random.nextInt(remainingIndices.size)
        val selectedIndex = remainingIndices.removeAt(randomIndexInList)
        currentMovieIndex = selectedIndex

        tvTitle.text = movies[selectedIndex]
        tvRemaining.text = getString(R.string.remaining_count, remainingIndices.size, movies.size)
    }

    fun onClearClick(view: View) {
        resetMovies()
    }

    fun onToggleLanguage(view: View) {
        val currentLang = resources.configuration.locales[0].language
        val targetLang = if (currentLang == "en") "ru" else "en"

        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_KEY_LANG, targetLang)
            .commit()

        recreate()
    }
}
