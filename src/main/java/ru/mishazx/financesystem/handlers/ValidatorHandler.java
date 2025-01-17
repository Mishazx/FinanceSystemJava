package ru.mishazx.financesystem.handlers;

import ru.mishazx.financesystem.exceptions.InvalidLoginException;
import ru.mishazx.financesystem.exceptions.InvalidPasswordException;

import java.util.regex.Pattern;

public class ValidatorHandler {
    private static final Pattern latinOnlyPattern = Pattern.compile("^[a-zA-Z0-9@#$%^&+=!]*$");
    private static final Pattern loginPattern = Pattern.compile("^[a-zA-Z0-9._-]*$");

    public static String validLogin(String login) throws InvalidLoginException {
        if (!isLoginValid(login)) {
            throw new InvalidLoginException("Логин должен содержать только английские буквы и быть не короче 3 символов");
        }
        return login;
    }

    private static boolean isLoginValid(String login) throws InvalidLoginException {
        StringBuilder errorMessage = new StringBuilder("Логин не соотвествует следующим требованиям:");

        if (login == null || login.isEmpty()) {
            errorMessage.append("\n- Логин не может быть пустым");
        } else {
            if (login.length() < 3) {
                errorMessage.append("\n- Логин должен быть длиннее 3 символов");
            }
            if (!loginPattern.matcher(login).matches()) {
                errorMessage.append("\n- Логин должен содержать только латинские буквы, цифры и символы: ._- ");
            }
        }
        if (errorMessage.length() > 44) {
            throw new InvalidLoginException(errorMessage.toString());
        }

        return true;
    }

    public static String validPassword(String password) throws InvalidPasswordException {
        if (!isPasswordValid(password)) {
            throw new InvalidPasswordException("Пароль не удовлетворяет требованиям безопасности!");
        }
        return password;
    }

    private static boolean isPasswordValid(String password) throws InvalidPasswordException {
        StringBuilder errorMessage = new StringBuilder("Пароль не соответствует следующим требованиям:");

        if (password == null || password.isEmpty()) {
            errorMessage.append("\n- Пароль не может быть пустым");
        } else {
            if (password.length() < 8) {
                errorMessage.append("\n- Пароль должен быть длиннее 8 символов");
            }
            if (!password.matches(".*[0-9].*")) {
                errorMessage.append("\n- Пароль должен содержать хотя бы одну цифру");
            }
            if (!password.matches(".*[a-z].*")) {
                errorMessage.append("\n- Пароль должен содержать хотя бы одну английскую строчную букву");
            }
            if (!password.matches(".*[A-Z].*")) {
                errorMessage.append("\n- Пароль должен содержать хотя бы одну английскую заглавную букву");
            }
            if (!password.matches(".*[@#$%^&+=!].*")) {
                errorMessage.append("\n- Пароль должен содержать хотя бы один специальный символ из набора @#$%^&+=!");
            }
            if (password.contains(" ")) {
                errorMessage.append("\n- Пароль не должен содержать пробелы");
            }
            if (!latinOnlyPattern.matcher(password).matches()) {
                errorMessage.append("\n- Пароль должен содержать только латинские буквы, цифры и специальные символы");
            }
        }

        if (errorMessage.length() > 46) {
            throw new InvalidPasswordException(errorMessage.toString());
        }

        return true;
    }
}