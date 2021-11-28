@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import ru.spbstu.wheels.appendLine
import java.io.File
import java.lang.Integer.max
import java.util.*

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103


/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    File(outputName).bufferedWriter().use { writer ->
        File(inputName).forEachLine { i ->
            if (i.isEmpty()) writer.newLine()
            else if (i[0] != '_') {
                writer.appendLine(i)
            }
        }
    }
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countOf(line: String, findLine: String): Int {
    if (line.isEmpty()) return 0
    var counter = 0
    var nextIndex = line.indexOf(findLine, 0)
    while (nextIndex != -1) {
        val index = nextIndex + 1
        counter++
        nextIndex = line.indexOf(findLine, index)
    }
    return counter
}

fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    // Создана с целью не переводить в нижний регистр по сто раз, но понимать, какое слово было изначально
    val substringsLowerMap = mutableMapOf<String, String>()
    for (i in substrings) substringsLowerMap[i.lowercase(Locale.getDefault())] = i
    File(inputName).forEachLine { line ->
        val lineLower = line.lowercase(Locale.getDefault())
        for (i in substringsLowerMap.keys) {
            map[substringsLowerMap[i]!!] =
                map[substringsLowerMap[i]!!]?.plus(countOf(lineLower, i)) ?: countOf(lineLower, i)
        }
    }
    return map
}

/*
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    val textList = File(inputName).readLines().map { it.lowercase(Locale.getDefault()) }
    for (i in substrings) {
        var count = 0
        val substringLower = i.lowercase(Locale.getDefault())
        for (j in textList) {
            count += countOf(j, substringLower)
        }
        map[i] = count
    }
    return map
}
*/

/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val regex = "[ЧчЩщЖжШш][ЯяЮюЫы]".toRegex()
    File(outputName).bufferedWriter().use { writer ->
        File(inputName).forEachLine { i ->
            writer.appendLine(i.replace(regex, transform = {
                it.value[0] + when (it.value[1]) {
                    'ы' -> "и"
                    'Ы' -> "И"
                    'я' -> "а"
                    'Я' -> "А"
                    'ю' -> "у"
                    'Ю' -> "У"
                    else -> it.value[1].toString()
                }
            }))
        }
    }
}

/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val textList = File(inputName).readLines().map { it.trim() }
    val maxLen = textList.maxOfOrNull { it.length }?.toInt() ?: 0
    File(outputName).bufferedWriter().use { writer ->
        for (i in textList)
            writer.appendLine(i.padStart(i.length + (maxLen - i.length) / 2, ' '))
    }
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val textList = File(inputName).readLines().map { it.trim() }
    val maxLen = textList.maxOfOrNull { it.length }?.toInt() ?: 0
    val wordsInText = textList.map { it.replace("""\s+""".toRegex(), " ").split(' ') }
    for (i in wordsInText.indices) {
        if (wordsInText[i].size == 1) {
            writer.appendLine(textList[i])
            continue
        }
        val needSpaces = maxLen - wordsInText[i].fold("") { line, add -> line + add }.length
        var remainingSpaces = needSpaces % (wordsInText[i].size - 1)
        val spacesBetween = needSpaces / (wordsInText[i].size - 1)
        writer.appendLine(
            wordsInText[i].fold("") { line, add ->
                remainingSpaces -= 1
                line + add + buildString {
                    for (a in (if (remainingSpaces >= 0) 0 else 1)..spacesBetween)
                        append(' ')
                }
            }.trim()
        )
    }
    writer.close()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
 * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val mapWords = mutableMapOf<String, Int>()
    val regex = """[^a-zа-я|ё]+""".toRegex()
    File(inputName).forEachLine { i ->
        val words = i.lowercase(Locale.getDefault()).split(regex)
        for (j in words.filter { it.isNotEmpty() }) {
            mapWords[j] = (mapWords[j] ?: 0) + 1
        }
    }
    return when {
        mapWords.size < 20 -> mapWords
        else -> {
            val maxValue = mapWords.map { newIt -> newIt.value }.sorted()
            mapWords.filter { it.value >= maxValue[maxValue.size - 20] }
        }
    }
}

/**
 * Средняя (14 баллов)
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */

fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val lowerDictionary = dictionary.map {
        Pair(
            it.key.lowercaseChar(),
            it.value.lowercase(Locale.getDefault())
        )
    }.toMap()
    File(outputName).bufferedWriter().use { writer ->
        File(inputName).forEachLine { i ->
            writer.appendLine(
                i.fold("") { last, it ->
                    last + when (it.lowercaseChar() in lowerDictionary) {
                        true -> when (!it.isLowerCase() && it.isLetter()) {
                            true -> lowerDictionary[it.lowercaseChar()]?.replaceFirstChar { it.uppercaseChar() }
                            else -> lowerDictionary[it.lowercaseChar()]
                        }
                        else -> it
                    }
                }
            )
        }
    }
}

/**
 * Средняя (12 баллов)
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun checkUniqueness(str: String): Boolean {
    val set = mutableSetOf<Char>()
    for (i in str) {
        if (i in set) return false
        set.add(i)
    }
    return true
}

fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val uniquenessWords =
        File(inputName).readLines().filter { checkUniqueness(it.lowercase(Locale.getDefault())) }
    val maxSize = uniquenessWords.map { it.length }.maxOrNull()
    File(outputName).bufferedWriter()
        .use { writer -> writer.appendLine(uniquenessWords.filter { it.length == maxSize }.joinToString()) }
}

/**
 * Сложная (22 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun formateLine(i: String, counter: Triple<Int, Int, Int>): Pair<String, Triple<Int, Int, Int>> {
    var str = ""
    var countI = counter.first
    var countB = counter.second
    var countS = counter.third
    if (i.isBlank()) {
        str += "</p><p>"
        return Pair(str, Triple(countI, countB, countS))
    }
    val iteratorI = i.iterator()
    // Сделана т.к. итератором нельзя сделать шаг назад, а бывают ситуации, когда он нужен
    // А желания гонять по строке, используя индексы у меня нет
    var needNew: Char? = null
    while (iteratorI.hasNext() || needNew != null) {
        val charNow = needNew ?: iteratorI.nextChar()
        needNew = null
        str += when (charNow) {
            '*' -> when (iteratorI.hasNext()) {
                true -> when (val nextChar = iteratorI.nextChar()) {
                    '*' -> if (countB == 0) {
                        countB++; "<b>"
                    } else {
                        countB--; "</b>"
                    }
                    else -> {
                        needNew = nextChar
                        if (countI == 0) {
                            countI++; "<i>"
                        } else {
                            countI--; "</i>"
                        }
                    }
                }
                else -> if (countI == 0) {
                    countI++; "<i>"
                } else {
                    countI--; "</i>"
                }
            }
            '~' -> when (iteratorI.hasNext()) {
                true -> when (val nextChar = iteratorI.nextChar()) {
                    '~' -> if (countS == 0) {
                        countS++; "<s>"
                    } else {
                        countS--; "</s>"
                    }
                    else -> {
                        needNew = nextChar
                        charNow
                    }
                }
                else -> charNow
            }
            else -> charNow
        }
    }
    return Pair(str, Triple(countI, countB, countS))
}

fun markdownToHtmlSimple(inputName: String, outputName: String) {
    var str = "<html><body><p>"
    var file = File(inputName).readLines()
    while (file.isNotEmpty() && file[0].trim().isEmpty()) file = file.subList(1, file.size)
    while (file.isNotEmpty() && file[file.size - 1].trim().isEmpty()) file = file.subList(0, file.size - 1)
    var counter = Triple(0, 0, 0)
    for (i in file) {
        if (!(str.endsWith("</p><p>") && i.isBlank())) {
            val strAndCounter = formateLine(i, counter)
            str += strAndCounter.first
            counter = strAndCounter.second
        }
    }
    str += "</p></body></html>"
    val writer = File(outputName).bufferedWriter()
    writer.appendLine(str)
    writer.close()
}

/**
 * Сложная (23 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body><p>...</p></body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<p>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>Или колбаса</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>Фрукты
<ol>
<li>Бананы</li>
<li>Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</p>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun countSpacesInStart(str: String): Int {
    var counter = 0
    for (i in str) {
        if (i != ' ')
            return counter
        counter++
    }
    return counter
}

fun markdownToHtmlLists(inputName: String, outputName: String) {
    var str = "<html><body><p>"
    val lines =
        Pair(File(inputName).readLines() + "", File(inputName).readLines().map { countSpacesInStart(it) } + -1)
    var numberOfLine = 0
    fun toList(prefix: String) {
        str += "<$prefix>"
        val countSpaces = lines.second[numberOfLine]
        do {
            str += "<li>${lines.first[numberOfLine].trim().replace("""^(\*|\d*.) """.toRegex(), "")}"
            if (lines.second[numberOfLine] < lines.second[numberOfLine + 1]) {
                numberOfLine++
                toList(if (lines.first[numberOfLine].trim()[0] == '*') "ul" else "ol")
            }
            numberOfLine++
            str += "</li>"
        } while (countSpaces <= lines.second[numberOfLine])
        numberOfLine--
        str += "</$prefix>"
    }
    toList(if (lines.first[0].trim()[0] == '*') "ul" else "ol")
    str += "</p></body></html>"
    val writer = File(outputName).bufferedWriter()
    writer.appendLine(str)
    writer.close()
}

/**
 * Очень сложная (30 баллов)
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun countAsterisk(str: String): Int {
    var counter = 0
    for (i in str)
        if (i == '*')
            counter++
    return counter
}

fun markdownToHtml(inputName: String, outputName: String) {
    var str = "<html><body><p>"
    var file = File(inputName).readLines()
    while (file[0].isEmpty()) file = file.subList(1, file.size)
    while (file[file.size - 1].isEmpty()) file = file.subList(0, file.size - 1)
    val lines = Pair(file + "", file.map { countSpacesInStart(it) } + -1)
    var numberOfLine = 0
    var counter = Triple(0, 0, 0)
    while (numberOfLine < lines.first.size - 1) {
        val lineNow = lines.first[numberOfLine]
        if ((lineNow.startsWith("* ") && countAsterisk(lineNow) % 2 == 1) || lineNow.startsWith("1. ")) {
            fun toList(prefix: String) {
                str += "<$prefix>"
                val countSpaces = lines.second[numberOfLine]
                do {
                    if (!lines.first[numberOfLine].trim().startsWith("*") && prefix == "ul") break
                    if (!lines.first[numberOfLine].trim().contains("""^\d*\. """.toRegex()) && prefix == "ol") break
                    val strAndCounter = formateLine(
                        lines.first[numberOfLine].trim().replace("""^(\*|\d*\.) """.toRegex(), ""), counter
                    )
                    str += "<li>${strAndCounter.first}"
                    counter = strAndCounter.second
                    if (lines.second[numberOfLine] < lines.second[numberOfLine + 1]) {
                        numberOfLine++
                        toList(if (lines.first[numberOfLine].trim()[0] == '*') "ul" else "ol")
                    }
                    numberOfLine++
                    str += "</li>"
                } while (countSpaces <= lines.second[numberOfLine])
                numberOfLine--
                str += "</$prefix>"
            }
            toList(if (lines.first[numberOfLine].trim().startsWith('*')) "ul" else "ol")
        } else {
            if (!(str.endsWith("</p><p>") && lineNow.isBlank())) {
                val strAndCounter = formateLine(lineNow, counter)
                str += strAndCounter.first
                counter = strAndCounter.second
            }
        }
        numberOfLine++
    }
    str += "</p></body></html>"
    val writer = File(outputName).bufferedWriter()
    writer.appendLine(str)
    writer.close()
}

/**
 * Средняя (12 баллов)
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
// Создает строку из quantity сиволов symbol
// Сильно укорачивает код и упрощает чтение
fun buildString(symbol: Char, quantity: Int): String = buildString { for (i in 0 until quantity) append(symbol) }

fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val rhvLength = rhv.toString().length
    val lenString =
        (lhv * rhv).toString().length + 1
    val writer = File(outputName).bufferedWriter()
    // Создание первых 4 строк
    writer.appendLine(
        buildString(' ', lenString - lhv.toString().length) + lhv +
                "\n*" + buildString(' ', lenString - rhvLength - 1) + rhv +
                "\n" + buildString('-', lenString) +
                "\n" + buildString(' ', lenString - (rhv % 10 * lhv).toString().length)
                + rhv % 10 * lhv
    )
    // Создание строк, начинающихся с +
    var rhvCopy = rhv / 10
    for (i in 0..rhvLength - 2) {
        writer.appendLine(
            "+" +
                    buildString(' ', lenString - (rhvCopy % 10 * lhv).toString().length - 2 - i)
                    + rhvCopy % 10 * lhv
        )
        rhvCopy /= 10
    }
    // Добавление строки ответа
    writer.appendLine(buildString('-', lenString) + "\n " + rhv * lhv)
    writer.close()
}


/**
 * Сложная (25 баллов)
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */

