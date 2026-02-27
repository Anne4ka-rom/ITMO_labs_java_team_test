package model;

public enum FuelType {
    GASOLINE,
    KEROSENE,
    ALCOHOL,
    MANPOWER;

    public static String getTypes() {
            StringBuilder sb = new StringBuilder();
            for (FuelType type : values()) {
                sb.append(type.name()).append(", ");
            }
            return sb.substring(0, sb.length() - 2);
        }
}