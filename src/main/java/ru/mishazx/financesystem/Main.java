package ru.mishazx.financesystem;

//import ru.mishazx.financesystem.utils.CustomIO;

import io.github.cdimascio.dotenv.Dotenv;
import ru.mishazx.financesystem.config.ConfigApp;
import ru.mishazx.financesystem.handlers.MenuHandler;
import ru.mishazx.financesystem.services.AuthService;

import java.util.UUID;

public class Main {

    private static UUID user_id;

    public UUID getUser_id() {
        return user_id;
    }

    public static void setUser_id(UUID user_id) {

    }

    private static void initUser(UUID id) {
        setUser_id(id);
    }

    public static void runProgram() {
//        ConfigApp.getDotenv();
//        Dotenv dotenv = cfg.getDotenv();
//        System.out.println(dotenv);
//        AuthService.login();
//        initUser(UUID.randomUUID());
        MenuHandler.handleMainMenu();
    }



    public static void main(String[] args) {
        runProgram();
    }
}

//        CustomIO.Print();

//        AuthService.register();
//        initUser(UUID.randomUUID());