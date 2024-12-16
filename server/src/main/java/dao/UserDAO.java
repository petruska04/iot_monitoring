//Класс для работы с данными пользователей в базе данных.

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static User getUserByLogin(String login) {
        String query = "SELECT id, login, password, city FROM users WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                String city = resultSet.getString("city");
                return new User(id, login, password, city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addUser(String login, String password, String city) {
        String query = "INSERT INTO users (login, password, city) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, city);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e instanceof java.sql.SQLIntegrityConstraintViolationException) {
                System.out.println("Ошибка: Пользователь с логином " + login + " уже существует.");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }
}