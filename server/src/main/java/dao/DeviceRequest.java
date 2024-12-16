//Класс, представляющий запрос на управление устройством.

package dao;

public class DeviceRequest {
    private int userId;
    private String typeDevice;
    private String name; // Уникальное имя устройства
    private String statuses;
    private int temperature;
    private int waterLevel;
    private int currentTemperature;


    public DeviceRequest(int userId, String typeDevice, String name, String statuses, int temperature, int waterLevel, int currentTemperature) {
        this.userId = userId;
        this.typeDevice = typeDevice;
        this.name = name;
        this.statuses = statuses;
        this.temperature = temperature;
        this.waterLevel = waterLevel;
        this.currentTemperature = currentTemperature;
    }

    public int getUserId() {
        return userId;
    }

    public String getTypeDevice() {
        return typeDevice;
    }

    public String getName() {
        return name;
    }

    public String getStatuses() {
        return statuses;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTypeDevice(String typeDevice) {
        this.typeDevice = typeDevice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatuses(String statuses) {
        this.statuses = statuses;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(int currentTemperature) {
        this.currentTemperature = currentTemperature;
    }
}