package model;

import java.time.LocalDate;

/**
 * Класс, представляющий транспортное средство
 * Реализует Comparable для естественной сортировки по id
 */
public class Vehicle implements Comparable<Vehicle> {
    private int id; // Уникальный, генерируется автоматически
    private String name; // Не null, не пустая строка
    private Coordinates coordinates; // Не null
    private LocalDate creationDate; // Генерируется автоматически, не null
    private Double enginePower; // Не null, > 0
    private double capacity; // > 0
    private VehicleType type; // Не null
    private FuelType fuelType; // Может быть null

    public Vehicle() {
        this.creationDate = LocalDate.now();
    }
    
    /**
     * Конструктор для создания нового транспортного средства
     * Автоматически генерирует id и дату создания
     */
    public Vehicle(int id, String name, Coordinates coordinates, Double enginePower, 
                   double capacity, VehicleType type, FuelType fuelType) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.type = type;
        this.fuelType = fuelType;
    }
    
// Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше 0");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым");
        }
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Дата создания не может быть null");
        }
        this.creationDate = creationDate;
    }

    public Double getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(Double enginePower) {
        if (enginePower == null) {
            throw new IllegalArgumentException("Engine power не может быть null");
        }
        if (enginePower <= 0) {
            throw new IllegalArgumentException("Engine power должен быть больше 0");
        }
        this.enginePower = enginePower;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity должна быть больше 0");
        }
        this.capacity = capacity;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип не может быть null");
        }
        this.type = type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, name='%s', coordinates=%s, date=%s, " +
                "enginePower=%.2f, capacity=%.2f, type=%s, fuelType=%s}",
                id, name, coordinates, creationDate, enginePower, capacity, type, fuelType);
    }

    @Override
    public int compareTo(Vehicle o) {
        // Сортировка по умолчанию - по имени
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return id == vehicle.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}