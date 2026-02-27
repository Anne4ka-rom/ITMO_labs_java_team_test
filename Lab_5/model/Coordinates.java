package model;

// Класс координат транспортного средства.

public class Coordinates {
    private Double x; // Максимальное значение поля: 636, Поле не может быть null
    private int y;

    public Coordinates() {}

    public Coordinates(Double x, int y) {
        setX(x);
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        if (x == null) {
            throw new IllegalArgumentException("X не может быть null");
        }
        if (x > 636) {
            throw new IllegalArgumentException("X должен быть <= 636");
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}