package ru.mishazx.financesystem.models;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private final List<User> users;
//    private final User currentUser;

    // Конструкторы, геттеры и сеттеры
    public Data() {
//        this.currentUser = currentUser;
        this.users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

//    public void setUsers(List<User> users) {
//        this.users = users;
//    }
}
