import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.commons.csv.CSVFormat
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Serializable
data class CategoryGroup(
    val category: String,
    @SerialName("category_ru")
    val categoryRu: String,
    @SerialName("total_apps")
    val totalApps: Int,
    val apps: List<AppItem>
)

@Serializable
data class AppItem(
    val app: String,
    val category: String,
    @SerialName("category_ru")
    val categoryRu: String,
    val rating: Double?,
    val reviews: Long?,
    val size: String,
    val installs: Long,
    val type: String?,
    val price: Boolean,
    @SerialName("content_rating")
    val contentRating: String?,
    val genres: List<String>,
    @SerialName("last_updated")
    val lastUpdated: String?,
    @SerialName("current_ver")
    val currentVer: String?,
    @SerialName("android_ver")
    val androidVer: String?,
    @SerialName("min_android_api")
    val minAndroidApi: Int?
)


private val categoryTranslations = mapOf(
    "ART_AND_DESIGN" to "Искусство и дизайн",
    "AUTO_AND_VEHICLES" to "Автомобили и транспорт",
    "BEAUTY" to "Красота",
    "BOOKS_AND_REFERENCE" to "Книги и справочники",
    "BUSINESS" to "Бизнес",
    "COMICS" to "Комиксы",
    "COMMUNICATION" to "Связь и общение",
    "DATING" to "Знакомства",
    "EDUCATION" to "Образование",
    "ENTERTAINMENT" to "Развлечения",
    "EVENTS" to "Мероприятия",
    "FAMILY" to "Для всей семьи",
    "FINANCE" to "Финансы",
    "FOOD_AND_DRINK" to "Еда и напитки",
    "GAME" to "Игры",
    "HEALTH_AND_FITNESS" to "Здоровье и фитнес",
    "HOUSE_AND_HOME" to "Дом и интерьер",
    "LIBRARIES_AND_DEMO" to "Библиотеки и демо",
    "LIFESTYLE" to "Стиль жизни",
    "MAPS_AND_NAVIGATION" to "Карты и навигация",
    "MEDICAL" to "Медицина",
    "NEWS_AND_MAGAZINES" to "Новости и журналы",
    "PARENTING" to "Материнство и детство",
    "PERSONALIZATION" to "Персонализация",
    "PHOTOGRAPHY" to "Фотография",
    "PRODUCTIVITY" to "Продуктивность",
    "SHOPPING" to "Покупки",
    "SOCIAL" to "Социальные сети",
    "SPORTS" to "Спорт",
    "TOOLS" to "Инструменты",
    "TRAVEL_AND_LOCAL" to "Путешествия",
    "VIDEO_PLAYERS" to "Видеоплееры и редакторы",
    "WEATHER" to "Погода"
)


private val versionToApi = mapOf(
    "1.0" to 1, "1.1" to 2, "1.5" to 3, "1.6" to 4,
    "2.0" to 5, "2.0.1" to 6, "2.1" to 7, "2.2" to 8,
    "2.3" to 9, "2.3.3" to 10,
    "3.0" to 11, "3.1" to 12, "3.2" to 13,
    "4.0" to 14, "4.0.3" to 15, "4.1" to 16, "4.2" to 17,
    "4.3" to 18, "4.4" to 19, "4.4W" to 20,
    "5.0" to 21, "5.1" to 22, "6.0" to 23,
    "7.0" to 24, "7.1" to 25, "7.1.1" to 25,
    "8.0" to 26, "8.1" to 27, "9.0" to 28, "10.0" to 29
)

private val versionRegex = Regex("""^([0-9]+(?:\.[0-9]+)*(?:W)?)""")

private val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US)

private fun parseMinApi(rawVer: String?): Int? {
    if (rawVer.isNullOrBlank()
        || rawVer.equals("Varies with device", true)
        || rawVer.equals("NaN", true)
    ) return null

    val ver = versionRegex.find(rawVer.trim())?.groupValues?.get(1) ?: return null

    versionToApi[ver]?.let { return it }

    return ver.split(".").take(2).joinToString(".").let { versionToApi[it] }
}

fun main(args: Array<String>) {
    val inputFile  = File(args.getOrElse(0) { "googleplaystore.csv" })
    val outputFile = File(args.getOrElse(1) { "googleplaystore.json" })

    // Настройка парсера Apache Commons CSV
    val csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader()
        .setSkipHeaderRecord(true)
        .setIgnoreHeaderCase(true)
        .setTrim(true)
        .build()

    val apps = inputFile.bufferedReader().use { reader ->
        csvFormat.parse(reader).mapNotNull { row ->
            // Пропускаем строки с неверным числом колонок
            if (!row.isConsistent || row.size() < 13) return@mapNotNull null

            val category = row["Category"]
            // Пропускаем строки, где категория пустая или содержит число (повреждённые данные)
            if (category.isBlank() || category.toDoubleOrNull() != null) return@mapNotNull null

            val priceStr = row["Price"].replace("$", "").trim()
            val isPaid = (priceStr.toDoubleOrNull() ?: 0.0) > 0.0
                    || row["Type"].equals("Paid", true)

            AppItem(
                app           = row["App"],
                category      = category,
                categoryRu    = categoryTranslations[category.uppercase()] ?: category,
                rating        = row["Rating"].toDoubleOrNull()?.takeIf { !it.isNaN() },
                reviews       = row["Reviews"].toLongOrNull(),
                size          = row["Size"],
                installs      = row["Installs"].filter { it.isDigit() }.toLongOrNull() ?: 0L,
                type          = row["Type"].takeIf { it.isNotBlank() && it != "NaN" },
                price         = isPaid,
                contentRating = row["Content Rating"].takeIf { it.isNotBlank() && it != "NaN" },
                genres        = row["Genres"].split(";").map { it.trim() }.filter { it.isNotEmpty() },
                lastUpdated   = runCatching {
                    LocalDate.parse(row["Last Updated"], dateFormatter).toString()
                }.getOrNull(),
                currentVer    = row["Current Ver"].takeIf { it.isNotBlank() && it != "NaN" },
                androidVer    = row["Android Ver"].takeIf { it.isNotBlank() && it != "NaN" },
                minAndroidApi = parseMinApi(row["Android Ver"])
            )
        }
    }

    val grouped = apps.groupBy { it.category }.map { (category, categoryApps) ->
        CategoryGroup(
            category   = category,
            categoryRu = categoryTranslations[category.uppercase()] ?: category,
            totalApps  = categoryApps.size,
            apps       = categoryApps
        )
    }.sortedBy { it.category }

    val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }
    outputFile.writeText(json.encodeToString(grouped))

    println("Готово! ${apps.size} приложений в ${grouped.size} категориях → ${outputFile.path}")
}
