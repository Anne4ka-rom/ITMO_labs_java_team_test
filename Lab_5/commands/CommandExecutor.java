package commands;

import collection.CollectionManager;
import model.*;
import utils.InputValidator;

import java.io.*;
import java.util.*;

/**
 * Единый класс для обработки всех команд.
 * Содержит логику выполнения всех 15 команд.
 */
public class CommandExecutor {
    private final CollectionManager collectionManager;
    private final Scanner scanner;
    private boolean isRunning;
    private final Set<String> scriptsInProgress;

    public CommandExecutor(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        this.scriptsInProgress = new HashSet<>();
    }

    /**
     * Запускает основной цикл обработки команд
     */
    public void start() {
        System.out.println("Программа запущена. Введите 'help' для списка команд.");
        while (isRunning) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                processCommand(input.split("\\s+"));
            }
        }
        scanner.close();
    }

    /**
     * Обрабатывает команду
     */
    private void processCommand(String[] commandParts) {
        String command = commandParts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(commandParts, 1, commandParts.length);

        try {
            switch (command) {
                case "help":
                    printHelp();
                    break;
                case "info":
                    System.out.println(collectionManager.getInfo());
                    break;
                case "show":
                    collectionManager.showAll();
                    break;
                case "add":
                    handleAdd();
                    break;
                case "update":
                    handleUpdate(args);
                    break;
                case "remove_by_id":
                    handleRemoveById(args);
                    break;
                case "clear":
                    collectionManager.clear();
                    System.out.println("Коллекция очищена");
                    break;
                case "save":
                    collectionManager.save();
                    break;
                case "execute_script":
                    handleExecuteScript(args);
                    break;
                case "exit":
                    isRunning = false;
                    System.out.println("Программа завершена");
                    break;
                case "remove_last":
                    collectionManager.removeLast();
                    break;
                case "remove_lower":
                    handleRemoveLower();
                    break;
                case "sort":
                    collectionManager.sort();
                    break;
                case "sum_of_capacity":
                    System.out.println("Сумма capacity: " + collectionManager.getSumOfCapacity());
                    break;
                case "filter_by_capacity":
                    handleFilterByCapacity(args);
                    break;
                case "filter_less_than_type":
                    handleFilterLessThanType(args);
                    break;
                default:
                    System.out.println("Неизвестная команда. Введите 'help' для справки.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка в аргументах: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка выполнения команды: " + e.getMessage());
        }
    }

    /**
     * Выводит справку по командам
     */
    private void printHelp() {
        System.out.println("""
                Доступные команды:
                help                                      - вывести справку
                info                                      - информация о коллекции
                show                                      - вывести все элементы
                add                                       - добавить новый элемент
                update id                                 - обновить элемент по id
                remove_by_id id                           - удалить элемент по id
                clear                                     - очистить коллекцию
                save                                      - сохранить коллекцию в файл
                execute_script file_name                  - выполнить скрипт
                exit                                      - завершить программу
                remove_last                               - удалить последний элемент
                remove_lower                              - удалить элементы меньше заданного
                sort                                      - отсортировать коллекцию
                sum_of_capacity                           - сумма значений capacity
                filter_by_capacity capacity               - фильтр по capacity
                filter_less_than_type type                - фильтр по типу (CAR, SUBMARINE, BICYCLE, HOVERBOARD)
                """);
    }

    /**
     * Обрабатывает команду add
     */
    private void handleAdd() {
        Vehicle vehicle = readVehicle();
        collectionManager.add(vehicle);
    }

    /**
     * Обрабатывает команду update
     */
    private void handleUpdate(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите id элемента");
            return;
        }

        try {
            int id = Integer.parseInt(args[0]);
            if (!collectionManager.containsId(id)) {
                System.out.println("Элемент с id " + id + " не найден");
                return;
            }

            Vehicle vehicle = readVehicle();
            if (collectionManager.updateById(id, vehicle)) {
                System.out.println("Элемент с id " + id + " обновлен");
            }
        } catch (NumberFormatException e) {
            System.out.println("id должен быть числом");
        }
    }

    /**
     * Обрабатывает команду remove_by_id
     */
    private void handleRemoveById(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите id элемента");
            return;
        }

        try {
            int id = Integer.parseInt(args[0]);
            if (collectionManager.removeById(id)) {
                System.out.println("Элемент с id " + id + " удален");
            } else {
                System.out.println("Элемент с id " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("id должен быть числом");
        }
    }

    /**
     * Обрабатывает команду execute_script
     */
    private void handleExecuteScript(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите имя файла");
            return;
        }

        String filename = args[0];

        // Защита от рекурсии
        if (scriptsInProgress.contains(filename)) {
            System.out.println("Ошибка: обнаружена рекурсия! Файл " + filename + " уже выполняется");
            return;
        }

        scriptsInProgress.add(filename);

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            System.out.println("Выполнение скрипта: " + filename);
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                System.out.println("[" + filename + ":" + lineNumber + "] " + line);
                processCommand(line.split("\\s+"));
            }

            System.out.println("Скрипт " + filename + " выполнен");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } finally {
            scriptsInProgress.remove(filename);
        }
    }

    /**
     * Обрабатывает команду remove_lower
     */
    private void handleRemoveLower() {
        System.out.println("Введите элемент для сравнения:");
        Vehicle vehicle = readVehicle();
        collectionManager.removeLower(vehicle);
    }

    /**
     * Обрабатывает команду filter_by_capacity
     */
    private void handleFilterByCapacity(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите значение capacity");
            return;
        }

        try {
            double capacity = Double.parseDouble(args[0]);
            List<Vehicle> result = collectionManager.filterByCapacity(capacity);

            if (result.isEmpty()) {
                System.out.println("Элементы с capacity = " + capacity + " не найдены");
            } else {
                System.out.println("Найдено элементов: " + result.size());
                result.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("capacity должно быть числом");
        }
    }

    /**
     * Обрабатывает команду filter_less_than_type
     */
    private void handleFilterLessThanType(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите тип (CAR, SUBMARINE, BICYCLE, HOVERBOARD)");
            return;
        }

        try {
            VehicleType type = InputValidator.validateAndParseVehicleType(args[0]);
            List<Vehicle> result = collectionManager.filterLessThanType(type);

            if (result.isEmpty()) {
                System.out.println("Элементы с типом меньше " + type + " не найдены");
            } else {
                System.out.println("Найдено элементов: " + result.size());
                result.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Читает объект Vehicle с консоли
     */
    private Vehicle readVehicle() {
        Vehicle vehicle = new Vehicle();
        System.out.println("Введите данные транспортного средства:");

        // Ввод имени
        while (true) {
            System.out.print("  name (не пустое): ");
            String input = scanner.nextLine().trim();
            try {
                InputValidator.validateName(input);
                vehicle.setName(input);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("  Ошибка: " + e.getMessage());
            }
        }

        // Ввод координаты X
        while (true) {
            System.out.print("  coordinate x (Double, <= 636): ");
            String input = scanner.nextLine().trim();
            try {
                Double x = input.isEmpty() ? null : Double.parseDouble(input);
                InputValidator.validateCoordinateX(x);

                Coordinates coords = vehicle.getCoordinates();
                if (coords == null) coords = new Coordinates();
                coords.setX(x);
                vehicle.setCoordinates(coords);
                break;
            } catch (NumberFormatException e) {
                System.out.println("  Ошибка: введите число");
            } catch (IllegalArgumentException e) {
                System.out.println("  Ошибка: " + e.getMessage());
            }
        }

        // Ввод координаты Y
        System.out.print("  coordinate y (int): ");
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int y = Integer.parseInt(input);
                vehicle.getCoordinates().setY(y);
                break;
            } catch (NumberFormatException e) {
                System.out.print("  Ошибка: введите целое число: ");
            }
        }

        // Ввод enginePower
        while (true) {
            System.out.print("  enginePower (Double > 0): ");
            String input = scanner.nextLine().trim();
            try {
                Double power = Double.parseDouble(input);
                InputValidator.validateEnginePower(power);
                vehicle.setEnginePower(power);
                break;
            } catch (NumberFormatException e) {
                System.out.println("  Ошибка: введите число");
            } catch (IllegalArgumentException e) {
                System.out.println("  Ошибка: " + e.getMessage());
            }
        }

        // Ввод capacity
        while (true) {
            System.out.print("  capacity (double > 0): ");
            String input = scanner.nextLine().trim();
            try {
                double capacity = Double.parseDouble(input);
                InputValidator.validateCapacity(capacity);
                vehicle.setCapacity(capacity);
                break;
            } catch (NumberFormatException e) {
                System.out.println("  Ошибка: введите число");
            } catch (IllegalArgumentException e) {
                System.out.println("  Ошибка: " + e.getMessage());
            }
        }

        // Ввод типа
        while (true) {
            System.out.print("  type (" + VehicleType.getTypes() + "): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                VehicleType type = InputValidator.validateAndParseVehicleType(input);
                vehicle.setType(type);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("  Ошибка: " + e.getMessage());
            }
        }

        // Ввод типа топлива (может быть null)
        System.out.print("  fuelType (" + FuelType.getTypes() + ", или пустая строка для null): ");
        String fuelInput = scanner.nextLine().trim();
        if (!fuelInput.isEmpty()) {
            try {
                FuelType fuelType = FuelType.valueOf(fuelInput.toUpperCase());
                vehicle.setFuelType(fuelType);
            } catch (IllegalArgumentException e) {
                System.out.println("  Неверный тип топлива, будет установлен null");
                vehicle.setFuelType(null);
            }
        } else {
            vehicle.setFuelType(null);
        }

        return vehicle;
    }
}