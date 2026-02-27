package model;

public enum VehicleType {
    CAR,
    SUBMARINE,
    BICYCLE,
    HOVERBOARD;

    public static String getTypes() {
        StringBuilder sb = new StringBuilder();
        for (VehicleType type : values()) {
            sb.append(type.name()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}