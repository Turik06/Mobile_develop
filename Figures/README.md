Исходные фигуры
Rect(x=2, y=2, width=4, height=2, area=8.0)
Square(x=1, y=1, side=4, area=16.0)
Circle(x=3, y=3, radius=5, area=78.53982)

Тестирование Rect
Исходный: Rect(x=2, y=2, width=4, height=2, area=8.0)
После перемещения move(3, 1): Rect(x=5, y=3, width=4, height=2, area=8.0)
После масштабирования resize(2): Rect(x=5, y=3, width=8, height=4, area=32.0)

Исходный прямоугольник для теста из GeoGebra: Rect(x=2, y=2, width=4, height=2, area=8.0)
Поворот против часовой вокруг (3, -3): Rect(x=-4, y=-4, width=2, height=4, area=8.0)
Поворот по часовой вокруг (3, -3): Rect(x=8, y=-6, width=2, height=4, area=8.0)

Тестирование Square
Исходный: Square(x=1, y=1, side=4, area=16.0)
После перемещения move(2, -1): Square(x=3, y=0, side=4, area=16.0)
После масштабирования resize(2): Square(x=3, y=0, side=8, area=64.0)
После поворота по часовой вокруг (0, 0): Square(x=0, y=-11, side=8, area=64.0)

Тестирование Circle
Исходный: Circle(x=3, y=3, radius=5, area=78.53982)
После перемещения move(-2, 4): Circle(x=1, y=7, radius=5, area=78.53982)
После масштабирования resize(2): Circle(x=1, y=7, radius=10, area=314.15927)
После поворота по часовой вокруг (0, 0): Circle(x=7, y=-1, radius=10, area=314.15927)
Круг по умолчанию: Circle(x=0, y=0, radius=1, area=3.1415927)
