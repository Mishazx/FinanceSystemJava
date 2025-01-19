package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.models.*;
import ru.mishazx.financesystem.utils.CustomIO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.mishazx.financesystem.handlers.MenuHandler.askToTransaction;

public class WalletService {
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
        Transaction transaction = askToTransaction();

        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                wallet.addTransaction(transaction);

                DataFileService.saveData(data);
                return;
            }
        }

        CustomIO.PrintError("Пользователь не найден.");
    }

    public static void getAllTransactions(UUID userId) {
        Data data = DataFileService.loadData();

        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                List<Transaction> transactions = wallet.getTransactions();
                if (transactions.isEmpty()) {
                    CustomIO.PrintInfo("У вас пока нет транзакций.");
                } else {
                    CustomIO.PrintSuccess("Ваши транзакции:");
                    transactions.forEach(t -> CustomIO.PrintInfo(t.toString()));
                }
                return;
            }
        }
        CustomIO.PrintError("Пользователь не найден.");
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

        // Создаем транзакции для обоих пользователей
        Transaction outgoingTransaction = new Transaction(-amount, "Перевод пользователю " + toUsername);
        Transaction incomingTransaction = new Transaction(amount, "Перевод от " + fromUser.getUsername());

        fromUser.getWallet().addTransaction(outgoingTransaction);
        toUser.getWallet().addTransaction(incomingTransaction);

        DataFileService.saveData(data);
        CustomIO.PrintSuccess("Перевод успешно выполнен.");
    }
}
