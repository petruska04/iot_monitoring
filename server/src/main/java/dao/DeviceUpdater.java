package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class DeviceUpdater {

    private final int userId;
    private final Connection connection;
    private final Random random = new Random();

    public DeviceUpdater(int userId, Connection connection) {
        this.userId = userId;
        this.connection = connection;
    }

    public void startUpdating() {
        new Thread(() -> {
            while (true) {
                try {
                    updateDeviceStates();
                    Thread.sleep(5000); // 5 секунд
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateDeviceStates() {
        try {
            updateDeviceStatuses();
            updateWaterLevel();
            updateTemperature();
            updateCurrentTemperature();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDeviceStatuses() throws SQLException {
        String query = "UPDATE devices SET statuses = ? WHERE id_user = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, random.nextBoolean() ? "Включено" : "Выключено");
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    private void updateWaterLevel() throws SQLException {
        String query = "UPDATE devices SET water_level = ? WHERE id_user = ? AND type_device = 'Увлажнитель'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int waterLevel = random.nextInt(101);
            statement.setInt(1, waterLevel);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    private void updateTemperature() throws SQLException {
        String query = "UPDATE devices SET temperature = ? WHERE id_user = ? AND type_device = 'Кондиционер'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int temperature = random.nextInt(10) + 17;
            statement.setInt(1, temperature);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    private void updateCurrentTemperature() throws SQLException {
        String query = "UPDATE devices SET current_temperature = ? WHERE id_user = ? AND type_device = 'Термостат'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int currentTemperature = random.nextInt(6) - 10;
            statement.setInt(1, currentTemperature);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            int userId = 23;
            DeviceUpdater updater = new DeviceUpdater(userId, connection);
            updater.startUpdating();

            System.out.println("DeviceUpdater is running. Press Ctrl+C to stop.");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}