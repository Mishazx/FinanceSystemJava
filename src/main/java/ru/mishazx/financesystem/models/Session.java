package ru.mishazx.financesystem.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {
    private final UUID userId;
    private final String token;
    private final LocalDateTime expiresAt;

    public Session(UUID userId) {
        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.expiresAt = LocalDateTime.now().plusDays(30); // Session expires in 30 days
    }

    public UUID getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiresAt);
    }
} 