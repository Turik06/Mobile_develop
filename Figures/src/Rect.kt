class Rect(var x: Int, var y: Int, var width: Int, var height: Int) : Movable, Figure(0), Transforming {
    var color: Int = -1
    lateinit var name: String

    constructor(rect: Rect) : this(rect.x, rect.y, rect.width, rect.height)

    override fun move(dx: Int, dy: Int) {
        x += dx
        y += dy
    }

    override fun area(): Float {
        return (width * height).toFloat()
    }

    override fun resize(zoom: Int) {
        width *= zoom
        height *= zoom
    }

    override fun rotate(direction: RotateDirection, centerX: Int, centerY: Int) {
        val centerFigureX = x + width / 2
        val centerFigureY = y + height / 2

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

        val temp = width
        width = height
        height = temp

        x = newCenterX - width / 2
        y = newCenterY - height / 2
    }

    override fun toString(): String {
        return "Rect(x=$x, y=$y, width=$width, height=$height, area=${area()})"
    }
}