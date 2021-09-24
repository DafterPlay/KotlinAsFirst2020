@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson5.task1

import ru.spbstu.wheels.allIndexed
import ru.spbstu.wheels.defaultCopy
import java.util.*
import kotlin.math.max

// Урок 5: ассоциативные массивы и множества
// Максимальное количество баллов = 14
// Рекомендуемое количество баллов = 9
// Вместе с предыдущими уроками = 33/47

/**
 * Пример
 *
 * Для заданного списка покупок `shoppingList` посчитать его общую стоимость
 * на основе цен из `costs`. В случае неизвестной цены считать, что товар
 * игнорируется.
 */
fun shoppingListCost(
    shoppingList: List<String>,
    costs: Map<String, Double>
): Double {
    var totalCost = 0.0

    for (item in shoppingList) {
        val itemCost = costs[item]
        if (itemCost != null) {
            totalCost += itemCost
        }
    }

    return totalCost
}

/**
 * Пример
 *
 * Для набора "имя"-"номер телефона" `phoneBook` оставить только такие пары,
 * для которых телефон начинается с заданного кода страны `countryCode`
 */
fun filterByCountryCode(
    phoneBook: MutableMap<String, String>,
    countryCode: String
) {
    val namesToRemove = mutableListOf<String>()

    for ((name, phone) in phoneBook) {
        if (!phone.startsWith(countryCode)) {
            namesToRemove.add(name)
        }
    }

    for (name in namesToRemove) {
        phoneBook.remove(name)
    }
}

/**
 * Пример
 *
 * Для заданного текста `text` убрать заданные слова-паразиты `fillerWords`
 * и вернуть отфильтрованный текст
 */
fun removeFillerWords(
    text: List<String>,
    vararg fillerWords: String
): List<String> {
    val fillerWordSet = setOf(*fillerWords)

    val res = mutableListOf<String>()
    for (word in text) {
        if (word !in fillerWordSet) {
            res += word
        }
    }
    return res
}

/**
 * Пример
 *
 * Для заданного текста `text` построить множество встречающихся в нем слов
 */
fun buildWordSet(text: List<String>): MutableSet<String> {
    val res = mutableSetOf<String>()
    for (word in text) res.add(word)
    return res
}


/**
 * Простая (2 балла)
 *
 * По заданному ассоциативному массиву "студент"-"оценка за экзамен" построить
 * обратный массив "оценка за экзамен"-"список студентов с этой оценкой".
 *
 * Например:
 *   buildGrades(mapOf("Марат" to 3, "Семён" to 5, "Михаил" to 5))
 *     -> mapOf(5 to listOf("Семён", "Михаил"), 3 to listOf("Марат"))
 */
fun buildGrades(grades: Map<String, Int>): Map<Int, List<String>> {
    val v = mutableMapOf<Int, MutableList<String>>()
    for ((i, j) in grades) {
        when (v[j]) {
            null -> v[j] = mutableListOf(i)
            else -> v[j]!! += i
        }
    }
    return v
}

/**
 * Простая (2 балла)
 *
 * Определить, входит ли ассоциативный массив a в ассоциативный массив b;
 * это выполняется, если все ключи из a содержатся в b с такими же значениями.
 *
 * Например:
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "z", "b" to "sweet")) -> true
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "zee", "b" to "sweet")) -> false
 */
fun containsIn(a: Map<String, String>, b: Map<String, String>): Boolean {
    for ((i, j) in a) {
        if (b[i] != j) return false
    }
    return true
}

/**
 * Простая (2 балла)
 *
 * Удалить из изменяемого ассоциативного массива все записи,
 * которые встречаются в заданном ассоциативном массиве.
 * Записи считать одинаковыми, если и ключи, и значения совпадают.
 *
 * ВАЖНО: необходимо изменить переданный в качестве аргумента
 *        изменяемый ассоциативный массив
 *
 * Например:
 *   subtractOf(a = mutableMapOf("a" to "z"), mapOf("a" to "z"))
 *     -> a changes to mutableMapOf() aka becomes empty
 */
