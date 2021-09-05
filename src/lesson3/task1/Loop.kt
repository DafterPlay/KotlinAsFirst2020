@file:Suppress("UNUSED_PARAMETER")

package lesson3.task1

import kotlin.math.*

// Урок 3: циклы
// Максимальное количество баллов = 9
// Рекомендуемое количество баллов = 7
// Вместе с предыдущими уроками = 16/21

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result *= i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false
    for (m in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n / 2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
    when {
        n == m -> 1
        n < 10 -> 0
        else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
    }

/**
 * Простая (2 балла)
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun digitNumber(n: Int): Int {
    return when {
        n < 10 -> 1
        else -> digitNumber(n / 10) + 1
    }
}

/**
 * Простая (2 балла)
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
 */
fun fib(n: Int): Int {
    if (n <= 2) return 1
    var num1 = 1
    var num2 = 1
    for (i in 3..n) {
        val num = num1
        num1 += num2
        num2 = num
    }
    return num1
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {
    if (n % 2 == 0) return 2
    for (i in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % i == 0) return i
    }
    return n
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int): Int {
    if (n % 2 == 0) return n / 2
    for (i in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % i == 0) return n / i
    }
    return 1
}

/**
 * Простая (2 балла)
 *
 * Гипотеза Коллатца. Рекуррентная последовательность чисел задана следующим образом:
 *
 *   ЕСЛИ (X четное)
 *     Xслед = X /2
 *   ИНАЧЕ
 *     Xслед = 3 * X + 1
 *
 * например
 *   15 46 23 70 35 106 53 160 80 40 20 10 5 16 8 4 2 1 4 2 1 4 2 1 ...
 * Данная последовательность рано или поздно встречает X == 1.
 * Написать функцию, которая находит, сколько шагов требуется для
 * этого для какого-либо начального X > 0.
 */
fun collatzSteps(x: Int): Int {
    return when {
        x == 1 -> 0
        x % 2 == 0 -> 1 + collatzSteps(x / 2)
        else -> 1 + collatzSteps(x * 3 + 1)
    }
}

/**
 * Средняя (3 балла)
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */

fun lcm(m: Int, n: Int): Int {
    val max = max(n, m)
    val min = min(n, m)
    for (i in min downTo 1) {
        if (min % i == 0 && max % i == 0) return m * n / i
    }
    return m * n
}

/**
 * Средняя (3 балла)
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */
fun isCoPrime(m: Int, n: Int): Boolean {
    if (m % 2 == 0 && n % 2 == 0) return false
    val min = min(m, n)
    val max = max(m, n)
    for (i in 3..sqrt(min.toDouble()).toInt() step 2) {
        if (min % i == 0) {
            if (max % i == 0) return false
            else if (max % (min / i) == 0) return false
        }
    }
    return max % min != 0
}

/**
 * Средняя (3 балла)
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
// Количество цифр в числе
fun countNumbers(n: Int): Int {
    var counter = 0
    var newN = n
    do {
        counter++
        newN /= 10
    } while (newN > 0)
    return counter
}

// Для степени десятки т.к. 10.pow() работать не хочет :(
fun pow(n: Int): Int {
    return when (n) {
        0 -> 1
        else -> pow(n - 1) * 10
    }
}

fun revert(n: Int): Int {
    return when {
        n < 10 -> n
        else -> revert(n / 10) + n % 10 * pow(countNumbers(n) - 1)
    }
}

/**
 * Средняя (3 балла)
 *
 * Проверить, является ли заданное число n палиндромом:
 * первая цифра равна последней, вторая -- предпоследней и так далее.
 * 15751 -- палиндром, 3653 -- нет.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun isPalindrome(n: Int): Boolean {
    val countN = countNumbers(n)
    for (i in 0 until countN / 2) {
        if ((n / pow(i)) % 10 != n / (pow(countN - 1 - i)) % 10) return false
    }
    return true
}

/**
 * Средняя (3 балла)
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun hasDifferentDigits(n: Int): Boolean {
    val num = n % 10
    var copyN = n
    do {
        if (copyN % 10 != num) return true
        copyN /= 10
    } while (copyN > 0)
    return false
}

/**
 * Средняя (4 балла)
 *
 * Для заданного x рассчитать с заданной точностью eps
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю.
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.sin и другие стандартные реализации функции синуса в этой задаче запрещается.
 */
fun sin(x: Double, eps: Double): Double {
    val copyX = x % (2 * PI)
    var steps = 3
    var morp = -1 //Минус или плюс
    var sin = copyX
    do {
        val nextElement = copyX.pow(steps) / factorial(steps) * morp
        sin += nextElement
        steps += 2
        morp *= -1
    } while (eps < abs(nextElement))
    return sin
}

/**
 * Средняя (4 балла)
 *
 * Для заданного x рассчитать с заданной точностью eps
 * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.cos и другие стандартные реализации функции косинуса в этой задаче запрещается.
 */
fun cos(x: Double, eps: Double): Double {
    val copyX = x % (2 * PI)
    var steps = 2
    var morp = -1 //Минус или плюс
    var cos = 1.0
    do {
        val nextElement = copyX.pow(steps) / factorial(steps) * morp
        cos += nextElement
        steps += 2
        morp *= -1
    } while (eps < abs(nextElement))
    return cos
}

/**
 * Сложная (4 балла)
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 149162536496481100121144...
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun squareSequenceDigit(n: Int): Int {
    var len = 0
    var sqr = 1
    do {
        len += countNumbers(sqr.pow2())
        if (len < n) sqr++
    } while (len < n)
    val needNum = len - n
    return (sqr.pow2() / pow(needNum)) % 10
}

private fun Int.pow2(): Int = this * this

/**
 * Сложная (5 баллов)
 *
 * Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
 * 1123581321345589144...
 * Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun fibSequenceDigit(n: Int): Int {
    if (n <= 2) return 1
    var len = 2
    var num1 = 1
    var num2 = 1
    do {
        val num = num1
        num1 += num2
        num2 = num
        len += countNumbers(num1)
    } while (len < n)
    val needNum = len - n
    return (num1 / pow(needNum)) % 10
}
