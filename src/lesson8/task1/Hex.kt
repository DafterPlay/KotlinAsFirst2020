@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Точка (гекс) на шестиугольной сетке.
 * Координаты заданы как в примере (первая цифра - y, вторая цифра - x)
 *
 *       60  61  62  63  64  65
 *     50  51  52  53  54  55  56
 *   40  41  42  43  44  45  46  47
 * 30  31  32  33  34  35  36  37  38
 *   21  22  23  24  25  26  27  28
 *     12  13  14  15  16  17  18
 *       03  04  05  06  07  08
 *
 * В примерах к задачам используются те же обозначения точек,
 * к примеру, 16 соответствует HexPoint(x = 6, y = 1), а 41 -- HexPoint(x = 1, y = 4).
 *
 * В задачах, работающих с шестиугольниками на сетке, считать, что они имеют
 * _плоскую_ ориентацию:
 *  __
 * /  \
 * \__/
 *
 * со сторонами, параллельными координатным осям сетки.
 *
 * Более подробно про шестиугольные системы координат можно почитать по следующей ссылке:
 *   https://www.redblobgames.com/grids/hexagons/
 */
data class HexPoint(val x: Int, val y: Int) {
    /**
     * Средняя (3 балла)
     *
     * Найти целочисленное расстояние между двумя гексами сетки.
     * Расстояние вычисляется как число единичных отрезков в пути между двумя гексами.
     * Например, путь межу гексами 16 и 41 (см. выше) может проходить через 25, 34, 43 и 42 и имеет длину 5.
     */
    fun distance(other: HexPoint): Int = (abs(x - other.x) + abs(y - other.y) + abs(x + y - other.x - other.y)) / 2

    override fun toString(): String = "$y.$x"
}

/**
 * Правильный шестиугольник на гексагональной сетке.
 * Как окружность на плоскости, задаётся центральным гексом и радиусом.
 * Например, шестиугольник с центром в 33 и радиусом 1 состоит из гексов 42, 43, 34, 24, 23, 32.
 */
data class Hexagon(val center: HexPoint, val radius: Int) {

    /**
     * Средняя (3 балла)
     *
     * Рассчитать расстояние между двумя шестиугольниками.
     * Оно равно расстоянию между ближайшими точками этих шестиугольников,
     * или 0, если шестиугольники имеют общую точку.
     *
     * Например, расстояние между шестиугольником A с центром в 31 и радиусом 1
     * и другим шестиугольником B с центром в 26 и радиуоом 2 равно 2
     * (расстояние между точками 32 и 24)
     */
    fun distance(other: Hexagon): Int {
        val num = center.distance(other.center) - abs(radius) - abs(other.radius)
        return when {
            num < 0 -> 0
            else -> num
        }
    }

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если заданная точка находится внутри или на границе шестиугольника
     */
    fun contains(point: HexPoint): Boolean = center.distance(point) <= radius

    /**
     * Возвращает точки, находящиеся на растоянии радиуса от центра
     */
    fun radiusBoundary(): List<HexPoint> {
        val answer = mutableListOf<HexPoint>()
        var hexPoint = HexPoint(center.x, center.y + radius)
        var direction = Direction.LEFT
        do {
            for (i in 0 until radius) {
                answer.add(hexPoint.move(direction, i))
            }
            hexPoint = hexPoint.move(direction, radius)
            direction = direction.next()
        } while (direction != Direction.LEFT)
        return answer
    }
}

/**
 * Прямолинейный отрезок между двумя гексами
 */
class HexSegment(val begin: HexPoint, val end: HexPoint) {
    /**
     * Простая (2 балла)
     *
     * Определить "правильность" отрезка.
     * "Правильным" считается только отрезок, проходящий параллельно одной из трёх осей шестиугольника.
     * Такими являются, например, отрезок 30-34 (горизонталь), 13-63 (прямая диагональ) или 51-24 (косая диагональ).
     * А, например, 13-26 не является "правильным" отрезком.
     */
    fun isValid(): Boolean = begin != end && (begin.y == end.y || begin.x == end.x ||
            begin.y - end.y == -(begin.x - end.x))

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление отрезка (см. описание класса Direction ниже).
     * Для "правильного" отрезка выбирается одно из первых шести направлений,
     * для "неправильного" -- INCORRECT.
     */
    fun direction(): Direction = when {
        begin == end -> Direction.INCORRECT
        begin.y == end.y -> when {
            begin.x - end.x < 0 -> Direction.RIGHT
            else -> Direction.LEFT
        }
        begin.x == end.x -> when {
            begin.y - end.y < 0 -> Direction.UP_RIGHT
            else -> Direction.DOWN_LEFT
        }
        begin.x + begin.y == end.x + end.y -> when (begin.y > end.y) {
            true -> Direction.DOWN_RIGHT
            else -> Direction.UP_LEFT
        }
        else -> Direction.INCORRECT
    }

