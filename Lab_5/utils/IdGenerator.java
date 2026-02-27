package utils;

import model.Vehicle;

import java.util.Collection;

/**
 * Генератор уникальных ID для объектов Vehicle.
 */
public class IdGenerator {
    private static int lastId = 0;

    /**
     * Генерирует новый уникальный ID
     */
    public static int generateId(Collection<Vehicle> collection) {
        return collection.stream()
                .mapToInt(Vehicle::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Альтернативный генератор с сохранением последнего ID
     */
    public static int generateId() {
        return ++lastId;
    }

    /**
     * Обновляет lastId на основе существующей коллекции
     */
    public static void updateLastId(Collection<Vehicle> collection) {
        lastId = collection.stream()
                .mapToInt(Vehicle::getId)
                .max()
                .orElse(0);
    }

    /**
     * Проверяет уникальность ID
     */
    public static boolean isIdUnique(int id, Collection<Vehicle> collection) {
        return collection.stream().noneMatch(v -> v.getId() == id);
    }
}