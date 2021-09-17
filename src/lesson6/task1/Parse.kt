@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import lesson4.task1.numToRim
import lesson4.task1.roman
import lesson5.task1.propagateHandshakes
import java.text.DateFormatSymbols

// Урок 6: разбор строк, исключения
// Максимальное количество баллов = 13
// Рекомендуемое количество баллов = 11
// Вместе с предыдущими уроками (пять лучших, 2-6) = 40/54

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun monthToNum(str: String): Int = when (str) {
    "января" -> 1
    "февраля" -> 2
    "марта" -> 3
    "апреля" -> 4
    "мая" -> 5
    "июня" -> 6
    "июля" -> 7
    "августа" -> 8
    "сентября" -> 9
    "октября" -> 10
    "ноября" -> 11
    "декабря" -> 12
    else -> -1
}

fun dateStrToDigit(str: String): String {
    val a = str.split(" ")
    if (a.size != 3) return ""
    val month = monthToNum(a[1])
    if (month == -1) return ""
    try {
        val year = a[2].toInt()
        if (year < 0) return ""
        val day = a[0].toInt()
        if (day < 1 || day > daysInMonth(month, year)) return ""
        return String.format("%02d.%02d.%d", day, month, year)
    } catch (e: NumberFormatException) {
        return ""
    }
}

/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun numToMonth(num: Int): String = when (num) {
    1 -> "января"
    2 -> "февраля"
    3 -> "марта"
    4 -> "апреля"
    5 -> "мая"
    6 -> "июня"
    7 -> "июля"
    8 -> "августа"
    9 -> "сентября"
    10 -> "октября"
    11 -> "ноября"
    12 -> "декабря"
    else -> ""
}

fun dateDigitToStr(digital: String): String {
    val a = digital.split(".")
    if (a.size != 3) return ""
    try {
        val month = numToMonth(a[1].toInt())
        if (month == "") return ""
        val year = a[2].toInt()
        if (year < 0) return ""
        val day = a[0].toInt()
        if (day < 1 || day > daysInMonth(a[1].toInt(), year)) return ""
        return String.format("%d %s %d", day, month, year)
    } catch (e: NumberFormatException) {
        return ""
    }
}

/**
 * Средняя (4 балла)
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun toNormalPhone(phone: String): String = phone.fold("") { previousResult, element ->
    if (element !in "- ") previousResult + element else previousResult
}

fun flattenPhoneNumber(phone: String): String {
    val normalizedPhone = toNormalPhone(phone)
    if (!normalizedPhone.contains("^\\+?\\d*(\\(\\d+\\)\\d*)?\$".toRegex())) return ""
    return "\\+*\\d+".toRegex().findAll(phone).map { it.value }.toList().joinToString(separator = "", postfix = "")
}

/**
 * Средняя (5 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (!"$jumps ".contains("^((\\d+|%|-) )+\$".toRegex())) return -1
    val jumpInt = "\\d+".toRegex().findAll(jumps).map { it.value }.toList()
    return jumpInt.maxOrNull()?.toInt() ?: -1
}

/**
 * Сложная (6 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (!"$jumps ".contains("^(\\d+ %*[+\\-%] )+\$".toRegex())) return -1
    val jumpInt = "(\\d+) %*\\+".toRegex().findAll(jumps).map { it.groupValues[1] }.toList()
    return jumpInt.maxOrNull()?.toInt() ?: -1
}

/**
 * Сложная (6 баллов)
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun calculate(list: List<String>): Int {
    var sum = list[0].toInt()
    for (i in 1 until list.size step 2) {
        when (list[i]) {
            "+" -> sum += list[i + 1].toInt()
            "-" -> sum -= list[i + 1].toInt()
            else -> throw IllegalArgumentException("Input line format violation")
        }
    }
    return sum
}

fun plusMinus(expression: String): Int {
    if (!expression.contains("^(\\d+ [+-] )*\\d+\$".toRegex()))
        throw IllegalArgumentException("Input line format violation")
    val example = "([^ ]+) ".toRegex().findAll("$expression ").map { it.groupValues[1] }.toList()
    return calculate(example)
}

/**
 * Сложная (6 баллов)
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int =
    "([^ ]+) \\1( |\$)".toRegex().find(str.lowercase())?.range?.first ?: -1

/**
 * Сложная (6 баллов)
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше нуля либо равны нулю.
 */
