//Класс, представляющий термостат.

package Devices;

public class Thermostat extends Device {

    public Thermostat(int idUser, String name, String statuses, int currentTemperature) {
        super(idUser, "Термостат", name, statuses, 0, 0, currentTemperature);
    }

    @Override
    public void performAction(String action) {
        switch (action) {
            case "Включено":
                this.statuses = "On";
                break;
            case "Выключено":
                this.statuses = "Off";
                break;
            case "setTargetTemperature":
                this.statuses = "Setting target temperature...";
                break;
            default:
                System.out.println("Неизвестное действие для термостата.");
        }
    }
}