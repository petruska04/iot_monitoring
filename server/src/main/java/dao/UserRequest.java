//Класс, представляющий запрос на регистрацию или
// аутентификацию пользователя

package dao;

public class UserRequest {
    private String login;
    private String password;
    private String city; // Добавляем поле для города

    public UserRequest(String login, String password, String city) {
        this.login = login;
        this.password = password;
        this.city = city;
    }

    public UserRequest(int userId, String city) {
        this.city = city;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getCity() {
        return city;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCity(String city) {
        this.city = city;
    }
}