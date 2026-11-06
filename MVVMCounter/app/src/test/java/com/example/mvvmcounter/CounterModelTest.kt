package com.example.mvvmcounter

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CounterModelTest {

    private lateinit var model: CounterModel

    @Before
    fun setUp() {
        model = CounterModel()
    }

    @Test
    fun initialCountIsZero() {
        assertEquals(0, model.count)
    }

    @Test
    fun incrementIncreasesCount() {
        model.increment()
        assertEquals(1, model.count)
        model.increment()
        assertEquals(2, model.count)
    }

    @Test
    fun decrementDecreasesCount() {
        model.decrement()
        assertEquals(-1, model.count)
    }

    @Test
    fun resetSetsCountToZero() {
        model.increment()
        model.increment()
        model.reset()
        assertEquals(0, model.count)
    }
}
