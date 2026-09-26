import java.util.Scanner
fun main(){
    val sc: Scanner = Scanner(System.`in`);
    val k = sc.nextInt()
    val x = sc.nextInt()
    val y = sc.nextInt()
    val n = when {
        x >= k && x - y >= 2 -> 0
        y >= k && y - x >= 2 -> 0
        else -> minOf(
            maxOf(k - x, y + 2 - x),
            maxOf(k - y, x + 2 - y)
        )
    }
    println(n)
}