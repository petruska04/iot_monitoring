//Класс, представляющий пользователя.

package dao;

public class User {
    private int id;
    private String login;
    private String password;
    private String city;

    public User(int id, String login, String password, String city) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}