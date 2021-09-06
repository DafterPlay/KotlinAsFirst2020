@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson4.task1

import lesson1.task1.discriminant
import lesson3.task1.isPrime
import kotlin.math.pow
import kotlin.math.sqrt

// Урок 4: списки
// Максимальное количество баллов = 12
// Рекомендуемое количество баллов = 8
// Вместе с предыдущими уроками = 24/33

/**
 * Пример
 *
 * Найти все корни уравнения x^2 = y
 */
fun sqRoots(y: Double) =
    when {
        y < 0 -> listOf()
        y == 0.0 -> listOf(0.0)
        else -> {
            val root = sqrt(y)
            // Результат!
            listOf(-root, root)
        }
    }

/**
 * Пример
 *
 * Найти все корни биквадратного уравнения ax^4 + bx^2 + c = 0.
 * Вернуть список корней (пустой, если корней нет)
 */
fun biRoots(a: Double, b: Double, c: Double): List<Double> {
    if (a == 0.0) {
        return if (b == 0.0) listOf()
        else sqRoots(-c / b)
    }
    val d = discriminant(a, b, c)
    if (d < 0.0) return listOf()
    if (d == 0.0) return sqRoots(-b / (2 * a))
    val y1 = (-b + sqrt(d)) / (2 * a)
    val y2 = (-b - sqrt(d)) / (2 * a)
    return sqRoots(y1) + sqRoots(y2)
}

/**
 * Пример
 *
 * Выделить в список отрицательные элементы из заданного списка
 */
fun negativeList(list: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (element in list) {
        if (element < 0) {
            result.add(element)
        }
    }
    return result
}

/**
 * Пример
 *
 * Изменить знак для всех положительных элементов списка
 */
fun invertPositives(list: MutableList<Int>) {
    for (i in 0 until list.size) {
        val element = list[i]
        if (element > 0) {
            list[i] = -element
        }
    }
}

/**
 * Пример
 *
 * Из имеющегося списка целых чисел, сформировать список их квадратов
 */
fun squares(list: List<Int>) = list.map { it * it }

/**
 * Пример
 *
 * Из имеющихся целых чисел, заданного через vararg-параметр, сформировать массив их квадратов
 */
fun squares(vararg array: Int) = squares(array.toList()).toTypedArray()

/**
 * Пример
 *
 * По заданной строке str определить, является ли она палиндромом.
 * В палиндроме первый символ должен быть равен последнему, второй предпоследнему и т.д.
 * Одни и те же буквы в разном регистре следует считать равными с точки зрения данной задачи.
 * Пробелы не следует принимать во внимание при сравнении символов, например, строка
 * "А роза упала на лапу Азора" является палиндромом.
 */
fun isPalindrome(str: String): Boolean {
    val lowerCase = str.lowercase().filter { it != ' ' }
    for (i in 0..lowerCase.length / 2) {
        if (lowerCase[i] != lowerCase[lowerCase.length - i - 1]) return false
    }
    return true
}

/**
 * Пример
 *
 * По имеющемуся списку целых чисел, например [3, 6, 5, 4, 9], построить строку с примером их суммирования:
 * 3 + 6 + 5 + 4 + 9 = 27 в данном случае.
 */
fun buildSumExample(list: List<Int>) = list.joinToString(separator = " + ", postfix = " = ${list.sum()}")

/**
 * Простая (2 балла)
 *
 * Найти модуль заданного вектора, представленного в виде списка v,
 * по формуле abs = sqrt(a1^2 + a2^2 + ... + aN^2).
 * Модуль пустого вектора считать равным 0.0.
 */
fun abs(v: List<Double>): Double {
    return when {
        v.isEmpty() -> 0.0
        else -> {
            var sqrt = 0.0
            for (i in v) {
                sqrt += i.pow(2)
            }
            sqrt(sqrt)
        }
    }
}

/**
 * Простая (2 балла)
 *
 * Рассчитать среднее арифметическое элементов списка list. Вернуть 0.0, если список пуст
 */
fun mean(list: List<Double>): Double {
    return when (list.isEmpty()) {
        true -> 0.0
        else -> (list.fold(0.0) { previousResult, element -> previousResult + element }) / list.size
    }
}

