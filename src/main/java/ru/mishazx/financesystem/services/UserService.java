package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.models.User;
import ru.mishazx.financesystem.utils.CustomIO;
import ru.mishazx.financesystem.utils.PasswordHasher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    private static List<User> users = new ArrayList<>();

    static {
        users = DataFileService.loadData();
        CustomIO.PrintDebug("[ИНФО] - Загружено " + users.size() + " пользователей.");
    }

    public static User createUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                CustomIO.PrintDebug("[ОШИБКА] - Пользователь с таким именем уже существует.");
                return null;
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        DataFileService.saveData(users);
        CustomIO.PrintDebug("[ИНФО] - Пользователь " + username + " успешно создан.");
        return newUser;
    }

    public static User readUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        CustomIO.PrintDebug("[ИНФО] - Пользователь не найден.");
        return null;
    }

    public static void updateUser(String username, String newPassword) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username)) {
                User updatedUser = new User(username, newPassword);
                users.set(i, updatedUser); // Обновляем пользователя
                DataFileService.saveData(users); // Сохраняем данные после обновления
                CustomIO.PrintDebug("[ИНФО] - Пользователь " + username + " успешно обновлён.");
                return;
            }
        }
        CustomIO.PrintDebug("[ОШИБКА] - Пользователь не найден.");
    }

    public static void deleteUser(String username) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username)) {
                users.remove(i); // Удаляем пользователя
                DataFileService.saveData(users); // Сохраняем данные после удаления
                CustomIO.PrintDebug("[ИНФО] - Пользователь " + username + " успешно удалён.");
                return;
            }
        }
        CustomIO.PrintDebug("[ОШИБКА] - Пользователь не найден.");
    }

    public static List<User> getAllUsers() {
        return users;
    }

    public static UUID authenticateUser(String username, String password) {
        for (User user : users) {
            boolean loginData = user.getUsername().equals(username);
            boolean passwordData = PasswordHasher.verifyPassword(password, user.getPassword());

            if (loginData && passwordData) {
                CustomIO.PrintDebug("[ИНФО] - Аутентификация успешна для пользователя " + username + ".");
                return user.getId();
            }
        }
        CustomIO.PrintDebug("[ОШИБКА] - Неверное имя пользователя или пароль.");
        return null;
    }
}
