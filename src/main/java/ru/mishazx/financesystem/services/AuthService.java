package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.exceptions.InvalidLoginException;
import ru.mishazx.financesystem.exceptions.InvalidPasswordException;
import ru.mishazx.financesystem.handlers.MenuHandler;
import ru.mishazx.financesystem.handlers.ValidatorHandler;
import ru.mishazx.financesystem.models.User;
import ru.mishazx.financesystem.utils.CustomIO;
import ru.mishazx.financesystem.utils.PasswordHasher;

import java.util.UUID;

public class AuthService {
    private static UUID user_id;

    public static UUID register() {
        String login = null;

        while (true) {
            try {
                login = getLoginWithRetry(login);
                String hashedPassword = getHashedPassword();
                User newUser = UserService.createUser(login, hashedPassword);
                if (newUser != null) {
                    user_id = newUser.getId();
                    CustomIO.PrintSuccess("Пользователь успешно зарегистрирован!");
                    return user_id;
                }
            } catch (InvalidLoginException | InvalidPasswordException e) {
                CustomIO.PrintError(e.getMessage());
                if (!askToRetry(e instanceof InvalidLoginException ? "установить логин" : "установить пароль")) {
                    return null;
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static UUID login() {
        String login = MenuHandler.readInput("Введите логин: ");
        boolean isLogin = isLoginTaken(login);
        if (isLogin) {
            String password = MenuHandler.readPassword("Введите пароль: ");
            user_id = UserService.authenticateUser(login, password);
            return user_id;

        } else {
            CustomIO.PrintError("Такого пользователя не существует!");
            return null;
        }
    }

    private static String getLoginWithRetry(String currentLogin) throws InvalidLoginException {
        while (currentLogin == null || isLoginTaken(currentLogin)) {
            if (currentLogin != null) {
                CustomIO.PrintError("Данный логин занят!");
            }
            currentLogin = getValidLogin();
        }
        return currentLogin;
    }

    private static boolean askToRetry(String action) {
        CustomIO.PrintInfo("Хотите попробовать снова " + action + "? (да/нет)");
        return MenuHandler.askToNoRetry();
    }

    private static boolean isLoginTaken(String login) {
        return UserService.readUser(login) != null;
    }

    private static String getValidLogin() throws InvalidLoginException {
        String userInput = MenuHandler.readInput("Придумайте логин: ");
        return ValidatorHandler.validLogin(userInput);
    }

    private static String getHashedPassword() throws InvalidPasswordException {
        String userInput = MenuHandler.readPassword("Придумайте пароль: ");
        String validPassword = ValidatorHandler.validPassword(userInput);

        String hashedPassword = PasswordHasher.hashPassword(validPassword);

        CustomIO.PrintDebug("[ИНФО] - Хэшированный пароль: " + hashedPassword);

        return hashedPassword;
    }
}
