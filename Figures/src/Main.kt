fun main() {
    val figures: Array<Figure> = arrayOf(
        Rect(2, 2, 4, 2),
        Square(1, 1, 4),
        Circle(3, 3, 5)
    )

    println("Исходные фигуры")
    for (figure in figures) {
        println(figure)
    }

    println("\nТестирование Rect")
    val rect = Rect(2, 2, 4, 2)
    println("Исходный: $rect")

    rect.move(3, 1)
    println("После перемещения move(3, 1): $rect")

    rect.resize(2)
    println("После масштабирования resize(2): $rect")

    val rectGeoGebra = Rect(2, 2, 4, 2)
    println("\nИсходный прямоугольник для теста из GeoGebra: $rectGeoGebra")

    val rectCCW = Rect(rectGeoGebra)
    rectCCW.rotate(RotateDirection.CounterClockwise, 3, -3)
    println("Поворот против часовой вокруг (3, -3): $rectCCW")

    val rectCW = Rect(rectGeoGebra)
    rectCW.rotate(RotateDirection.Clockwise, 3, -3)
    println("Поворот по часовой вокруг (3, -3): $rectCW")

    println("\nТестирование Square")
    val square = Square(1, 1, 4)
    println("Исходный: $square")

    square.move(2, -1)
    println("После перемещения move(2, -1): $square")

    square.resize(2)
    println("После масштабирования resize(2): $square")

    square.rotate(RotateDirection.Clockwise, 0, 0)
    println("После поворота по часовой вокруг (0, 0): $square")

    println("\nТестирование Circle")
    val circle = Circle(3, 3, 5)
    println("Исходный: $circle")

    circle.move(-2, 4)
    println("После перемещения move(-2, 4): $circle")

    circle.resize(2)
    println("После масштабирования resize(2): $circle")

    circle.rotate(RotateDirection.Clockwise, 0, 0)
    println("После поворота по часовой вокруг (0, 0): $circle")

    val defaultCircle = Circle()
    println("Круг по умолчанию: $defaultCircle")
}