/**
 * Средняя (3 балла)
 *
 * Центрировать заданный список list, уменьшив каждый элемент на среднее арифметическое всех элементов.
 * Если список пуст, не делать ничего. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun center(list: MutableList<Double>): MutableList<Double> {
    if (list.isEmpty()) return list // Откидываем пустой список
    val mean = mean(list)
    for (index in 0 until list.size) {
        list[index] -= mean
    }
    return list
}

/**
 * Средняя (3 балла)
 *
 * Найти скалярное произведение двух векторов равной размерности,
 * представленные в виде списков a и b. Скалярное произведение считать по формуле:
 * C = a1b1 + a2b2 + ... + aNbN. Произведение пустых векторов считать равным 0.
 */
fun times(a: List<Int>, b: List<Int>): Int {
    var C = 0
    for (i in 0 until a.size) {
        C += a[i] * b[i]
    }
    return C
}

/**
 * Средняя (3 балла)
 *
 * Рассчитать значение многочлена при заданном x:
 * p(x) = p0 + p1*x + p2*x^2 + p3*x^3 + ... + pN*x^N.
 * Коэффициенты многочлена заданы списком p: (p0, p1, p2, p3, ..., pN).
 * Значение пустого многочлена равно 0 при любом x.
 */
fun polynom(p: List<Int>, x: Int): Int {
    var p_x = 0
    for (i in 0 until p.size) {
        p_x += p[i] * x.pow(i)
    }
    return p_x
}

private fun Int.pow(i: Int): Int {
    var counter = 1
    for (j in 1..i) {
        counter *= this
    }
    return counter
}


/**
 * Средняя (3 балла)
 *
 * В заданном списке list каждый элемент, кроме первого, заменить
 * суммой данного элемента и всех предыдущих.
 * Например: 1, 2, 3, 4 -> 1, 3, 6, 10.
 * Пустой список не следует изменять. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun accumulate(list: MutableList<Int>): MutableList<Int> {
    var sum = 0
    for (i in 0 until list.size) {
        sum += list[i]
        list[i] += sum - list[i]
    }
    return list
}

/**
 * Средняя (3 балла)
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде списка множителей, например 75 -> (3, 5, 5).
 * Множители в списке должны располагаться по возрастанию.
 */
fun factorize(n: Int): List<Int> {
    if (isPrime(n)) return listOf(n)
    var copyN = n
    var testNum = 1
    var nextNum = true
    val result = mutableListOf<Int>()
    while (copyN > 1) {
        if (nextNum) {
            testNum++;
            if (!isPrime(testNum)) continue
            if (copyN % testNum != 0) continue
        }
        result.add(testNum)
        copyN /= testNum
        nextNum = copyN % testNum != 0
        if (isPrime(copyN)) return result + copyN
    }
    return result
}

/**
 * Сложная (4 балла)
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде строки, например 75 -> 3*5*5
 * Множители в результирующей строке должны располагаться по возрастанию.
 */
fun factorizeToString(n: Int): String = factorize(n).joinToString(separator = "*")

/**
 * Средняя (3 балла)
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием base > 1.
 * Результат перевода вернуть в виде списка цифр в base-ичной системе от старшей к младшей,
 * например: n = 100, base = 4 -> (1, 2, 1, 0) или n = 250, base = 14 -> (1, 3, 12)
 */
fun convert(n: Int, base: Int): List<Int> {
    var copyN = n
    val v = mutableListOf<Int>()
    while (copyN > 0) {
        v.add(copyN % base)
        copyN /= base
    }
    return v.reversed()
}

/**
 * Сложная (4 балла)
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием 1 < base < 37.
 * Результат перевода вернуть в виде строки, цифры более 9 представлять латинскими
 * строчными буквами: 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: n = 100, base = 4 -> 1210, n = 250, base = 14 -> 13c
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, n.toString(base) и подобные), запрещается.
 */
fun convertToString(n: Int, base: Int): String {
    val v = convert(n, base)
    if (base < 10) return v.joinToString(separator = "")
    return v.fold("") { previousResult, element ->
        when {
            element < 10 -> previousResult + element
            else -> previousResult + ('a'.toInt() + element - 10).toChar()
        }
    }
}

/**
 * Средняя (3 балла)
 *
 * Перевести число, представленное списком цифр digits от старшей к младшей,
 * из системы счисления с основанием base в десятичную.
 * Например: digits = (1, 3, 12), base = 14 -> 250
 */
