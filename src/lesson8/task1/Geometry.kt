@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import kotlin.math.*

// Урок 8: простые классы
// Максимальное количество баллов = 40 (без очень трудных задач = 11)

/**
 * Точка на плоскости
 */
data class Point(var x: Double, var y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
    operator fun div(num: Int): Point {
        x /= num
        y /= num
        return this
    }

    fun subtract(p: Point): Point = Point(x - p.x, y - p.y)
    fun cross(p: Point): Double = x * p.y - y * p.x
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая (2 балла)
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double {
        val distance = center.distance(other.center) - radius - other.radius
        return when (distance > 0) {
            false -> 0.0
            else -> distance
        }
    }

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = center.distance(p) <= radius
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    val distance = begin.distance(end)

    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.toSet().size < 2) throw IllegalArgumentException()
    var segmentWithMaxDistance = Segment(points[0], points[1])
    for (i in points.indices)
        for (j in (i + 1) until points.size)
            if (segmentWithMaxDistance.distance < points[i].distance(points[j]))
                segmentWithMaxDistance = Segment(points[i], points[j])
    return segmentWithMaxDistance
}

/**
 * Простая (2 балла)
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle = Circle(
    Point((diameter.begin.x + diameter.end.x) / 2, (diameter.begin.y + diameter.end.y) / 2),
    diameter.distance / 2
)

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя (3 балла)
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val x = (other.b * cos(angle) - b * cos(other.angle)) / sin(angle - other.angle)
        val y = if (cos(angle) != cos(PI / 2)) {
            (x * sin(angle) + b) / cos(angle)
        } else {
            (x * sin(other.angle) + other.b) / cos(other.angle)
        }
        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

fun angleX(a: Point, b: Point): Double = when {
    (b.y - a.y) / (b.x - a.x) < 0 -> (PI - acos(((abs(a.x - b.x)) / a.distance(b)) % PI)) % PI
    else -> acos(((abs(a.x - b.x)) / a.distance(b)) % PI) % PI
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = Line(s.begin, angleX(s.begin, s.end))

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = Line(a, angleX(a, b))

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line =
    Line(
        Point(((a.x + b.x)) / 2, ((a.y + b.y)) / 2),
        (angleX(a, b) + if (angleX(a, b) > PI / 2) -PI / 2 else PI / 2) % PI
    )

/**
 * Средняя (3 балла)
 *
 * Задан список из n окружностей на плоскости.
 * Найти пару наименее удалённых из них; расстояние между окружностями
 * рассчитывать так, как указано в Circle.distance.
 *
 * При наличии нескольких наименее удалённых пар,
 * вернуть первую из них по порядку в списке circles.
 *
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw IllegalArgumentException()
    var answer = Pair(circles[0], circles[1])
    for (i in circles.indices)
        for (j in (i + 1) until circles.size)
            if (answer.first.distance(answer.second) > circles[i].distance(circles[j]))
                answer = Pair(circles[i], circles[j])
    return answer
}

/**
 * Сложная (5 баллов)
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle = Circle(
    bisectorByPoints(a, b).crossPoint(bisectorByPoints(c, b)),
    a.distance(b) * b.distance(c) * c.distance(a) / (4 * Triangle(a, b, c).area())
)


/**
 * Очень сложная (10 баллов)
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
// Использовал код с этого сайта https://www.nayuki.io/page/smallest-enclosing-circle
// Взял версию java и первел на kotlin
fun makeCircleOnePoint(points: List<Point>, p: Point): Circle {
    var c = Circle(p, 0.0)
    for (i in points.indices) {
        val q = points[i]
        if (!c.contains(q)) {
            c = if (c.radius == 0.0)
                makeDiameter(p, q)
            else
                makeCircleTwoPoints(points.subList(0, i + 1), p, q)
        }
    }
    return c
}

fun makeCircleTwoPoints(points: List<Point>, p: Point, q: Point): Circle {
    val circ = makeDiameter(p, q)
    var left: Circle? = null
    var right: Circle? = null
    val pq = q.subtract(p)
    for (r in points) {
        if (circ.contains(r)) continue
        val cross = pq.cross(r.subtract(p))
        val c = makeCircumcircle(p, q, r)
        if (c == null) continue
        else if (cross > 0 && (left == null || pq.cross(c.center.subtract(p)) > pq.cross(left.center.subtract(p))))
            left = c
        else if (cross < 0 && (right == null || pq.cross(c.center.subtract(p)) < pq.cross(right.center.subtract(p))))
            right = c
    }
    return when {
        left == null && right == null -> circ
        left == null -> right!!
        right == null -> left
        else -> if (left.radius <= right.radius) left
        else right
    }
}

fun makeDiameter(a: Point, b: Point): Circle {
    val c = Point((a.x + b.x) / 2, (a.y + b.y) / 2)
    return Circle(c, max(c.distance(a), c.distance(b)))
}

fun makeCircumcircle(a: Point, b: Point, c: Point): Circle? {
    val ox = (min(min(a.x, b.x), c.x) + max(max(a.x, b.x), c.x)) / 2
    val oy = (min(min(a.y, b.y), c.y) + max(max(a.y, b.y), c.y)) / 2
    val ax = a.x - ox
    val ay = a.y - oy
    val bx = b.x - ox
    val by = b.y - oy
    val cx = c.x - ox
    val cy = c.y - oy
    val d = (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)) * 2
    if (d == 0.0) return null
    val x = ((ax * ax + ay * ay) * (by - cy) + (bx * bx + by * by) * (cy - ay) + (cx * cx + cy * cy) * (ay - by)) / d
    val y = ((ax * ax + ay * ay) * (cx - bx) + (bx * bx + by * by) * (ax - cx) + (cx * cx + cy * cy) * (bx - ax)) / d
    val p = Point(ox + x, oy + y)
    val r = max(max(p.distance(a), p.distance(b)), p.distance(c))
    return Circle(p, r)
}

fun minContainingCircle(vararg points: Point): Circle {
    val shuffled = points.toList().shuffled()
    var c: Circle? = null
    for (i in shuffled.indices) {
        val p = shuffled[i]
        if (c == null || !c.contains(p))
            c = makeCircleOnePoint(shuffled.subList(0, i + 1), p)
    }
    return c!!
}