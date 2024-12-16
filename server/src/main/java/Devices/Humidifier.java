//Класс, представляющий увлажнитель.

package Devices;

public class Humidifier extends Device {

    public Humidifier(int idUser, String name, String statuses, int waterLevel) {
        super(idUser, "Увлажнитель", name, statuses, 0, waterLevel, 0);
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
            case "setHumidity":
                this.statuses = "Setting humidity...";
                break;
            default:
                System.out.println("Неизвестное действие для увлажнителя.");
        }
    }
}