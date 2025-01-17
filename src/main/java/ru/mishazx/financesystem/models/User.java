package ru.mishazx.financesystem.models;

import ru.mishazx.financesystem.utils.PasswordHasher;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String password;
//    private final String salt;

    public User (String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
//        this.salt = PasswordHasher.generateSalt();
        this.password = password;
//        this.password = PasswordHasher.hashPassword(password);
    }

    public UUID getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

//    public String getSalt() {
//        return this.salt;
//    }

}
