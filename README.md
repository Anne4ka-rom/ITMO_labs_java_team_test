# ITMO_labs_java_team_test
---
## Person 1 (Модели, хранение и базовые команды): -- Я
1. Пакет model (4 файла)
2. Пакет collection (1 файл)
3. Пакет file (1-2 файла)
4. Команды info, show, help (легко)
5. Команды filter_by_capacity, filter_less_than_type (используют его CollectionManager)
6. Команда sum_of_capacity (использует его CollectionManager)
7. Команда sort (использует его CollectionManager)

Коммиты:
- feat(model): add model classes
- feat(collection): implement collection manager
- feat(file): add xml file manager
- feat(commands): add info, show, help
- feat(commands): add filter commands
- feat(commands): add sum and sort


## Person 2 (Интерактив и сложные команды): -- Ты
1. Пакет utils (InputValidator) - для валидации ввода
2. Пакет exceptions
3. Команда add (самая сложная - интерактивный ввод)
4. Команда update (тоже сложная)
5. Команды remove (by_id, last, lower)
6. Команда clear
7. Команда save
8. Команда execute_script (очень сложная)
9. Main.java

Коммиты:
- feat(utils): add input validator
- feat(exceptions): add custom exceptions
- feat(commands): add interactive add command
- feat(commands): add update command
- feat(commands): add remove commands
- feat(commands): add save and clear
- feat(commands): add execute script
- feat(main): main application class
---
## Почему это равномерно
### Person 1:
Простые, хорошо структурированные задачи
Работает с данными, которые сам создал
Команды используют его CollectionManager

### Person 2:
Сложная логика ввода/вывода
Много разных команд
Но все они используют готовый CollectionManager
---
## Совет для работы
Person 1 начинает первым - создает модель и CollectionManager
Person 2 может начинать с utils и exceptions пока ждет
Через 2 дня у Person 1 готов CollectionManager, Person 2 начинает команды
Параллельно Person 1 добавляет file и простые команды
В конце вместе тестируют execute_script
---
## Создайте ветки:
person1-core - модели, коллекция, файлы
person2-interactive - валидация, сложные команды
develop - для слияния
main - финальная версия
---
## День 1 - Настройка и база
### Person 1 (Модели)
- Создать структуру пакетов в проекте
- Реализовать Coordinates.java с валидацией x ≤ 636, x не null
- Реализовать VehicleType.java (enum CAR, SUBMARINE, BICYCLE, HOVERBOARD)
- Реализовать FuelType.java (enum GASOLINE, KEROSENE, ALCOHOL, MANPOWER)
- Начать Vehicle.java (поля, конструкторы)
Результат: Готовы базовые классы данных

### Person 2 (Инструменты)
- Создать .gitignore (исключить .class, target/, .vscode/, .idea/)
- Написать InputValidator.java с методами:
  - validateName(String name)
  - validateCoordinateX(Double x)
  - validateEnginePower(Double enginePower)
  - validateCapacity(double capacity)
- Создать InvalidDataException.java
- Создать FileAccessException.java

Результат: Готовы валидаторы и исключения

## День 2 - Ядро коллекции
### Person 1
- Закончить Vehicle.java:
  - Все геттеры/сеттеры с валидацией
  - compareTo(Vehicle o) для сортировки по умолчанию
  - toString(), equals(), hashCode()
- Создать IdGenerator.java
- Создать CollectionManager.java:
  - Поле Stack<Vehicle> collection
  - Конструктор
  - Метод add(Vehicle vehicle) с генерацией ID

Результат: Можно добавлять объекты в коллекцию

### Person 2
- Создать ScriptException.java
- Написать базовую структуру CommandExecutor.java:
  - Поля CollectionManager, Scanner, isRunning
  - Конструктор
  - Метод start() - главный цикл
  - Заготовки под все 15 команд (пустые методы)
  - Реализовать команду help (вывод всех команд)

Результат: Есть каркас команд, работает help

## День 3 - XML и простые команды
### Person 1
- Создать FileManager.java:
  - Конструктор с именем файла
  - Метод saveCollection(Stack<Vehicle> collection) через FileOutputStream
  - Метод loadCollection() через FileReader
- Ручной парсинг XML (без библиотек)
- Протестировать сохранение и загрузку

Результат: Данные сохраняются и загружаются

### Person 2
- В CommandExecutor реализовать:
  - info - вызывает collectionManager.getInfo()
  - show - вызывает collectionManager.showAll()
  - clear - очистка коллекции
  - exit - завершение программы
- Добавить обработку неизвестных команд

Результат: Работают базовые команды info, show, clear, exit

## День 4 - Команда add и фильтры
### Person 1
- В CollectionManager добавить методы фильтрации:
  - filterByCapacity(double capacity)
  - filterLessThanType(VehicleType type)
  - getSumOfCapacity()
- Реализовать sort() (Collections.sort)
- Добавить поле initializationDate для info

Результат: Коллекция умеет фильтровать и сортировать

### Person 2
- Реализовать команду add:
  - Построчный ввод с приглашениями
  - Для каждого поля вызывать InputValidator
  - При ошибке - повтор ввода
  - Для enum выводить список констант
  - Null через пустую строку
- Протестировать add с разными данными

Результат: Можно интерактивно добавлять элементы

## День 5 - Update и Remove
### Person 1
- В CollectionManager добавить:
  - updateById(int id, Vehicle newVehicle)
  - removeById(int id)
  - removeLast()
  - removeLower(Vehicle vehicle)
  - containsId(int id) для проверки

Результат: CollectionManager поддерживает все операции удаления

### Person 2
- Реализовать команды:
  - update id - проверить существование id, затем ввод как в add
  - remove_by_id id
  - remove_last
  - remove_lower - запросить элемент, затем удалить меньшие
- Добавить проверки на существование id

Результат: Работают все команды изменения коллекции

## День 6 - Фильтры и вычисления
### Person 1
- Исправить баги в CollectionManager
- Помочь Person 2 с интеграцией
- Начать писать JavaDoc для своих классов

Результат: Стабильная коллекция

### Person 2
- Реализовать команды:
  - filter_by_capacity capacity
  - filter_less_than_type type
  - sum_of_capacity
  - sort
- Добавить вывод результатов фильтрации
- Реализовать save - вызывает fileManager.saveCollection()

Результат: Работают все 15 команд, кроме execute_script

## День 7 - Execute script и финальная интеграция
### Person 1
- Закончить JavaDoc для model, collection, file
- Проверить все граничные случаи (пустая коллекция, null значения)
- Исправить найденные баги

### Person 2
- Реализовать execute_script file_name:
  - Чтение файла
  - Защита от рекурсии (Set имён выполняемых скриптов)
  - Поддержка комментариев (#)
  - Пропуск пустых строк
  - Для add/update читать поля из следующих строк
- Протестировать на разных скриптах

Результат: Работают все 15 команд, включая скрипты

## День 8 - Финальная полировка
### Person 1
- Проверить все требования к полям (больше 0, не null и т.д.)
- Сгенерировать JavaDoc для своих пакетов
- Создать пример data/vehicles.xml с тестовыми данными

### Person 2
- Сгенерировать JavaDoc для своих пакетов
- Создать папку scripts/ с примерами:
  - create_test_data.txt - создание тестовых объектов
  - test_all.txt - тест всех команд
  - recursive_test.txt - для проверки защиты от рекурсии
- Написать README.md с инструкцией по запуску

### Вместе
- Протестировать все команды
- Проверить обработку ошибок:
- Неверный ввод
- Отсутствие файла
- Нет прав на чтение/запись
- Рекурсия в скриптах
- Собрать финальную версию
