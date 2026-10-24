package com.example.testcanvas

data class Rect(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
) {
    fun isInto(pointX: Float, pointY: Float): Boolean {
        return pointX >= x && pointX <= x + width && pointY >= y && pointY <= y + height
    }
}
