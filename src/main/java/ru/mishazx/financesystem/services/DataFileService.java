package ru.mishazx.financesystem.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ru.mishazx.financesystem.models.User;
import ru.mishazx.financesystem.utils.CustomIO;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataFileService {
    public static final String DATABASE_FILE = "main.json";
    private static final Type USER_LIST_TYPE = new TypeToken<List<User>>(){}.getType();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static boolean checkData() {
        File db_file = new File(DATABASE_FILE);
        if (!db_file.exists()) {
            try {
                if (db_file.createNewFile()) {
                    try (Writer writer = new FileWriter(db_file)) {
                        GSON.toJson(new ArrayList<User>(), USER_LIST_TYPE, writer); // Сохраняем пустой массив
                    }
                    CustomIO.PrintSuccess("[ИНФО] - Создан новый файл базы данных: " + DATABASE_FILE);
                    return false;
                }
                return true;
            } catch (IOException e) {
                CustomIO.PrintError("[ОШИБКА] - Произошла ошибка при создании файла базы данных: " + e.getMessage(), true);
                return true;
            }
        }
        return false;
    }

    public static void saveData(List<User> users) {
        if (checkData()) {
            return;
        }

        try (Writer writer = new FileWriter(DATABASE_FILE)) {
            GSON.toJson(users, USER_LIST_TYPE, writer); // Сохраняем список пользователей как массив
        } catch (IOException e) {
            CustomIO.PrintError("Ошибка при сохранении данных: " + e.getMessage(), true);
        }
    }

    public static List<User> loadData() {
        if (checkData()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(DATABASE_FILE)) {
            List<User> loadedUsers = GSON.fromJson(reader, USER_LIST_TYPE);
            if (loadedUsers == null) {
                CustomIO.PrintError("[ИНФО] - Данные пользователей не загружены, файл может быть пустым или поврежденным.", true);
                return new ArrayList<>();
            }
            return loadedUsers;
        } catch (IOException | JsonSyntaxException e) {
            CustomIO.PrintError("Ошибка при загрузке данных: " + e.getMessage(), true);
            return new ArrayList<>();
        }
    }
}
