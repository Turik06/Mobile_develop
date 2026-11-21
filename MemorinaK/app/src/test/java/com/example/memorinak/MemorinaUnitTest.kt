package com.example.memorinak

import org.junit.Test
import org.junit.Assert.*

class MemorinaUnitTest {

    @Test
    fun testDeckHas16CardsAnd8Pairs() {
        val cardImages = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        val deck = ArrayList<Int>()
        for (imageRes in cardImages) {
            deck.add(imageRes)
            deck.add(imageRes)
        }

        assertEquals(16, deck.size)
        for (imageRes in cardImages) {
            val count = deck.count { it == imageRes }
            assertEquals(2, count)
        }
    }

    @Test
    fun testDeckShufflePreservesPairs() {
        val cardImages = listOf(101, 102, 103, 104, 105, 106, 107, 108)
        val deck = ArrayList<Int>()
        for (imageRes in cardImages) {
            deck.add(imageRes)
            deck.add(imageRes)
        }
        deck.shuffle()

        assertEquals(16, deck.size)
        val uniqueCards = deck.distinct()
        assertEquals(8, uniqueCards.size)
    }

    @Test
    fun testGridRowsDistribution() {
        val totalCards = 16
        val cardsPerRow = 4
        val rowsCount = 4

        val rows = Array(rowsCount) { ArrayList<Int>() }
        for (i in 0 until totalCards) {
            val row = i / cardsPerRow
            rows[row].add(i)
        }

        assertEquals(4, rows.size)
        for (row in rows) {
            assertEquals(4, row.size)
        }
    }

    @Test
    fun testCardMatchingCondition() {
        val firstTag = 100
        val matchingSecondTag = 100
        val nonMatchingSecondTag = 200

        assertTrue(firstTag == matchingSecondTag)
        assertFalse(firstTag == nonMatchingSecondTag)
    }

    @Test
    fun testWinConditionWhenAllInvisible() {
        // Симулируем 16 карт
        val cardVisibility = MutableList(16) { 0 } // 0 - видима

        fun isWin(list: List<Int>) = list.all { it == 4 } // 4 - View.INVISIBLE

        assertFalse(isWin(cardVisibility))

        // Убираем все карты
        for (i in 0 until 16) {
            cardVisibility[i] = 4
        }

        assertTrue(isWin(cardVisibility))
    }
}
