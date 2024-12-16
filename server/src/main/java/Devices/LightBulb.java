//Класс, представляющий лампочку.

package Devices;

public class LightBulb extends Device {

    public LightBulb(int idUser, String name, String statuses) {
        super(idUser, "Лампочка", name, statuses, 0, 0, 0); // Передаем 0 для неиспользуемых полей
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
            default:
                System.out.println("Неизвестное действие для лампочки.");
        }
    }
}