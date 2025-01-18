package ru.mishazx.financesystem.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ru.mishazx.financesystem.models.Data; // Импортируйте новый класс Data
import ru.mishazx.financesystem.utils.CustomIO;
import ru.mishazx.financesystem.utils.LocalDateTimeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

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
                        GSON.toJson(new Data(), DATA_TYPE, writer); // Сохраняем пустой объект Data
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
            return new Data(); // Возвращаем новый объект Data
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
}
