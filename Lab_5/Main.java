import collection.CollectionManager;
import commands.CommandExecutor;
import file.FileManager;

/**
 * Главный класс приложения.
 * Запускает программу и обрабатывает аргументы командной строки.
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Проверка наличия аргумента с именем файла
            if (args.length == 0) {
                System.out.println("Ошибка: не указано имя файла с данными");
                System.out.println("Использование: java Main <filename>");
                System.exit(1);
            }

            String filename = args[0];
            System.out.println("Загрузка данных из файла: " + filename);

            // Создание менеджера файлов и загрузка коллекции
            FileManager fileManager = new FileManager(filename);
            CollectionManager collectionManager = new CollectionManager(fileManager);

            // Запуск обработчика команд
            CommandExecutor executor = new CommandExecutor(collectionManager);
            executor.start();

        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            System.exit(1);
        }
    }
}