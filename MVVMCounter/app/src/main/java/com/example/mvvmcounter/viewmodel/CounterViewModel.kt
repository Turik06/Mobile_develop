package com.example.mvvmcounter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmcounter.model.CounterModel

/**
 * ViewModel — связующее звено между Model и View.
 * Хранит состояние счётчика и предоставляет LiveData для наблюдения из View.
 */
class CounterViewModel : ViewModel() {

    private val model = CounterModel()

    private val _counter = MutableLiveData(model.count)

    /** Неизменяемая LiveData для наблюдения из Activity/Fragment */
    val counter: LiveData<Int> get() = _counter

    fun increment() {
        model.increment()
        _counter.value = model.count
    }

    fun decrement() {
        model.decrement()
        _counter.value = model.count
    }

    fun reset() {
        model.reset()
        _counter.value = model.count
    }
}