fun subtractOf(a: MutableMap<String, String>, b: Map<String, String>) {
    for ((i, j) in b) {
        if (a[i] == j) a.remove(i)
    }
}

/**
 * Простая (2 балла)
 *
 * Для двух списков людей найти людей, встречающихся в обоих списках.
 * В выходном списке не должно быть повторяющихся элементов,
 * т. е. whoAreInBoth(listOf("Марат", "Семён, "Марат"), listOf("Марат", "Марат")) == listOf("Марат")
 */
fun whoAreInBoth(a: List<String>, b: List<String>): List<String> = a.intersect(b).toList()

/**
 * Средняя (3 балла)
 *
 * Объединить два ассоциативных массива `mapA` и `mapB` с парами
 * "имя"-"номер телефона" в итоговый ассоциативный массив, склеивая
 * значения для повторяющихся ключей через запятую.
 * В случае повторяющихся *ключей* значение из mapA должно быть
 * перед значением из mapB.
 *
 * Повторяющиеся *значения* следует добавлять только один раз.
 *
 * Например:
 *   mergePhoneBooks(
 *     mapOf("Emergency" to "112", "Police" to "02"),
 *     mapOf("Emergency" to "911", "Police" to "02")
 *   ) -> mapOf("Emergency" to "112, 911", "Police" to "02")
 */
fun mergePhoneBooks(mapA: Map<String, String>, mapB: Map<String, String>): Map<String, String> {
    val phones = mutableMapOf<String, String>()
    for (map in listOf(mapA, mapB)) {
        for ((i, j) in map) {
            when {
                phones[i] == null -> phones[i] = j
                phones[i] != j -> phones[i] += ", $j"
            }
        }
    }
    return phones
}

/**
 * Средняя (4 балла)
 *
 * Для заданного списка пар "акция"-"стоимость" вернуть ассоциативный массив,
 * содержащий для каждой акции ее усредненную стоимость.
 *
 * Например:
 *   averageStockPrice(listOf("MSFT" to 100.0, "MSFT" to 200.0, "NFLX" to 40.0))
 *     -> mapOf("MSFT" to 150.0, "NFLX" to 40.0)
 */
fun averageStockPrice(stockPrices: List<Pair<String, Double>>): Map<String, Double> {
    val answer = mutableMapOf<String, Double>()
    for ((first) in stockPrices.associateBy { it.first }) {
        val filteredMas = stockPrices.filter { first == it.first }
        answer[first] = filteredMas.fold(0.0) { e1, e2 -> e1 + e2.second } / filteredMas.size
    }
    return answer
}

/**
 * Средняя (4 балла)
 *
 * Входными данными является ассоциативный массив
 * "название товара"-"пара (тип товара, цена товара)"
 * и тип интересующего нас товара.
 * Необходимо вернуть название товара заданного типа с минимальной стоимостью
 * или null в случае, если товаров такого типа нет.
 *
 * Например:
 *   findCheapestStuff(
 *     mapOf("Мария" to ("печенье" to 20.0), "Орео" to ("печенье" to 100.0)),
 *     "печенье"
 *   ) -> "Мария"
 */
fun findCheapestStuff(stuff: Map<String, Pair<String, Double>>, kind: String): String? {
    val filteredMas = stuff.filter { it.value.first == kind }
    if (filteredMas.isEmpty()) return null
    var min = Double.MAX_VALUE
    var name = ""
    for ((key, value) in filteredMas) if (value.second <= min) {
        min = value.second
        name = key
    }
    return name
}

/**
 * Средняя (3 балла)
 *
 * Для заданного набора символов определить, можно ли составить из него
 * указанное слово (регистр символов игнорируется)
 *
 * Например:
 *   canBuildFrom(listOf('a', 'b', 'o'), "baobab") -> true
 */