fun decimal(digits: List<Int>, base: Int): Int {
    var num = 0
    val size = digits.size
    for (i in 0 until size) {
        num += digits[i] * base.pow(digits.size - i - 1)
    }
    return num
}

/**
 * Сложная (4 балла)
 *
 * Перевести число, представленное цифровой строкой str,
 * из системы счисления с основанием base в десятичную.
 * Цифры более 9 представляются латинскими строчными буквами:
 * 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: str = "13c", base = 14 -> 250
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, str.toInt(base)), запрещается.
 */
fun decimalFromString(str: String, base: Int): Int {
    val v = mutableListOf<Int>()
    for (i in str) {
        when {
            i < 'a' -> v.add(i.toInt() - '0'.toInt())
            else -> v.add(i.toInt() - 'a'.toInt() + 10)
        }
    }
    return decimal(v, base)
}

/**
 * Сложная (5 баллов)
 *
 * Перевести натуральное число n > 0 в римскую систему.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: 23 = XXIII, 44 = XLIV, 100 = C
 */
fun numToRim(n: Int, str: String): String = when {
    n < 4 -> str[0].toString().pow(n)
    n == 4 -> str[0].toString() + str[1].toString()
    n < 9 -> str[1].toString() + str[0].toString().pow(n - 5)
    else -> str[0].toString().pow(10 - n) + str[2].toString()
}

private fun String.pow(i: Int): String = when (i) {
    0 -> ""
    else -> this + this.pow(i - 1)
}

fun roman(n: Int): String {
    val list = listOf<String>("IVX", "XLC", "CDM") //1-10, 10-100, 100-1000
    return "M".pow(n / 1000) + numToRim((n / 100) % 10, list[2]) +
            numToRim((n / 10) % 10, list[1]) + numToRim(n % 10, list[0])
}

/**
 * Очень сложная (7 баллов)
 *
 * Записать заданное натуральное число 1..999999 прописью по-русски.
 * Например, 375 = "триста семьдесят пять",
 * 23964 = "двадцать три тысячи девятьсот шестьдесят четыре"
 */
fun units(n: Int): String = when (n) { // 1-9
    1 -> "один"
    2 -> "два"
    3 -> "три"
    4 -> "четыре"
    5 -> "пять"
    6 -> "шесть"
    7 -> "семь"
    8 -> "восемь"
    9 -> "девять"
    else -> ""
}

fun dozens(n: Int): String = when (n) { // 20-90
    in 2..3 -> units(n) + "дцать"
    4 -> "сорок"
    in 5..8 -> units(n) + "десят"
    9 -> "девяносто"
    else -> ""
}

fun tens(n: Int): String = when (n) { // 11-19
    1 -> "одиннадцать"
    2 -> "двенадцать"
    3 -> "тринадцать"
    4 -> "четырнадцать"
    in 5..9 -> units(n).substring(0, units(n).length - 1) + "надцать"
    else -> "десять"
}

fun hundreds(n: Int): String = when (n) { // 100-900
    1 -> "сто"
    2 -> "двести"
    3 -> "триста"
    4 -> "четыреста"
    in 5..9 -> units(n) + "сот"
    else -> ""
}

fun unitsThousands(n: Int): String = when (n) { // 1-9 для тысяч
    1 -> "одна"
    2 -> "две"
    in 3..9 -> units(n)
    else -> ""
}

fun russian(n: Int): String {
    // Часть от 1 до 999
    val str1 = hundreds((n / 100) % 10) + " " + when ((n / 10) % 10) {
        1 -> tens(n % 10)
        else -> dozens((n / 10) % 10) + " " + units(n % 10)
    }
    // Часть вторая, для тысяч + сама преписка "тысяча"
    val str2 = hundreds((n / 100000) % 10) + " " + when ((n / 10000) % 10) {
        1 -> tens((n / 1000) % 10) + " тысяч "
        else -> dozens((n / 10000) % 10) + " " + unitsThousands((n / 1000) % 10) + " " + when {
            (n / 1000) % 10 == 1 -> "тысяча "
            (n / 1000) % 10 in 2..4 -> "тысячи "
            (n / 1000) % 10 in 5..9 || n / 1000 > 0 -> "тысяч"
            else -> ""
        }
    }
    return (str2 + str1).trim().replace(" +".toRegex(), " ")
}
