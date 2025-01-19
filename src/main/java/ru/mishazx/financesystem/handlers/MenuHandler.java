package ru.mishazx.financesystem.handlers;

import ru.mishazx.financesystem.models.Transaction;
import ru.mishazx.financesystem.services.AuthService;
import ru.mishazx.financesystem.services.DataService;
import ru.mishazx.financesystem.services.DataFileService;
import ru.mishazx.financesystem.services.WalletService;
import ru.mishazx.financesystem.utils.CustomIO;

import java.io.Console;
import java.util.List;
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
        System.out.println("\n1. Проверить баланс");
        System.out.println("2. Посмотреть все транзакции");
        System.out.println("3. Добавить транзакцию");
        System.out.println("4. Редактировать транзакцию");
        System.out.println("5. Удалить транзакцию");
        System.out.println("6. Установить бюджет категории");
        System.out.println("7. Посмотреть статус бюджета");
        System.out.println("8. Сделать перевод");
        System.out.println("9. Выйти из аккаунта");
        System.out.println("10. Выйти из программы");
        System.out.print("Выберите действие: ");
    }

    public static void handleMainMenu() {
        printWelcome();

        var session = DataFileService.loadSession();
        if (session != null) {
            user_id = session.getUserId();
            isAuthenticated = true;
            CustomIO.PrintSuccess("Сессия восстановлена");
        }
        
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
                        if (isAuthenticated) {
                            DataFileService.saveSession(user_id);
                        }
                        break;
                    case "2":
                        user_id = AuthService.register();
                        if (user_id != null) {
                            isAuthenticated = true;
                            DataFileService.saveSession(user_id);
                        }
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
                        double balance = WalletService.checkBalance(user_id);
                        CustomIO.PrintSuccess("Ваш баланс: " + balance);
                        break;
                    case "2":
                        List<Transaction> transactionList = WalletService.getAllTransactions(user_id);
                        CustomIO.PrintSuccess(transactionList.toString());
                        break;
                    case "3":
                        WalletService.addTransaction(user_id);
                        break;
                    case "4":
                        WalletService.editTransaction(user_id);
                        break;
                    case "5":
                        handleRemoveTransaction();
                        break;
                    case "6":
                        WalletService.handleSetBudget(user_id);
                        break;
                    case "7":
                        WalletService.showBudgetStatus(user_id);
                        break;
                    case "8":
                        handleTransfer();
                        break;
                    case "9":
                        isAuthenticated = false;
                        user_id = null;
                        DataFileService.deleteSession();
                        CustomIO.PrintSuccess("Вы успешно вышли из аккаунта");
                        break;
                    case "10":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            }
        }
    }


    private static void handleTransfer() {
        System.out.print("Введите имя получателя: ");
        String recipient = scanner.nextLine();
        System.out.print("Введите сумму перевода: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            WalletService.transferMoney(user_id, recipient, amount);
        } catch (NumberFormatException e) {
            CustomIO.PrintError("Неверный формат числа");
        }
    }

    private static void handleRemoveTransaction() {
        List<Transaction> transactions = WalletService.getAllTransactions(user_id);

        while (true) {
            System.out.print("Введите индекс транзакции для удаления (или 'exit' для выхода): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                return;
            }

            try {
                int removeIndex = Integer.parseInt(input);

                if (removeIndex < 0 || removeIndex >= transactions.size()) {
                    CustomIO.PrintError("Индекс вне диапазона. Пожалуйста, введите корректный индекс.");
                    continue;
                }

                WalletService.removeTransaction(user_id, removeIndex);
                break;
            } catch (NumberFormatException e) {
                CustomIO.PrintError("Неверный ввод. Пожалуйста, введите корректный индекс или 'exit' для выхода.");
            }
        }
    }

    public static String readChoice() {
        return scanner.nextLine().trim();
    }

    public static String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray);
        } else {
            return readInput(prompt);
        }
    }

    public static Transaction askToTransaction() {
        System.out.print("Введите сумму (отрицательная для расхода, положительная для дохода): ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (amount == 0) {
                CustomIO.PrintError("Сумма не должна быть равна нулю. Введите положительное или отрицательное число.");
                return null;
            }
        } catch (NumberFormatException e) {
            CustomIO.PrintError("Неверный формат числа");
            return null;
        }

        System.out.print("Введите категорию: ");
        String category = scanner.nextLine();

        return new Transaction(-1, amount, category);
    }

    public static boolean askToNoRetry() {
        String input = scanner.nextLine().toLowerCase();
        return input.equals("да") || input.equals("yes") || input.equals("y");
    }
}