    override fun equals(other: Any?) =
        other is HexSegment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Направление отрезка на гексагональной сетке.
 * Если отрезок "правильный", то он проходит вдоль одной из трёх осей шестугольника.
 * Если нет, его направление считается INCORRECT
 */
enum class Direction {
    RIGHT,      // слева направо, например 30 -> 34
    UP_RIGHT,   // вверх-вправо, например 32 -> 62
    UP_LEFT,    // вверх-влево, например 25 -> 61
    LEFT,       // справа налево, например 34 -> 30
    DOWN_LEFT,  // вниз-влево, например 62 -> 32
    DOWN_RIGHT, // вниз-вправо, например 61 -> 25
    INCORRECT;  // отрезок имеет изгиб, например 30 -> 55 (изгиб в точке 35)

    /**
     * Простая (2 балла)
     *
     * Вернуть направление, противоположное данному.
     * Для INCORRECT вернуть INCORRECT
     */
    fun opposite(): Direction = if (this == INCORRECT) INCORRECT else values()[(this.ordinal + 3) % 6]

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление, повёрнутое относительно
     * заданного на 60 градусов против часовой стрелки.
     *
     * Например, для RIGHT это UP_RIGHT, для UP_LEFT это LEFT, для LEFT это DOWN_LEFT.
     * Для направления INCORRECT бросить исключение IllegalArgumentException.
     * При решении этой задачи попробуйте обойтись без перечисления всех семи вариантов.
     */
    fun next(): Direction =
        if (this == INCORRECT) throw  IllegalArgumentException() else values()[(this.ordinal + 1) % 6]

