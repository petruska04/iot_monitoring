//Класс, представляющий кондиционер.

package Devices;

public class AirConditioner extends Device {

    public AirConditioner(int idUser, String name, String statuses, int temperature) {
        super(idUser, "Кондиционер", name, statuses, temperature, 0, 0);
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
            case "setTemperature":
                this.statuses = "Setting temperature...";
                break;
            default:
                System.out.println("Неизвестное действие для кондиционера.");
        }
    }
}