fun canBuildFrom(chars: List<Char>, word: String): Boolean {
    val a = word.lowercase(Locale.getDefault()).toSet()
    val b: List<Char> = chars.map { it.lowercaseChar() }
    return b.containsAll(a)
}

/**
 * Средняя (4 балла)
 *
 * Найти в заданном списке повторяющиеся элементы и вернуть
 * ассоциативный массив с информацией о числе повторений
 * для каждого повторяющегося элемента.
 * Если элемент встречается только один раз, включать его в результат
 * не следует.
 *
 * Например:
 *   extractRepeats(listOf("a", "b", "a")) -> mapOf("a" to 2)
 */
fun extractRepeats(list: List<String>): Map<String, Int> {
    val v = mutableMapOf<String, Int>()
    for (i in list) {
        v[i] = v[i]?.plus(1) ?: 1
    }
    return v.filter { it.value > 1 }
}

/**
 * Средняя (3 балла)
 *
 * Для заданного списка слов определить, содержит ли он анаграммы.
 * Два слова здесь считаются анаграммами, если они имеют одинаковую длину
 * и одно можно составить из второго перестановкой его букв.
 * Скажем, тор и рот или роза и азор это анаграммы,
 * а поле и полено -- нет.
 *
 * Например:
 *   hasAnagrams(listOf("тор", "свет", "рот")) -> true
 */

fun hasAnagrams(words: List<String>): Boolean {
    val v = mutableListOf<String>()
    for (element in words) {
        //Сортировка букв в слове
        v.add(element.toSortedSet().joinToString(separator = "", postfix = ""))
    }
    //Расположение слов по порядку
    v.sort()
    for (i in 1 until v.size) {
        if (v[i - 1] == v[i]) return true
    }
    return false
}

/**
 * Сложная (5 баллов)
 *
 * Для заданного ассоциативного массива знакомых через одно рукопожатие `friends`
 * необходимо построить его максимальное расширение по рукопожатиям, то есть,
 * для каждого человека найти всех людей, с которыми он знаком через любое
 * количество рукопожатий.
 *
 * Считать, что все имена людей являются уникальными, а также что рукопожатия
 * являются направленными, то есть, если Марат знает Свету, то это не означает,
 * что Света знает Марата.
 *
 * Оставлять пустой список знакомых для людей, которые их не имеют (см. EvilGnome ниже),
 * в том числе для случая, когда данного человека нет в ключах, но он есть в значениях
 * (см. GoodGnome ниже).
 *
 * Например:
 *   propagateHandshakes(
 *     mapOf(
 *       "Marat" to setOf("Mikhail", "Sveta"),
 *       "Sveta" to setOf("Marat"),
 *       "Mikhail" to setOf("Sveta"),
 *       "Friend" to setOf("GoodGnome"),
 *       "EvilGnome" to setOf()
 *     )
 *   ) -> mapOf(
 *          "Marat" to setOf("Mikhail", "Sveta"),
 *          "Sveta" to setOf("Marat", "Mikhail"),
 *          "Mikhail" to setOf("Sveta", "Marat"),
 *          "Friend" to setOf("GoodGnome"),
 *          "EvilGnome" to setOf(),
 *          "GoodGnome" to setOf()
 *        )
 */
fun propagateHandshakes(friends: Map<String, Set<String>>): Map<String, Set<String>> {
    val newFriends = mutableMapOf<String, Set<String>>()
    // Создаем пустые списки для каждого имени, встречающегося в мапе
    for ((_, names) in friends) {
        for (name in names) newFriends[name] = setOf()
    }
    // Заполняем именами те, которые есть в изначальном списке
    newFriends.putAll(friends)
    // Перебор всех имен
    do {
        var exit = false
        for ((name, friendSet) in newFriends) {
            // Пока список друзей меняется, добовлять друзей по рукопожатию первого порядка
            do {
                val size = newFriends[name]!!.count()
                for (friendName in friendSet) {
                    newFriends[name] = newFriends[name]!!.union(newFriends[friendName]!!)
                }
                // Защита, от взаимной дружбы
                newFriends[name] = newFriends[name]!! - name
                if (size != newFriends[name]!!.count()) exit = true
            } while (size != newFriends[name]!!.count())
        }
    } while (exit)

    return newFriends
}

