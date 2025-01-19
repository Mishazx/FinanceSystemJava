package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.models.Data;
import ru.mishazx.financesystem.models.User;

import java.util.UUID;

public class DataService {
    public static boolean isValidUserId(UUID userId) {
        Data data = DataFileService.loadData();
        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public static boolean allowLogin() {
        Data data = DataFileService.loadData();
        return !data.getUsers().isEmpty();
    }
}
