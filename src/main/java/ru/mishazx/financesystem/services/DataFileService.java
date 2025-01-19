package ru.mishazx.financesystem.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ru.mishazx.financesystem.models.Data;
import ru.mishazx.financesystem.models.Session;
import ru.mishazx.financesystem.utils.CustomIO;
import ru.mishazx.financesystem.utils.LocalDateTimeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.UUID;

public class DataFileService {
    public static final String DATABASE_FILE = "main.json";
    private static final Type DATA_TYPE = new TypeToken<Data>(){}.getType();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    private static boolean checkData() {
        File db_file = new File(DATABASE_FILE);
        if (!db_file.exists()) {
            try {
                if (db_file.createNewFile()) {
                    try (Writer writer = new FileWriter(db_file)) {
                        GSON.toJson(new Data(), DATA_TYPE, writer);
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

    public static void saveData(Data data) {
        if (checkData()) {
            return;
        }

        try (Writer writer = new FileWriter(DATABASE_FILE)) {
            String json = GSON.toJson(data);
            CustomIO.PrintDebug("[ИНФО] - Сохраняем в json:" + json);
            GSON.toJson(data, DATA_TYPE, writer);
        } catch (IOException e) {
            CustomIO.PrintError("Ошибка при сохранении данных: " + e.getMessage(), true);
        }
    }

    public static Data loadData() {
        if (checkData()) {
            return new Data();
        }

        try (Reader reader = new FileReader(DATABASE_FILE)) {
            Data loadedData = GSON.fromJson(reader, DATA_TYPE);
            if (loadedData == null) {
                CustomIO.PrintError("[ИНФО] - Данные не загружены, файл может быть пустым или поврежденным.", true);
                return new Data();
            }
            return loadedData;
        } catch (IOException | JsonSyntaxException e) {
            CustomIO.PrintError("Ошибка при загрузке данных: " + e.getMessage(), true);
            return new Data();
        }
    }

    public static void saveSession(UUID userId) {
        Data data = loadData();
        Session session = new Session(userId);
        data.setCurrentSession(session);
        saveData(data);
        CustomIO.PrintDebug("[ИНФО] - Сессия сохранена");
    }

    public static Session loadSession() {
        Data data = loadData();
        Session session = data.getCurrentSession();
        
        if (session != null && session.isValid() && DataService.isValidUserId(session.getUserId())) {
            return session;
        } else {
            deleteSession();
            return null;
        }
    }

    public static void deleteSession() {
        Data data = loadData();
        data.setCurrentSession(null);
        saveData(data);
        CustomIO.PrintDebug("[ИНФО] - Сессия удалена");
    }
}
