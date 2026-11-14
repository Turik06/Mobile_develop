# WidgetsDemo2728: Погодное приложение

Учебное приложение с демонстрацией работы виджетов **Spinner**, **RadioGroup / RadioButton** и **CheckBox**.

## Демонстрация работы

| Выбор шкалы (°C / °F) | Скрытие ветра по CheckBox | Выбор города из Spinner |
| :---: | :---: | :---: |
| ![Шкала C/F](screenshots/demo_celsius_wind.png) | ![Ветер скрыт](screenshots/demo_wind_hidden.png) | ![Выбор города](screenshots/demo_city_sochi.png) |

## Что сделано по заданию

1. **Spinner (выбор города)**: список городов хранится в `res/values/strings.xml` (`<string-array name="cities">`). При смене города в списке погода сразу обновляется.
2. **RadioButton (шкала температуры)**: выбор шкалы (°C или °F) через переключатели. При переключении температура мгновенно пересчитывается.
3. **CheckBox (направление ветра)**: управление отображением ветра. Если галочка стоит - показывается, если снята - скрывается.
4. **Хранение данных в JSON**: файл `assets/weather.json` хранит готовые данные для городов (работает без интернета).
5. **Дополнительный балл (OpenWeatherMap API + корутины)**: запрос свежей погоды через API с помощью корутин. Если интернет отключен, приложение само берёт данные из JSON.

## Структура проекта

* `MainActivity.kt`: логика виджетов (Spinner, RadioGroup, CheckBox) и обновление экрана.
* `WeatherRepository.kt`: загрузка погоды из OpenWeatherMap API или из файла `assets/weather.json`.
* `WeatherInfo.kt`: модель данных погоды и перевод температуры (°C в °F).
* `res/layout/activity_main.xml`: простой макет интерфейса с виджетами.
* `res/values/strings.xml`: список городов и строки.
* `assets/weather.json`: локальные сведения о погоде.
* `WeatherUnitTest.kt`: тесты формул и логики.
