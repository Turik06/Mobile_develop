package com.example.mvvmcounter.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmcounter.databinding.ActivityMainBinding
import com.example.mvvmcounter.viewmodel.CounterViewModel

/**
 * View — отображает данные и обрабатывает действия пользователя.
 * Не содержит бизнес-логики — только подписка на LiveData из ViewModel.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Наблюдаем за изменениями счётчика в ViewModel
        viewModel.counter.observe(this) { count ->
            binding.tvCounter.text = count.toString()
        }

        // Обработка нажатий — делегируем всё ViewModel
        binding.btnIncrement.setOnClickListener {
            viewModel.increment()
        }

        binding.btnDecrement.setOnClickListener {
            viewModel.decrement()
        }

        binding.btnReset.setOnClickListener {
            viewModel.reset()
        }
    }
}
