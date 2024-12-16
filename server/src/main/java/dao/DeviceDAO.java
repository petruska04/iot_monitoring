/*
* Класс для работы с данными устройств в базе данных.
* */


package dao;

import Devices.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {

    public static List<Device> getDevicesByUserId(int userId) {
        List<Device> devices = new ArrayList<>();
        String query = "SELECT id_user, type_device, name, statuses, temperature, water_level, current_temperature FROM devices WHERE id_user = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String typeDevice = resultSet.getString("type_device");
                String name = resultSet.getString("name");
                String statuses = resultSet.getString("statuses");
                int temperature = resultSet.getInt("temperature");
                int waterLevel = resultSet.getInt("water_level");
                int currentTemperature = resultSet.getInt("current_temperature");
                Device device = createDevice(userId, typeDevice, name, statuses, temperature, waterLevel, currentTemperature);
                devices.add(device);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    private static Device createDevice(int userId, String typeDevice, String name, String statuses, int temperature, int waterLevel, int currentTemperature) {
        switch (typeDevice) {
            case "Лампочка":
                return new LightBulb(userId, name, statuses);
            case "Увлажнитель":
                return new Humidifier(userId, name, statuses, waterLevel);
            case "Кондиционер":
                return new AirConditioner(userId, name, statuses, temperature);
            case "Термостат":
                return new Thermostat(userId, name, statuses, currentTemperature);
            default:
                throw new IllegalArgumentException("Неизвестный тип устройства: " + typeDevice);
        }
    }

    public static boolean addDevice(int userId, String typeDevice, String name, String statuses) {
        String query = "INSERT INTO devices (id_user, type_device, name, statuses) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, typeDevice);
            statement.setString(3, name);
            statement.setString(4, statuses);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateDeviceStatus(int userId, String typeDevice, String name, String newStatus, int temperature, int waterLevel, int currentTemperature) {
        String query = "UPDATE devices SET statuses = ?, temperature = ?, water_level = ?, current_temperature = ? WHERE id_user = ? AND type_device = ? AND name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newStatus);
            statement.setInt(2, temperature);
            statement.setInt(3, waterLevel);
            statement.setInt(4, currentTemperature);
            statement.setInt(5, userId);
            statement.setString(6, typeDevice);
            statement.setString(7, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteDevice(int userId, String typeDevice, String name) {
        String query = "DELETE FROM devices WHERE id_user = ? AND type_device = ? AND name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, typeDevice);
            statement.setString(3, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}