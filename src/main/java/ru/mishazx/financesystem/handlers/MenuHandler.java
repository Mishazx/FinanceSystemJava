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
                        printTransactions(user_id);
                        break;
                    case "3":
                        printTransactions(user_id);
                        WalletService.addTransaction(user_id);
                        break;
                    case "4":
                        printTransactions(user_id);
                        handleEditTransaction();
                        break;
                    case "5":
                        printTransactions(user_id);
                        handleRemoveTransaction();
                        break;
                    case "6":
                        WalletService.handleSetBudget(user_id);
                        break;
                    case "7":
                        WalletService.showBudgetStatus(user_id);
                        break;
                    case "8":
                        WalletService.transferMoney(user_id);
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

    public static Transaction askToTransaction(int index) {
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

        return new Transaction(index, amount, category);
    }

    public static void printTransactions(UUID user_id) {
        List<Transaction> transactions = WalletService.getAllTransactions(user_id);
        if (transactions.isEmpty()) {
            CustomIO.PrintError("Список транзакций пуст.");
            return;
        }

        System.out.println("Список транзакций:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public static boolean askToNoRetry() {
        String input = scanner.nextLine().toLowerCase();
        return input.equals("да") || input.equals("yes") || input.equals("y");
    }

    private static void handleEditTransaction() {
        List<Transaction> transactions = WalletService.getAllTransactions(user_id); // Получаем все транзакции

        while (true) {
            System.out.print("Введите индекс транзакции для редактирования (или 'exit' для выхода): ");
            String input = scanner.nextLine().trim();

            // Проверка на выход
            if (input.equalsIgnoreCase("exit")) {
                return; // Выход из метода
            }

            try {
                int editIndex = Integer.parseInt(input);

                if (editIndex < 0 || editIndex >= transactions.size()) {
                    CustomIO.PrintError("Индекс вне диапазона. Пожалуйста, введите корректный индекс.");
                    continue;
                }

                Transaction editedTransaction = askToTransaction(editIndex);
                if (editedTransaction != null) {
                    WalletService.editTransaction(user_id, editIndex, editedTransaction);
                }
                break; // Выход из цикла, если редактирование прошло успешно
            } catch (NumberFormatException e) {
                CustomIO.PrintError("Неверный ввод. Пожалуйста, введите корректный индекс или 'exit' для выхода.");
            }
        }
    }

    private static void handleRemoveTransaction() {
        List<Transaction> transactions = WalletService.getAllTransactions(user_id); // Получаем все транзакции

        while (true) {
            System.out.print("Введите индекс транзакции для удаления (или 'exit' для выхода): ");
            String input = scanner.nextLine().trim();

            // Проверка на выход
            if (input.equalsIgnoreCase("exit")) {
                return; // Выход из метода
            }

            try {
                int removeIndex = Integer.parseInt(input) - 1; // Пробуем преобразовать ввод в целое число и уменьшаем на 1
                
                // Проверка существования транзакции
                if (removeIndex < 0 || removeIndex >= transactions.size()) {
                    CustomIO.PrintError("Индекс вне диапазона. Пожалуйста, введите корректный индекс.");
                    continue; // Возвращаемся к началу цикла
                }

                WalletService.removeTransaction(user_id, removeIndex);
                break; // Выход из цикла, если удаление прошло успешно
            } catch (NumberFormatException e) {
                CustomIO.PrintError("Неверный ввод. Пожалуйста, введите корректный индекс или 'exit' для выхода.");
            }
        }
    }
}
