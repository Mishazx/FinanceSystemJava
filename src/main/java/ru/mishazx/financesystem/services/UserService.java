package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.models.Data;
import ru.mishazx.financesystem.models.User;
import ru.mishazx.financesystem.utils.CustomIO;
import ru.mishazx.financesystem.utils.PasswordHasher;

import java.util.List;
import java.util.UUID;

public class UserService {
    private static Data data = DataFileService.loadData(); // Загружаем данные
    private static User currentUser;

    static {
        CustomIO.PrintDebug("[ИНФО] - Загружено " + data.getUsers().size() + " пользователей.");
    }

    public static User createUser(String username, String password) {
        for (User user : data.getUsers()) {
            if (user.getUsername().equals(username)) {
                CustomIO.PrintDebug("[ОШИБКА] - Пользователь с таким именем уже существует.");
                return null;
            }
        }
        User newUser = new User(username, password);
        data.getUsers().add(newUser); // Добавляем нового пользователя в список
        DataFileService.saveData(data); // Сохраняем обновленный объект Data
        CustomIO.PrintDebug("[ИНФО] - Пользователь " + username + " успешно создан.");
        return newUser;
    }

    public static User readUser(String username) {
        for (User user : data.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        CustomIO.PrintDebug("[ИНФО] - Пользователь не найден.");
        return null;
    }

    public static User getUserByUsername(String username) {
        for (User user : data.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        CustomIO.PrintDebug("[ИНФО] - Пользователь не найден.");
        return null;
    }

    public static User getUserById(UUID id) {
        for (User user : data.getUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        CustomIO.PrintDebug("[ИНФО] - Пользователь с ID " + id + " не найден.");
        return null;
    }

    public static List<User> getAllUsers() {
        return data.getUsers(); // Возвращаем список пользователей из объекта Data
    }

    public static UUID authenticateUser(String username, String password) {
        for (User user : data.getUsers()) {
            boolean loginData = user.getUsername().equals(username);
            boolean passwordData = PasswordHasher.verifyPassword(password, user.getPassword());

            if (loginData && passwordData) {
                CustomIO.PrintDebug("[ИНФО] - Аутентификация успешна для пользователя " + username + ".");
                currentUser = user;
                return user.getId();
            }
        }
        CustomIO.PrintDebug("[ОШИБКА] - Неверное имя пользователя или пароль.");
        return null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
