package ru.mishazx.financesystem.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedPlainPassword = hashPassword(plainPassword);
        CustomIO.PrintDebug("[ИНФО] - Хэшированный пароль: " + hashedPassword);
        CustomIO.PrintDebug("[ИНФО] - Хэшированный введенный пароль: " + hashedPlainPassword);
        return hashedPlainPassword.equals(hashedPassword);
    }
}
