# CurrentWeatherDataBinding

Android-приложение для просмотра погоды с использованием сервиса **OpenWeatherMap API**, механизма **Data Binding**, корутин Kotlin и парсинга JSON через **Gson**.

## Скриншот работы приложения

![Работа приложения](screenshots/screenshot_app.png)

## Особенности реализации

* **Data Binding**: разметка `activity_main.xml` обёрнута в тег `<layout>` и использует блок `<data>` с переменными `activity`, `weather`, `loading`, `message`.
* **Привязка данных в UI**:
  * Название города (`@{weather.city}`)
  * Температура (`@{@string/temperature(weather.temperature)}`)
  * Описание погоды (`@{weather.description}`)
  * Отображение индикатора загрузки и ошибок через Data Binding (`View.VISIBLE` / `View.GONE`)
* **Выбор города**: выпадающий список `Spinner` с перечнем городов из строковых ресурсов (`R.array.cities`), автоматическое обновление данных при смене выбора.
* **Обновление по кнопке**: кнопка «Обновить погоду» вызывает метод через привязку выражения Data Binding: `android:onClick="@{() -> activity.loadWeather()}"`.
* **Асинхронность**: сетевые запросы выполняются на `Dispatchers.IO` с помощью корутин Kotlin (`WeatherApi.loadWeather`).
* **Обработка ошибок**: обработка сетевых ошибок (отсутствие интернета, неверный код ответа, некорректный API-ключ, город не найден).
* **Тестирование**: модульные тесты парсера погоды в `WeatherTest.kt`.

## Структура проекта

* `Weather.kt` — модель данных погоды с методом парсинга `fromJson` через `JsonParser`.
* `WeatherApi.kt` — объект сетевого взаимодействия с OpenWeatherMap API с использованием `HttpURLConnection`.
* `MainActivity.kt` — Activity, настраивающая Data Binding, адаптер городов и запуск корутин.
* `res/layout/activity_main.xml` — разметка интерфейса с использованием тега `<layout>` и секции `<data>`.
* `res/values/strings.xml` — строковые ресурсы, ключ API и массив городов.
* `WeatherTest.kt` — unit-тесты корректности парсинга и валидации JSON.
