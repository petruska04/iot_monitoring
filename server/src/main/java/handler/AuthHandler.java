package handler;

import Devices.Device;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dao.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AuthHandler {

    private final OutputStream output;
    private final Gson gson = new Gson();

    public AuthHandler(OutputStream output) {
        this.output = output;
    }

    public void handleAuthRequest(String path, String body) throws IOException {
        try {
            if (path.equals("/auth/login")) {
                handleLoginRequest(body);
            } else if (path.equals("/auth/register")) {
                handleRegisterRequest(body);
            } else if (path.equals("/auth/addDevice")) {
                handleAddDeviceRequest(body);
            } else if (path.equals("/auth/updateDeviceStatus")) {
                handleUpdateDeviceStatusRequest(body);
            } else if (path.equals("/auth/getDeviceData")) {
                handleGetDeviceData(body);
            } else if (path.equals("/auth/deleteDevice")) {
                handleDeleteDeviceRequest(body);
            } else {
                sendErrorResponse("404 Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse("500 Internal Server Error");
        }
    }

    private void handleLoginRequest(String body) throws IOException {
        try {
            String cleanedBody = body.trim();
            UserRequest request = gson.fromJson(cleanedBody, UserRequest.class);
            System.out.println("Получен запрос на вход: login=" + request.getLogin() + ", password=" + request.getPassword());

            User user = UserDAO.getUserByLogin(request.getLogin());
            if (user != null) {
                String hashedPassword = hashPassword(request.getPassword());
                if (hashedPassword == null) {
                    sendErrorResponse("500 Internal Server Error: Ошибка хеширования пароля");
                    return;
                }

                if (user.getPassword().equals(hashedPassword)) {
                    List<Device> devices = DeviceDAO.getDevicesByUserId(user.getId());
                    UserResponse response = new UserResponse(true, "Вход выполнен успешно!", user, devices);
                    sendResponse(gson.toJson(response), "application/json");
                } else {
                    UserResponse response = new UserResponse(false, "Неверный логин или пароль!");
                    sendResponse(gson.toJson(response), "application/json");
                }
            } else {
                UserResponse response = new UserResponse(false, "Неверный логин или пароль!");
                sendResponse(gson.toJson(response), "application/json");
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            sendErrorResponse("400 Bad Request: Некорректный JSON");
        }
    }

    private void handleRegisterRequest(String body) throws IOException {
        try {
            String cleanedBody = body.trim();
            UserRequest request = gson.fromJson(cleanedBody, UserRequest.class);
            System.out.println("Получен запрос на регистрацию: login=" + request.getLogin() + ", password=" + request.getPassword() + ", city=" + request.getCity());

            String hashedPassword = hashPassword(request.getPassword());
            if (hashedPassword == null) {
                sendErrorResponse("500 Internal Server Error: Ошибка хеширования пароля");
                return;
            }

            boolean success = UserDAO.addUser(request.getLogin(), hashedPassword, request.getCity());
            if (success) {
                UserResponse response = new UserResponse(true, "Регистрация выполнена успешно!");
                sendResponse(gson.toJson(response), "application/json");
            } else {
                UserResponse response = new UserResponse(false, "Пользователь с таким логином уже существует!");
                sendResponse(gson.toJson(response), "application/json");
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            sendErrorResponse("400 Bad Request: Некорректный JSON");
        }
    }

    private void handleAddDeviceRequest(String body) throws IOException {
        try {
            String cleanedBody = body.trim();
            DeviceRequest request = gson.fromJson(cleanedBody, DeviceRequest.class);
            System.out.println("Получен запрос на добавление устройства: userId=" + request.getUserId() + ", typeDevice=" + request.getTypeDevice() + ", name=" + request.getName() + ", statuses=" + request.getStatuses());

            boolean success = DeviceDAO.addDevice(request.getUserId(), request.getTypeDevice(), request.getName(), request.getStatuses());
            UserResponse response = new UserResponse(success, success ? "Устройство добавлено успешно!" : "Ошибка при добавлении устройства!");
            sendResponse(gson.toJson(response), "application/json");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            sendErrorResponse("400 Bad Request: Некорректный JSON");
        }
    }

    private void handleUpdateDeviceStatusRequest(String body) throws IOException {
        try {
            String cleanedBody = body.trim();
            DeviceRequest request = gson.fromJson(cleanedBody, DeviceRequest.class);
            System.out.println("Получен запрос на изменение статуса устройства: userId=" + request.getUserId() + ", typeDevice=" + request.getTypeDevice() + ", name=" + request.getName() + ", newStatus=" + request.getStatuses());

            boolean success = DeviceDAO.updateDeviceStatus(
                    request.getUserId(),
                    request.getTypeDevice(),
                    request.getName(),
                    request.getStatuses(),
                    request.getTemperature(),
                    request.getWaterLevel(),
                    request.getCurrentTemperature()
            );
            UserResponse response = new UserResponse(success, success ? "Статус устройства обновлен успешно!" : "Ошибка при обновлении статуса устройства!");
            sendResponse(gson.toJson(response), "application/json");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            sendErrorResponse("400 Bad Request: Некорректный JSON");
        }
    }

    private void handleDeleteDeviceRequest(String body) throws IOException {
        try {
            String cleanedBody = body.trim();
            DeviceRequest request = gson.fromJson(cleanedBody, DeviceRequest.class);
            System.out.println("Получен запрос на удаление устройства: userId=" + request.getUserId() + ", typeDevice=" + request.getTypeDevice() + ", name=" + request.getName());

            boolean success = DeviceDAO.deleteDevice(request.getUserId(), request.getTypeDevice(), request.getName());
            UserResponse response = new UserResponse(success, success ? "Устройство удалено успешно!" : "Ошибка при удалении устройства!");
            sendResponse(gson.toJson(response), "application/json");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            sendErrorResponse("400 Bad Request: Некорректный JSON");
        }
    }

    private void handleGetDeviceData(String body) throws IOException {
        try {
            String cleanedBody = body.trim();
            DeviceRequest request = gson.fromJson(cleanedBody, DeviceRequest.class);
            System.out.println("Получен запрос на получение данных об устройствах: userId=" + request.getUserId());

            List<Device> devices = DeviceDAO.getDevicesByUserId(request.getUserId());

            UserResponse response = new UserResponse(true, "Данные об устройствах успешно получены!", devices);
            sendResponse(gson.toJson(response), "application/json");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            sendErrorResponse("400 Bad Request: Некорректный JSON");
        }
    }

    private void sendResponse(String json, String contentType) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n";
        response += "Content-Type: " + contentType + "; charset=UTF-8\r\n";
        response += "Content-Length: " + json.getBytes(StandardCharsets.UTF_8).length + "\r\n";
        response += "\r\n";
        response += json;

        output.write(response.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    private void sendErrorResponse(String status) throws IOException {
        String response = "HTTP/1.1 " + status + "\r\n";
        response += "Content-Type: text/html; charset=UTF-8\r\n";
        response += "\r\n";
        response += "<h1>" + status + "</h1>";

        output.write(response.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    // Метод для хеширования пароля с использованием SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}