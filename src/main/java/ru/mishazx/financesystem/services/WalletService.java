package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.models.*;
import ru.mishazx.financesystem.utils.CustomIO;

import java.util.*;

import static ru.mishazx.financesystem.handlers.MenuHandler.askToTransaction;

public class WalletService {
    private static final Scanner sc = new Scanner(System.in);

    public static double checkBalance(UUID userId) {
        Data data = DataFileService.loadData();

        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                return wallet.getBalance();
            }
        }
        return 0;
    }

    public static void addTransaction(UUID userId) {
        Data data = DataFileService.loadData();

        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                int id = wallet.getTransactions().size() + 1;
                Transaction transaction = askToTransaction(id);
                Transaction newTransaction = new Transaction(id, transaction.getAmount(), transaction.getCategory());
                wallet.addTransaction(newTransaction);

                DataFileService.saveData(data);
                return;
            }
        }

        CustomIO.PrintError("Пользователь не найден.");
    }

    public static List<Transaction> getAllTransactions(UUID userId) {
        Data data = DataFileService.loadData();
        List<Transaction> transactions = new ArrayList<>();

        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                transactions = wallet.getTransactions();
                return transactions;
            }
        }
        CustomIO.PrintError("Пользователь не найден.");
        return transactions;
    }

    public static void setBudget(UUID userId, String category, double limit) {
        Data data = DataFileService.loadData();

        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                wallet.setBudget(category, limit);
                DataFileService.saveData(data);
                CustomIO.PrintSuccess("Бюджет для категории " + category + " установлен: " + limit);
                return;
            }
        }
        CustomIO.PrintError("Пользователь не найден.");
    }

    public static void removeTransaction(UUID user_id, int removeIndex) {
        List<Transaction> transactions = WalletService.getAllTransactions(user_id);

        while (true) {
            try {
                if (removeIndex < 0 || removeIndex >= transactions.size()) {
                    CustomIO.PrintError("Индекс вне диапазона. Пожалуйста, введите корректный индекс.");
                    continue;
                }

                Data data = DataFileService.loadData();
                for (User user : data.getUsers()) {
                    if (user.getId().equals(user_id)) {
                        Wallet wallet = user.getWallet();
                        wallet.removeTransaction(removeIndex);
                        DataFileService.saveData(data);
                        return;
                    }
                }
                CustomIO.PrintError("Пользователь не найден.");
                break;
            } catch (NumberFormatException e) {
                CustomIO.PrintError("Неверный ввод. Пожалуйста, введите корректный индекс или 'exit' для выхода.");
            }
        }
    }


    public static void transferMoney(UUID user_id) {
        CustomIO.PrintInfo("Введите имя получателя: ");
        String recipient = sc.nextLine();
        CustomIO.PrintInfo("Введите сумму перевода: ");
        try {
            double amount = Double.parseDouble(sc.nextLine());
            WalletService.transferMoney(user_id, recipient, amount);
        } catch (NumberFormatException e) {
            CustomIO.PrintError("Неверный формат числа");
        }
    }

    public static void handleSetBudget(UUID user_id) {
        System.out.print("Введите название категории: ");
        String category = sc.nextLine();
        System.out.print("Введите лимит бюджета: ");
        try {
            double limit = Double.parseDouble(sc.nextLine());
            if (limit < 0) {
                CustomIO.PrintError("Лимит бюджета не может быть отрицательным");
                return;
            }
            WalletService.setBudget(user_id, category, limit);
        } catch (NumberFormatException e) {
            CustomIO.PrintError("Неверный формат числа");
        }
    }

    public static void showBudgetStatus(UUID userId) {
        Data data = DataFileService.loadData();

        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                Map<String, Category> categories = wallet.getCategories();
                
                CustomIO.PrintSuccess("Статус бюджета по категориям:");
                for (Category category : categories.values()) {
                    String status = String.format("%s: Лимит: %.2f, Потрачено: %.2f, Осталось: %.2f",
                            category.getName(),
                            category.getBudgetLimit(),
                            category.getCurrentSpent(),
                            category.getRemainingBudget());
                    
                    if (category.isOverBudget()) {
                        CustomIO.PrintWarning(status);
                    } else {
                        CustomIO.PrintInfo(status);
                    }
                }
                return;
            }
        }
        CustomIO.PrintError("Пользователь не найден.");
    }

    public static void transferMoney(UUID fromUserId, String toUsername, double amount) {
        if (amount <= 0) {
            CustomIO.PrintError("Сумма перевода должна быть положительной.");
            return;
        }

        Data data = DataFileService.loadData();
        User fromUser = null;
        User toUser = null;

        for (User user : data.getUsers()) {
            if (user.getId().equals(fromUserId)) {
                fromUser = user;
            } else if (user.getUsername().equals(toUsername)) {
                toUser = user;
            }
        }

        if (fromUser == null) {
            CustomIO.PrintError("Отправитель не найден.");
            return;
        }
        if (toUser == null) {
            CustomIO.PrintError("Получатель не найден.");
            return;
        }
        if (fromUser.getWallet().getBalance() < amount) {
            CustomIO.PrintError("Недостаточно средств для перевода.");
            return;
        }

        List<Transaction> fromTransactions = fromUser.getWallet().getTransactions();
        List<Transaction> toTransactions = toUser.getWallet().getTransactions();

        int outgoingId = fromTransactions.size() + 1;
        Transaction outgoingTransaction = new Transaction(outgoingId, -amount, "Прочее");

        int incomingId = toTransactions.size() + 1;
        Transaction incomingTransaction = new Transaction(incomingId, amount, "Прочее");

        fromUser.getWallet().addTransaction(outgoingTransaction);
        toUser.getWallet().addTransaction(incomingTransaction);

        DataFileService.saveData(data);
        CustomIO.PrintSuccess("Перевод успешно выполнен.");
    }

    public static void editTransaction(UUID userId, int index, Transaction newTransaction) {
        Data data = DataFileService.loadData();
        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                wallet.editTransaction(index, newTransaction);
                DataFileService.saveData(data);
                return;
            }
        }
        CustomIO.PrintError("Пользователь не найден.");
    }
}
