package ru.mishazx.financesystem.utils;

import java.util.UUID;

public class GeneratorUUID {
    public String generateUserID() {
        return UUID.randomUUID().toString().substring(0, 10);
    }
}
