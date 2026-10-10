fun mergeArrays(a1: IntArray, a2: IntArray): IntArray {
    val result = IntArray(a1.size + a2.size)
    var i = 0
    var j = 0
    var k = 0

    while (i < a1.size && j < a2.size) {
        if (a1[i] <= a2[j]) {
            result[k] = a1[i]
            i++
        } else {
            result[k] = a2[j]
            j++
        }
        k++
    }

    while (i < a1.size) {
        result[k] = a1[i]
        i++
        k++
    }

    while (j < a2.size) {
        result[k] = a2[j]
        j++
        k++
    }

    return result
}

fun main() {
    val a1 = intArrayOf(0, 2, 2)
    val a2 = intArrayOf(1, 3)
    val res1 = mergeArrays(a1, a2)
    println("Пример из задания: " + res1.contentToString())

    val res2 = mergeArrays(intArrayOf(), intArrayOf())
    println("Оба пустые: " + res2.contentToString())

    val res3 = mergeArrays(intArrayOf(), intArrayOf(1, 5, 9))
    println("Один пустой: " + res3.contentToString())

    val b1 = intArrayOf(1, 3, 5, 7)
    val b2 = intArrayOf(2, 4, 6, 8, 10)
    println("Слияние: " + mergeArrays(b1, b2).contentToString())

    val c1 = intArrayOf(-5, -2, 0)
    val c2 = intArrayOf(-3, 1, 4)
    println("С отрицательными: " + mergeArrays(c1, c2).contentToString())
}
