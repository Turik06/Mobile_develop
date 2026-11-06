package com.example.mvvmcounter.model

/**
 * Model — хранит данные счётчика и бизнес-логику.
 */
class CounterModel {
    var count: Int = 0
        private set

    fun increment() {
        count++
    }

    fun decrement() {
        count--
    }

    fun reset() {
        count = 0
    }
}
