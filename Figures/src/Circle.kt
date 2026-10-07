class Circle(var x: Int = 0, var y: Int = 0, var radius: Int = 1) : Figure(0), Movable, Transforming {
    var color: Int = -1
    lateinit var name: String

    constructor(circle: Circle) : this(circle.x, circle.y, circle.radius)

    override fun move(dx: Int, dy: Int) {
        x += dx
        y += dy
    }

    override fun area(): Float {
        return (Math.PI * radius * radius).toFloat()
    }

    override fun resize(zoom: Int) {
        radius *= zoom
    }

    override fun rotate(direction: RotateDirection, centerX: Int, centerY: Int) {
        val dx = x - centerX
        val dy = y - centerY
        when (direction) {
            RotateDirection.Clockwise -> {
                x = centerX + dy
                y = centerY - dx
            }
            RotateDirection.CounterClockwise -> {
                x = centerX - dy
                y = centerY + dx
            }
        }
    }

    override fun toString(): String {
        return "Circle(x=$x, y=$y, radius=$radius, area=${area()})"
    }
}