/*
* Абстрактный класс для всех устройств
* */


package Devices;

public abstract class Device {
    protected int idUser;
    protected String typeDevice;
    protected String name; // Уникальное имя устройства
    protected String statuses;
    protected int temperature; // Для кондиционера
    protected int waterLevel; // Для увлажнителя
    protected int currentTemperature; // Для термостата

    public Device(int idUser, String typeDevice, String name, String statuses, int temperature, int waterLevel, int currentTemperature) {
        this.idUser = idUser;
        this.typeDevice = typeDevice;
        this.name = name;
        this.statuses = statuses;
        this.temperature = temperature;
        this.waterLevel = waterLevel;
        this.currentTemperature = currentTemperature;
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

    public abstract void performAction(String action);
}
