package file;

import model.*;
import exceptions.InvalidDataException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Управляет чтением и записью XML файла.
 * Использует FileReader и FileOutputStream согласно заданию.
 */
public class FileManager {
    private final String filename;

    public FileManager(String filename) {
        this.filename = filename;
    }

    /**
     * Сохраняет коллекцию в XML файл
     */
    public void saveCollection(Stack<Vehicle> collection) throws IOException {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<vehicles>\n");

        for (Vehicle v : collection) {
            xml.append("  <vehicle>\n");
            xml.append("    <id>").append(v.getId()).append("</id>\n");
            xml.append("    <name>").append(escapeXml(v.getName())).append("</name>\n");
            xml.append("    <coordinates>\n");
            xml.append("      <x>").append(v.getCoordinates().getX()).append("</x>\n");
            xml.append("      <y>").append(v.getCoordinates().getY()).append("</y>\n");
            xml.append("    </coordinates>\n");
            xml.append("    <creationDate>").append(v.getCreationDate()).append("</creationDate>\n");
            xml.append("    <enginePower>").append(v.getEnginePower()).append("</enginePower>\n");
            xml.append("    <capacity>").append(v.getCapacity()).append("</capacity>\n");
            xml.append("    <type>").append(v.getType()).append("</type>\n");
            xml.append("    <fuelType>").append(v.getFuelType()).append("</fuelType>\n");
            xml.append("  </vehicle>\n");
        }

        xml.append("</vehicles>");

        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(xml.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Загружает коллекцию из XML файла
     */
    public Stack<Vehicle> loadCollection() throws IOException, InvalidDataException {
        Stack<Vehicle> collection = new Stack<>();
        StringBuilder content = new StringBuilder();

        try (FileReader reader = new FileReader(filename)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);
            }
        }

        String xml = content.toString();
        parseVehicles(xml, collection);
        return collection;
    }

    /**
     * Парсит XML и заполняет коллекцию
     */
    private void parseVehicles(String xml, Stack<Vehicle> collection) throws InvalidDataException {
        Pattern vehiclePattern = Pattern.compile("<vehicle>(.*?)</vehicle>", Pattern.DOTALL);
        Matcher vehicleMatcher = vehiclePattern.matcher(xml);

        while (vehicleMatcher.find()) {
            String vehicleXml = vehicleMatcher.group(1);
            try {
                Vehicle vehicle = parseVehicle(vehicleXml);
                collection.push(vehicle);
            } catch (Exception e) {
                throw new InvalidDataException("Ошибка парсинга vehicle: " + e.getMessage());
            }
        }
    }

    /**
     * Парсит один элемент Vehicle
     */
    private Vehicle parseVehicle(String xml) throws InvalidDataException {
        Vehicle vehicle = new Vehicle();

        // ID
        String idStr = extractTag(xml, "id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                if (id <= 0) throw new InvalidDataException("ID должен быть > 0");
                vehicle.setId(id);
            } catch (NumberFormatException e) {
                throw new InvalidDataException("Неверный формат ID");
            }
        }

        // Name
        String name = extractTag(xml, "name");
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Имя не может быть пустым");
        }
        vehicle.setName(name);

        // Coordinates
        String coordsXml = extractTag(xml, "coordinates");
        if (coordsXml == null) {
            throw new InvalidDataException("Отсутствуют координаты");
        }
        vehicle.setCoordinates(parseCoordinates(coordsXml));

        // Creation Date
        String dateStr = extractTag(xml, "creationDate");
        if (dateStr != null) {
            vehicle.setCreationDate(LocalDate.parse(dateStr));
        }

        // Engine Power
        String powerStr = extractTag(xml, "enginePower");
        if (powerStr != null) {
            try {
                Double power = Double.parseDouble(powerStr);
                if (power <= 0) throw new InvalidDataException("Engine power должен быть > 0");
                vehicle.setEnginePower(power);
            } catch (NumberFormatException e) {
                throw new InvalidDataException("Неверный формат enginePower");
            }
        }

        // Capacity
        String capacityStr = extractTag(xml, "capacity");
        if (capacityStr != null) {
            try {
                double capacity = Double.parseDouble(capacityStr);
                if (capacity <= 0) throw new InvalidDataException("Capacity должна быть > 0");
                vehicle.setCapacity(capacity);
            } catch (NumberFormatException e) {
                throw new InvalidDataException("Неверный формат capacity");
            }
        }

        // Type
        String typeStr = extractTag(xml, "type");
        if (typeStr != null) {
            try {
                vehicle.setType(VehicleType.valueOf(typeStr));
            } catch (IllegalArgumentException e) {
                throw new InvalidDataException("Неверный тип vehicle");
            }
        }

        // Fuel Type (может быть null)
        String fuelStr = extractTag(xml, "fuelType");
        if (fuelStr != null && !fuelStr.equals("null")) {
            try {
                vehicle.setFuelType(FuelType.valueOf(fuelStr));
            } catch (IllegalArgumentException e) {
                vehicle.setFuelType(null);
            }
        }

        return vehicle;
    }

    /**
     * Парсит координаты
     */
    private Coordinates parseCoordinates(String xml) throws InvalidDataException {
        Coordinates coords = new Coordinates();

        String xStr = extractTag(xml, "x");
        if (xStr != null) {
            try {
                Double x = Double.parseDouble(xStr);
                if (x > 636) throw new InvalidDataException("X должен быть <= 636");
                coords.setX(x);
            } catch (NumberFormatException e) {
                throw new InvalidDataException("Неверный формат X");
            }
        }

        String yStr = extractTag(xml, "y");
        if (yStr != null) {
            try {
                coords.setY(Integer.parseInt(yStr));
            } catch (NumberFormatException e) {
                throw new InvalidDataException("Неверный формат Y");
            }
        }

        return coords;
    }

    /**
     * Извлекает содержимое XML тега
     */
    private String extractTag(String xml, String tag) {
        Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xml);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    /**
     * Экранирует специальные символы XML
     */
    private String escapeXml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}