package ru.mishazx.financesystem.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import ru.mishazx.financesystem.utils.CustomIO;

public class ConfigApp {
    private static Boolean DebugApp;

    static {
        Dotenv dotenv;
        try {
            dotenv = Dotenv.load();
            String debugValue = dotenv.get("DEBUG");
            DebugApp = Boolean.parseBoolean(debugValue);
            CustomIO.PrintDebug("[ИНФО] - Файл .env загружен! ");

        } catch (DotenvException e) {
            Dotenv.configure().ignoreIfMissing().load();
            CustomIO.PrintError("[ОШИБКА] - Ошибка загрузки .env файла: " + e.getMessage());
        }
    }

    public static boolean getDebug() {
        return DebugApp;
    }
}
