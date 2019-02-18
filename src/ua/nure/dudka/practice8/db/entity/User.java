package ua.nure.dudka.practice8.db.entity;

import java.util.Objects;

public class User {
    private long id;
    private String login;

    public User(String login) {
        this.login = login;
    }

    public User(long id, String login) {
        this.id = id;
        this.login = login;
    }

    public static User createUser(String login) {
        return new User(login);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return getLogin().equals(user.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin());
    }
}
