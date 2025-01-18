package ru.mishazx.financesystem.models;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String password;
    private final Wallet wallet;

    public User (String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.wallet = new Wallet(this.id);
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

    public Wallet getWallet() {
        return wallet;
    }
}