/**
 * Сложная (6 баллов)
 *
 * Для заданного списка неотрицательных чисел и числа определить,
 * есть ли в списке пара чисел таких, что их сумма равна заданному числу.
 * Если да, верните их индексы в виде Pair<Int, Int>;
 * если нет, верните пару Pair(-1, -1).
 *
 * Индексы в результате должны следовать в порядке (меньший, больший).
 *
 * Постарайтесь сделать ваше решение как можно более эффективным,
 * используя то, что вы узнали в данном уроке.
 *
 * Например:
 *   findSumOfTwo(listOf(1, 2, 3), 4) -> Pair(0, 2)
 *   findSumOfTwo(listOf(1, 2, 3), 6) -> Pair(-1, -1)
 */
fun maxPair(pair: Pair<Int, Int>): Pair<Int, Int> {
    return when {
        pair.first < pair.second -> pair
        else -> Pair(pair.second, pair.first)
    }
}

fun nextIndexInList(list: List<Int>, number: Int): Pair<Int, Int> {
    val a = list.toMutableList()
    val b = a.indexOf(number)
    a[a.indexOf(number)] = -1
    return Pair(b, a.indexOf(number))
}

fun findSumOfTwo(list: List<Int>, number: Int): Pair<Int, Int> {
    val newSet = list.toSet().sorted()
    for (i in newSet) {
        when {
            number - i < 0 -> return Pair(-1, -1)
            number - i == i && list.filter { it == i }.size >= 2 ->
                return maxPair(nextIndexInList(list, i))
            number - i != i && number - i in newSet -> return maxPair(Pair(list.indexOf(number - i), list.indexOf(i)))
        }
    }
    return Pair(-1, -1)
}

/**
 * Очень сложная (8 баллов)
 *
 * Входными данными является ассоциативный массив
 * "название сокровища"-"пара (вес сокровища, цена сокровища)"
 * и вместимость вашего рюкзака.
 * Необходимо вернуть множество сокровищ с максимальной суммарной стоимостью,
 * которые вы можете унести в рюкзаке.
 *
 * Перед решением этой задачи лучше прочитать статью Википедии "Динамическое программирование".
 *
 * Например:
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     850
 *   ) -> setOf("Кубок")
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     450
 *   ) -> emptySet()
 */
// Алгоритм взят с https://clck.ru/XT9jH, однако вместо написания рекурсивной функции
// я создал массив - дублер, только уже с набором сокровищь т.к. рекурсивный алгоритм сложен в реализации
// при заданных типах данных и без использования глобальных переменных
fun bagPacking(treasures: Map<String, Pair<Int, Int>>, capacity: Int): Set<String> {
    val newTreasures = treasures.filter { it.value.first <= capacity }.toList()
    if (newTreasures.isEmpty()) return setOf()
    val n = newTreasures.size
    val a: List<MutableList<Int>> = List(n + 1) { MutableList(capacity + 1) { 0 } }
    val b: List<MutableList<Set<String>>> = List(n + 1) { MutableList(capacity + 1) { setOf() } }
    for (k in 1..n) {
        for (s in 1..capacity) {
            if (s >= newTreasures[k - 1].second.first) {
                val max = max(
                    a[k - 1][s], a[k - 1][s - newTreasures[k - 1].second.first] + newTreasures[k - 1].second.second
                )
                a[k][s] = max
                if (max == a[k - 1][s]) b[k][s] = b[k - 1][s]
                else b[k][s] = b[k - 1][s - newTreasures[k - 1].second.first] + newTreasures[k - 1].first
            } else {
                a[k][s] = a[k - 1][s]
                b[k][s] = b[k - 1][s]
            }
        }
    }
    return b[n][capacity]
}
