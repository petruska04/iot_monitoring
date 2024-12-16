//Запускает сервер.

import dao.DatabaseConnection;
import handler.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class ServerLauncher {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Соединение с базой данных установлено успешно!");
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных: " + e.getMessage());
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Новое соединение: " + clientSocket.getInetAddress());

                    RequestHandler requestHandler = new RequestHandler(clientSocket);
                    requestHandler.handleRequest();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