fun mostExpensive(description: String): String {
    if (!description.contains("^([^ ]+ (\\d+(\\.\\d+)?)(; |\$))+\$".toRegex())) return ""
    val descriptionList = description.split("; ")
    var max = Pair("", -1.0)
    for (i in descriptionList) {
        val splitDescription = i.split(" ")
        if (max.second < splitDescription[1].toDouble()) {
            max = Pair(splitDescription[0], splitDescription[1].toDouble())
        }
    }
    return max.first
}

/**
 * Сложная (6 баллов)
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun symbolsToInt(symbol: Char): Int = when (symbol) {
    'I' -> 1
    'V' -> 5
    'X' -> 10
    'L' -> 50
    'C' -> 100
    'D' -> 500
    'M' -> 1000
    else -> -1
}

// Алогритм позаимствован и адаптирован из https://www.cyberforum.ru/pascal/thread66998.html
// Сам я до этого не дошел
fun fromRoman(roman: String): Int {
    if (roman == "") return -1
    var symbolInIntFirst = 0
    var sum = 0
    for (i in roman) {
        val symbolInIntSecond = symbolInIntFirst.toInt()
        symbolInIntFirst = when (val symbolInInt = symbolsToInt(i)) {
            -1 -> return -1
            else -> symbolInInt
        }
        sum += if (symbolInIntFirst > symbolInIntSecond) -2 * symbolInIntSecond + symbolInIntFirst else symbolInIntFirst
    }
    if (roman != roman(sum)) return -1 // Быстрее, чем другие проверки, которые я придумал
    return sum
}

/**
 * Очень сложная (7 баллов)
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun commandsIsCorrect(commands: String) {
    val command = "+-<> "
    var counter = 0
    for (i in commands) {
        when (i) {
            '[' -> counter++
            ']' -> {
                counter--
                if (counter < 0) throw IllegalArgumentException("Incorrect Input")
            }
            !in command -> throw IllegalArgumentException("Incorrect Input")
        }
    }
    if (counter != 0) throw IllegalArgumentException("Incorrect Input")
}

fun nextBracket(commands: String): Int {
    var counterBracket = 0
    var newCellNumber = 0
    for (i in commands) {
        if (counterBracket == 0 && i == ']')
            return newCellNumber + 1
        when (i) {
            '[' -> counterBracket++
            ']' -> counterBracket--
        }
        newCellNumber++
    }
    // В теории до сюда никогда не дойдет, добавил т.к. компилятор хотел return
    throw IllegalArgumentException("Incorrect Input")
}

fun nextBracketRevers(commands: String): Int {
    var counterBracket = 0
    var newCellNumber = 0
    for (i in commands.reversed()) {
        if (counterBracket == 0 && i == '[')
            return newCellNumber + 2
        when (i) {
            '[' -> counterBracket++
            ']' -> counterBracket--
        }
        newCellNumber++
    }
    // В теории до сюда никогда не дойдет, добавил т.к. компилятор хотел return
    throw IllegalArgumentException("Incorrect Input")
}

fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    commandsIsCorrect(commands)
    val cellsList: MutableList<Int> = List(size = cells) { 0 }.toMutableList()
    var numberOfCommand = 0
    var numberOfCell: Int = cells / 2
    for (i in 1..limit) {
        when (commands[numberOfCommand]) {
            '+' -> cellsList[numberOfCell]++
            '-' -> cellsList[numberOfCell]--
            '>' -> numberOfCell++
            '<' -> numberOfCell--
            '[' -> if (cellsList[numberOfCell] == 0)
                numberOfCommand += nextBracket(commands.substring(numberOfCommand + 1))
            ']' -> if (cellsList[numberOfCell] != 0)
                numberOfCommand -= nextBracketRevers(commands.substring(0, numberOfCommand - 1))
        }
        numberOfCommand++
        if (numberOfCell > cells || numberOfCell < 0) throw IllegalStateException("The maximum value has been reached")
        if (numberOfCommand >= commands.length) break
    }
    return cellsList
}
