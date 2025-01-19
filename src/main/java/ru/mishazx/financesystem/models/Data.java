package ru.mishazx.financesystem.models;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private Session currentSession;
    private final List<User> users;


    public Data() {
        this.currentSession = null;
        this.users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session session) {
        this.currentSession = session;
    }
}
