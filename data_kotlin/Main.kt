import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class App(
    val name: String, val category: String, val categoryRu: String,
    val rating: Double?, val reviews: Long?, val size: String,
    val installs: Long, val type: String, val isPaid: Boolean,
    val contentRating: String, val genres: List<String>,
    val lastUpdated: String, val currentVer: String, val androidApi: Int?
)

/** Перевод категорий на русский язык */
val categoryRu = mapOf(
    "ART_AND_DESIGN" to "Искусство и дизайн", "AUTO_AND_VEHICLES" to "Автомобили и транспорт",
    "BEAUTY" to "Красота", "BOOKS_AND_REFERENCE" to "Книги и справочники",
    "BUSINESS" to "Бизнес", "COMICS" to "Комиксы", "COMMUNICATION" to "Связь",
    "DATING" to "Знакомства", "EDUCATION" to "Образование", "ENTERTAINMENT" to "Развлечения",
    "EVENTS" to "События", "FAMILY" to "Семья", "FINANCE" to "Финансы",
    "FOOD_AND_DRINK" to "Еда и напитки", "GAME" to "Игры",
    "HEALTH_AND_FITNESS" to "Здоровье и фитнес", "HOUSE_AND_HOME" to "Жилье и дом",
    "LIBRARIES_AND_DEMO" to "Библиотеки и демо", "LIFESTYLE" to "Стиль жизни",
    "MAPS_AND_NAVIGATION" to "Карты и навигация", "MEDICAL" to "Медицина",
    "NEWS_AND_MAGAZINES" to "Новости и журналы", "PARENTING" to "Материнство и детство",
    "PERSONALIZATION" to "Персонализация", "PHOTOGRAPHY" to "Фотография",
    "PRODUCTIVITY" to "Продуктивность", "SHOPPING" to "Покупки",
    "SOCIAL" to "Социальные сети", "SPORTS" to "Спорт", "TOOLS" to "Инструменты",
    "TRAVEL_AND_LOCAL" to "Путешествия", "VIDEO_PLAYERS" to "Видеоплееры и редакторы",
    "WEATHER" to "Погода"
)

/** Маппинг версии Android → номер API (порядок важен: более длинные префиксы первыми) */
val apiMap = listOf(
    "1.0" to 1, "1.1" to 2, "1.5" to 3, "1.6" to 4, "2.0.1" to 6, "2.0" to 5,
    "2.1" to 7, "2.2" to 8, "2.3.3" to 10, "2.3" to 9, "3.0" to 11, "3.1" to 12,
    "3.2" to 13, "4.0.3" to 15, "4.0" to 14, "4.1" to 16, "4.2" to 17, "4.3" to 18,
    "4.4W" to 20, "4.4" to 19, "5.0" to 21, "5.1" to 22, "6.0" to 23,
    "7.0" to 24, "7.1" to 25, "8.0" to 26, "8.1" to 27
)

fun getApi(ver: String): Int? {
    val v = ver.trim()
    if (v == "NaN" || v == "Varies with device") return null
    return apiMap.firstOrNull { v.startsWith(it.first) }?.second
}

/** Разбор строки CSV с учётом кавычек */
fun parseCsvLine(line: String): List<String> {
    val cols = mutableListOf<String>()
    val cur = StringBuilder()
    var q = false
    for (ch in line) when {
        ch == '"'          -> q = !q
        ch == ',' && !q    -> { cols.add(cur.toString()); cur.clear() }
        else               -> cur.append(ch)
    }
    cols.add(cur.toString())
    return cols
}

/** Экранирование строки для JSON */
fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
    .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t")

val dateFmt = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)

fun main() {
    val lines = File("googleplaystore.csv").readLines()

    // Парсинг CSV → список App, пропуская битые строки
    val apps = lines.drop(1).mapNotNull { line ->
        val c = parseCsvLine(line)
        if (c.size != 13) return@mapNotNull null
        App(
            name = c[0], category = c[1], categoryRu = categoryRu[c[1]] ?: c[1],
            rating = if (c[2] == "NaN") null else c[2].toDoubleOrNull(),
            reviews = c[3].toLongOrNull(), size = c[4],
            installs = c[5].filter { it.isDigit() }.toLongOrNull() ?: 0L,
            type = c[6],
            isPaid = c[6] == "Paid" || c[7].replace("$", "").trim().let { it != "0" && it != "0.0" && it != "" },
            contentRating = c[8],
            genres = c[9].split(";").map { it.trim() }.filter { it.isNotEmpty() },
            lastUpdated = try { LocalDate.parse(c[10].trim(), dateFmt).toString() } catch (_: Exception) { c[10].trim() },
            currentVer = c[11], androidApi = getApi(c[12])
        )
    }

    // Группировка по категориям и запись JSON
    val grouped = apps.groupBy { it.category }

    File("googleplaystore.json").bufferedWriter().use { w ->
        w.write("{\n")
        grouped.entries.forEachIndexed { ci, (cat, catApps) ->
            w.write("  \"${esc(cat)}\": {\n")
            w.write("    \"category\": \"${esc(cat)}\",\n")
            w.write("    \"category_ru\": \"${esc(categoryRu[cat] ?: cat)}\",\n")
            w.write("    \"total_apps\": ${catApps.size},\n")
            w.write("    \"apps\": [\n")
            catApps.forEachIndexed { ai, a ->
                val g = a.genres.joinToString(", ") { "\"${esc(it)}\"" }
                w.write("      {\n")
                w.write("        \"App\": \"${esc(a.name)}\",\n")
                w.write("        \"Category\": \"${esc(a.category)}\",\n")
                w.write("        \"Category_RU\": \"${esc(a.categoryRu)}\",\n")
                w.write("        \"Rating\": ${a.rating},\n")
                w.write("        \"Reviews\": ${a.reviews},\n")
                w.write("        \"Size\": \"${esc(a.size)}\",\n")
                w.write("        \"Installs\": ${a.installs},\n")
                w.write("        \"Type\": \"${esc(a.type)}\",\n")
                w.write("        \"Price\": ${a.isPaid},\n")
                w.write("        \"Content Rating\": \"${esc(a.contentRating)}\",\n")
                w.write("        \"Genres\": [$g],\n")
                w.write("        \"Last Updated\": \"${esc(a.lastUpdated)}\",\n")
                w.write("        \"Current Ver\": \"${esc(a.currentVer)}\",\n")
                w.write("        \"Android Ver\": ${a.androidApi}\n")
                w.write(if (ai < catApps.size - 1) "      },\n" else "      }\n")
            }
            w.write("    ]\n")
            w.write(if (ci < grouped.size - 1) "  },\n" else "  }\n")
        }
        w.write("}\n")
    }
    println("Готово! ${apps.size} приложений, ${grouped.size} категорий → googleplaystore.json")
}
