//Класс, представляющий ответ сервера на запрос пользователя.

package dao;

import Devices.Device;

import java.util.List;

public class UserResponse {
    private boolean success;
    private String message;
    private User user;
    private List<Device> devices; // Список устройств (если пользователь успешно вошёл)

    // Конструкторы, геттеры и сеттеры
    public UserResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UserResponse(boolean success, String message, List<Device> devices) {
        this.success = success;
        this.message = message;
        this.devices = devices;
    }

    public UserResponse(boolean success, String message, User user, List<Device> devices) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.devices = devices;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Device> getDevices() {
        return devices;
    }
}