fun moveRobot(robot: Robot, toX: Int, toY: Int) {
    val targetDirectionX = if (toX > robot.x) Direction.RIGHT else Direction.LEFT
    if (toX != robot.x) {
        while (robot.direction != targetDirectionX) {
            robot.turnRight()
        }
        while (robot.x != toX) {
            robot.stepForward()
        }
    }

    val targetDirectionY = if (toY > robot.y) Direction.UP else Direction.DOWN
    if (toY != robot.y) {
        while (robot.direction != targetDirectionY) {
            robot.turnRight()
        }
        while (robot.y != toY) {
            robot.stepForward()
        }
    }
}

fun main() {

    val robot = Robot(0, 0, Direction.UP)
    println("Начальное состояние: $robot")

    //Перемещение в точку (3, 5)
    println("\n moveRobot(robot, 3, 5)")
    moveRobot(robot, 3, 5)
    println("После перемещения: $robot")

    //Перемещение в точку (-2, -3)
    println("\n moveRobot(robot, -2, -3)")
    moveRobot(robot, -2, -3)
    println("После перемещения: $robot")

    // Перемещение в ту же точку 
    println("\n vmoveRobot(robot, -2, -3)")
    moveRobot(robot, -2, -3)
    println("После перемещения: $robot")

    // Перемещение только по одной оси
    println("\n moveRobot(robot, -2, 10)")
    moveRobot(robot, -2, 10)
    println("После перемещения: $robot")

    // Робот начинает с другого направления
    println("\n новый робот (5, 5, LEFT) -> (0, 0)")
    val robot2 = Robot(5, 5, Direction.LEFT)
    println("Начальное состояние: $robot2")
    moveRobot(robot2, 0, 0)
    println("После перемещения: $robot2")
}