    /**
     * Простая (2 балла)
     *
     * Вернуть true, если данное направление совпадает с other или противоположно ему.
     * INCORRECT не параллельно никакому направлению, в том числе другому INCORRECT.
     */
    fun isParallel(other: Direction): Boolean = other != INCORRECT && (other == this || other == this.opposite())
}

/**
 * Средняя (3 балла)
 *
 * Сдвинуть точку в направлении direction на расстояние distance.
 * Бросить IllegalArgumentException(), если задано направление INCORRECT.
 * Для расстояния 0 и направления не INCORRECT вернуть ту же точку.
 * Для отрицательного расстояния сдвинуть точку в противоположном направлении на -distance.
 *
 * Примеры:
 * 30, direction = RIGHT, distance = 3 --> 33
 * 35, direction = UP_LEFT, distance = 2 --> 53
 * 45, direction = DOWN_LEFT, distance = 4 --> 05
 */
fun HexPoint.move(direction: Direction, distance: Int): HexPoint = when (direction) {
    Direction.RIGHT -> HexPoint(this.x + distance, this.y)
    Direction.UP_RIGHT -> HexPoint(this.x, this.y + distance)
    Direction.UP_LEFT -> HexPoint(this.x - distance, this.y + distance)
    Direction.LEFT -> HexPoint(this.x - distance, this.y)
    Direction.DOWN_LEFT -> HexPoint(this.x, this.y - distance)
    Direction.DOWN_RIGHT -> HexPoint(this.x + distance, this.y - distance)
    else -> throw IllegalArgumentException()
}

/**
 * Сложная (5 баллов)
 *
 * Найти кратчайший путь между двумя заданными гексами, представленный в виде списка всех гексов,
 * которые входят в этот путь.
 * Начальный и конечный гекс также входят в данный список.
 * Если кратчайших путей существует несколько, вернуть любой из них.
 *
 * Пример (для координатной сетки из примера в начале файла):
 *   pathBetweenHexes(HexPoint(y = 2, x = 2), HexPoint(y = 5, x = 3)) ->
 *     listOf(
 *       HexPoint(y = 2, x = 2),
 *       HexPoint(y = 2, x = 3),
 *       HexPoint(y = 3, x = 3),
 *       HexPoint(y = 4, x = 3),
 *       HexPoint(y = 5, x = 3)
 *     )
 */
fun pathBetweenHexes(from: HexPoint, to: HexPoint): List<HexPoint> {
    if (from == to) return listOf(from)
    val distance = from.distance(to)
    val way = mutableListOf<HexPoint>()
    for (i in 0..distance) {
        way.add(
            HexPoint(
                from.x + (((to.x - from.x) * i / distance.toDouble()) - 1e-5).roundToInt(),
                from.y + (((to.y - from.y) * i / distance.toDouble())).roundToInt()
            )
        )
    }
    return way
}

/**
 * Очень сложная (20 баллов)
 *
 * Дано три точки (гекса). Построить правильный шестиугольник, проходящий через них
 * (все три точки должны лежать НА ГРАНИЦЕ, а не ВНУТРИ, шестиугольника).
 * Все стороны шестиугольника должны являться "правильными" отрезками.
 * Вернуть null, если такой шестиугольник построить невозможно.
 * Если шестиугольников существует более одного, выбрать имеющий минимальный радиус.
 *
 * Пример: через точки 13, 32 и 44 проходит правильный шестиугольник с центром в 24 и радиусом 2.
 * Для точек 13, 32 и 45 такого шестиугольника не существует.
 * Для точек 32, 33 и 35 следует вернуть шестиугольник радиусом 3 (с центром в 62 или 05).
 *
 * Если все три точки совпадают, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 */

fun hexagonByThreePoints(a: HexPoint, b: HexPoint, c: HexPoint): Hexagon? {
    if (a == b && b == c) return Hexagon(a, 0)
    val maxDistance = maxDistance(listOf(a, b, c))
    val lastHex = (listOf(a, b, c) - maxDistance.second.first - maxDistance.second.second).first()
    // Радиус, при котором все шестиугольники пересеклись
    var radius = (maxDistance.first + 1) / 2
    // Лист с точками, в которых пересекаются все границы
    var intersection = Hexagon(a, radius).radiusBoundary().intersect(Hexagon(b, radius).radiusBoundary()).intersect(
        Hexagon(c, radius).radiusBoundary()
    ).toList()
    // Пока не найдем точку пересечения или не достигнем максимального расстояния между центрами
    while (intersection.isEmpty() && radius != maxDistance.first) {
        radius++
        intersection =
            Hexagon(maxDistance.second.first, radius).radiusBoundary()
                .filter { it.distance(lastHex) == radius && it.distance(maxDistance.second.second) == radius }
    }
    return if (intersection.isEmpty()) null else Hexagon(intersection.first(), radius)
}

/**
 * Очень сложная (20 баллов)
 *
 * Дано множество точек (гексов). Найти правильный шестиугольник минимального радиуса,
 * содержащий все эти точки (безразлично, внутри или на границе).
 * Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит один гекс, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 *
 * Пример: 13, 32, 45, 18 -- шестиугольник радиусом 3 (с центром, например, в 15)
 */

// Найти максимальное расстояние между точками и две самые удаленные точки
fun maxDistance(points: List<HexPoint>): Pair<Int, Pair<HexPoint, HexPoint>> {
    // Мы обрабатываем 0 и 1 точки ранее
    var max = Pair(0, Pair(points[0], points[1]))
    for (i in points.indices)
        for (j in i + 1 until points.size) {
            val distance = points[i].distance(points[j])
            if (distance > max.first) max = Pair(distance, Pair(points[i], points[j]))
        }
    return max
}

fun minContainingHexagon(vararg points: HexPoint): Hexagon {
    return when (points.size) {
        0 -> throw IllegalArgumentException()
        1 -> Hexagon(points.first(), 0)
        else -> {
            val maxDistance = maxDistance(points.toList())
            val radius = (maxDistance.first + 1) / 2
            // Список точек, которые получены пересечением 2 самых удаленных центров
            val list = Hexagon(maxDistance.second.first, radius).radiusBoundary()
                .intersect(Hexagon(maxDistance.second.second, radius).radiusBoundary())
            for (i in list) {
                val hexagon = Hexagon(i, radius)
                var flag = true
                for (j in points) {
                    if (!hexagon.contains(j)) {
                        flag = false
                        break
                    }
                }
                if (flag) return hexagon
            }
            throw IllegalArgumentException("How did you get here?")
        }
    }
}