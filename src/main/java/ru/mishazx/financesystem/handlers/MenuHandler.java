package ru.mishazx.financesystem.handlers;

import ru.mishazx.financesystem.services.AuthService;
import ru.mishazx.financesystem.services.DataService;
import ru.mishazx.financesystem.utils.CustomIO;

import java.io.Console;
import java.util.Scanner;
import java.util.UUID;

public class MenuHandler {
    private final static Scanner scanner = new Scanner(System.in);
    private static boolean isAuthenticated;
    private static UUID user_id;

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
        System.out.println("4. Завершить программу");
        System.out.print("Выберите действие: ");
    }

    public static void handleMainMenu() {
        printWelcome();
        while (true) {
            CustomIO.PrintDebug("[ИНФО] - isAuth ? " + isAuthenticated);
            CustomIO.PrintDebug("[ИНФО] - user_id: " + user_id);
            if (!isAuthenticated) {
                printAuthMenu();
                String choice = readChoice();
                switch (choice.toUpperCase()) {
                    case "1":
                        user_id = AuthService.login();
                        isAuthenticated = DataService.isValidUserId(user_id);
                        break;
                    case "2":
                        AuthService.register();
                        break;
                    case "3":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            } else {
                printUserMenu();
                String choice = readChoice();
                switch (choice.toUpperCase()) {
                    case "1":
                        double balance = DataService.checkBalance(user_id);
                        CustomIO.PrintSuccess("Ваш баланс: " + balance);
                        break;
                    case "2":
                        DataService.addTransaction(user_id);
                        break;
                    case "3":
                        CustomIO.PrintSuccess("Выходим из системы! ");
                        user_id = null;
                        isAuthenticated = false;
                        break;
                    case "4":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
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
