package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.models.User;

import java.util.List;
import java.util.UUID;

public class DataService {
    public static boolean isValidUserId(UUID userId) {
        List<User> users = DataFileService.loadData();
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}
