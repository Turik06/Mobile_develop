class Square(var x: Int, var y: Int, var side: Int) : Figure(0), Movable, Transforming {
    var color: Int = -1
    lateinit var name: String

    constructor(square: Square) : this(square.x, square.y, square.side)

    override fun move(dx: Int, dy: Int) {
        x += dx
        y += dy
    }

    override fun area(): Float {
        return (side * side).toFloat()
    }

    override fun resize(zoom: Int) {
        side *= zoom
    }

    override fun rotate(direction: RotateDirection, centerX: Int, centerY: Int) {
        val centerFigureX = x + side / 2
        val centerFigureY = y + side / 2

        val dx = centerFigureX - centerX
        val dy = centerFigureY - centerY

        val newCenterX: Int
        val newCenterY: Int
        when (direction) {
            RotateDirection.Clockwise -> {
                newCenterX = centerX + dy
                newCenterY = centerY - dx
            }
            RotateDirection.CounterClockwise -> {
                newCenterX = centerX - dy
                newCenterY = centerY + dx
            }
        }

        x = newCenterX - side / 2
        y = newCenterY - side / 2
    }

    override fun toString(): String {
        return "Square(x=$x, y=$y, side=$side, area=${area()})"
    }
}