// Функция для перевода инта в лист, до числа toNum
fun intToListFor(number: Int, toNum: Int): List<Int> {
    val result = mutableListOf<Int>()
    var copyNumber = number
    for (i in 0 until number.toString().length - toNum) {
        result.add(copyNumber % 10)
        copyNumber /= 10
    }
    return result.reversed()
}

private val Int.len: Int
    get() = this.toString().length

private fun Int.pow(i: Int): Int {
    var result = 1
    for (j in 0 until i) {
        result *= this
    }
    return result
}

fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    // Рассмотрим == 0, как частный случай
    if (lhv / rhv == 0) {
        writer.appendLine(
            (if (lhv.len <= ((lhv / rhv) * rhv).len) " " else "") + "$lhv | $rhv\n" +
                    buildString(' ', lhv.len - 2) + "-0   0\n" +
                    buildString('-', max(lhv.len, 2)) + "\n" + (if (lhv < 10) " " else "") + "$lhv"
        )
    } else {
        val lenLhv = lhv.len
        var lastNum = 0
        // На каком числе с конца остановились
        var counter = 0
        // Подсчет первого числа
        for (i in (lenLhv - 1) downTo 0) {
            lastNum = lastNum * 10 + (lhv / 10.pow(i)) % 10
            counter += 1
            if (lastNum / rhv != 0)
                break
        }
        // Первая строка
        writer.appendLine((if (lastNum.len <= ((lastNum / rhv) * rhv).len) " " else "") + "$lhv | $rhv")
        // Вторая строка
        writer.appendLine(
            "-${lastNum - lastNum % rhv}" +
                    buildString(' ', lenLhv - lastNum.len + 3) +
                    "${lhv / rhv}"
        )
        val result = intToListFor(lhv / rhv, 1)
        var indent = 0
        for ((counterOfResult, i) in intToListFor(lhv, lastNum.len).withIndex()) {
            val integerPart = lastNum - lastNum % rhv // То, что мы вычитаем
            val remains = lastNum % rhv // Остаток от деления
            writer.appendLine(
                // Отступ перед ----
                buildString(' ', indent) +
                        // Построили полосу из ----
                        buildString('-', max(lastNum.len, integerPart.len + 1)) + "\n" +
                        // Построили пробелы
                        buildString(' ', max(lastNum.len, integerPart.len + 1) + indent - remains.len) +
                        // Добавили остаток от деления и снесли цифру
                        "$remains$i\n" +
                        // Добавили пробелы перед вычитаемым
                        buildString(
                            ' ',
                            max(lastNum.len, integerPart.len + 1) + indent - (result[counterOfResult] * rhv).len
                        ) +
                        // Добавить вычитание
                        "-${result[counterOfResult] * rhv}"
            )
            indent =
                max(lastNum.len, integerPart.len + 1) + indent - max(
                    (result[counterOfResult] * rhv).len,
                    remains.len
                )
            lastNum = "$remains$i".toInt()
        }
        writer.appendLine(
            buildString(' ', indent) +
                    buildString('-', max(lastNum.len, (lastNum - lastNum % rhv).len + 1)) + "\n" +
                    buildString(
                        ' ',
                        max(lastNum.len, (lastNum - lastNum % rhv).len + 1) + indent - (lastNum % rhv).len
                    ) +
                    // Добавили остаток от деления и снесли цифру
                    "${lastNum % rhv}"
        )
    }
    writer.close()
}


