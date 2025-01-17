package ru.mishazx.financesystem.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import ru.mishazx.financesystem.utils.CustomIO;

public class ConfigApp {
    private static Dotenv dotenv;
    private static Boolean DebugApp;

    static {
        try {
            dotenv = Dotenv.load();
            String debugValue = dotenv.get("DEBUG");
            DebugApp = Boolean.parseBoolean(debugValue);

        } catch (DotenvException e) {
            CustomIO.PrintError("[ОШИБКА] - Ошибка загрузки .env файла: " + e.getMessage());
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        }
    }

    public static Dotenv getDotenv() {
        return dotenv;
    }

    public static boolean getDebug() {
        return DebugApp;
    }
}
