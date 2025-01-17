package ru.mishazx.financesystem.handlers;

import ru.mishazx.financesystem.services.AuthService;
import ru.mishazx.financesystem.services.DataFileService;
import ru.mishazx.financesystem.services.DataService;

import java.io.Console;
import java.util.Scanner;
import java.util.UUID;

public class MenuHandler {
    private final static Scanner scanner = new Scanner(System.in);
    private static boolean isAuthenticated;

    public static void printWelcome() {
        System.out.println("Добро пожаловать в лучшую систему учета финансов");
    }

    public static void printAuthMenu() {
        System.out.println("\n1. Войти в систему");
        System.out.println("2. Зарегистроваться");
        System.out.println("3. Выйти");
        System.out.print("Выберите действие: ");
    }

    public static void printUserMenu() {
        System.out.println("\n1. Просмотреть баланс");
        System.out.println("2. Добавить транзакцию");
        System.out.println("3. Выйти из системы");
        System.out.print("Выберите действие: ");
    }

    public static void handleMainMenu() {
        printWelcome();
        while (true) {
            if (!isAuthenticated) {
                printAuthMenu();
                String choice = readChoice();
                switch (choice.toUpperCase()) {
                    case "1":
                        UUID id = AuthService.login();
                        isAuthenticated = DataService.isValidUserId(id);
                        break;
                    case "2":
                        AuthService.register();
                        break;
                    case "3":
                        System.out.println("Выход из системы.");
                        return;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            } else {
                printUserMenu();
                String choice = readChoice();
            }
        }
    }

    public static String readChoice() {
        return scanner.nextLine();
    }

    public static String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static String readPassword(String prompt) {
        Console console = System.console();
        if (console == null) {
            System.out.print(prompt);
            return scanner.nextLine();
        } else {
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray);
        }
    }

    public static boolean askToNoRetry() {
        String response = scanner.nextLine().toLowerCase();
        return !response.equalsIgnoreCase("нет");
    }
}
