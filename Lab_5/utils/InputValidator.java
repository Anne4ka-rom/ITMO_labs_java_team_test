package utils;

import model.VehicleType;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Валидация пользовательского ввода.
 * Проверяет все поля на соответствие требованиям.
 */
public class InputValidator {

    /**
     * Проверяет имя
     */
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
    }

    /**
     * Проверяет enginePower
     */
    public static void validateEnginePower(Double enginePower) {
        if (enginePower == null) {
            throw new IllegalArgumentException("enginePower не может быть null");
        }
        if (enginePower <= 0) {
            throw new IllegalArgumentException("enginePower должен быть больше 0");
        }
    }

    /**
     * Проверяет capacity
     */
    public static void validateCapacity(double capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity должна быть больше 0");
        }
    }

    /**
     * Проверяет координату X
     */
    public static void validateCoordinateX(Double x) {
        if (x == null) {
            throw new IllegalArgumentException("X не может быть null");
        }
        if (x > 636) {
            throw new IllegalArgumentException("X должен быть <= 636");
        }
    }

    /**
     * Проверяет и преобразует строку в VehicleType
     */
    public static VehicleType validateAndParseVehicleType(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Тип не может быть пустым");
        }

        try {
            return VehicleType.valueOf(input.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            String available = Arrays.stream(VehicleType.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Тип должен быть одним из: " + available);
        }
    }

    /**
     * Проверяет ID
     */
    public static void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше 0");
        }
    }
}