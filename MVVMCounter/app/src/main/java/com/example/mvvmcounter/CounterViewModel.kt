package com.example.mvvmcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    private val model = CounterModel()

    private val _counter = MutableLiveData(model.count)
    val counter: LiveData<Int> = _counter

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
