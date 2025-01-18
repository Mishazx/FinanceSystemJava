package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.models.Data;
import ru.mishazx.financesystem.models.Transaction;
import ru.mishazx.financesystem.models.User;
import ru.mishazx.financesystem.models.Wallet;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class DataService {
    private static Data data = DataFileService.loadData(); // Изменено на Data

    public static boolean isValidUserId(UUID userId) {
        for (User user : data.getUsers()) { // Получаем пользователей из объекта Data
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public static double checkBalance(UUID userId) {
        for (User user : data.getUsers()) { // Получаем пользователей из объекта Data
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                return wallet.getBalance();
            }
        }
        return 0;
    }

    public static void addTransaction(UUID userId) {
        Scanner scanner = new Scanner(System.in);

        // Prompt for transaction amount
        System.out.print("Введите сумму транзакции: ");
        double amount = scanner.nextDouble();

        // Prompt for transaction category
        System.out.print("Введите категорию транзакции: ");
        String category = scanner.next();

        // Prompt for income or expense
        System.out.print("Это доход? (да/нет): ");
        String incomeResponse = scanner.next();
        Boolean isIncome = incomeResponse.equalsIgnoreCase("да");

        // Create a new transaction
        Transaction transaction = new Transaction(amount, category, isIncome);

        // Find the user and add the transaction to their wallet
        for (User user : data.getUsers()) { // Получаем пользователей из объекта Data
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                wallet.addTransaction(transaction); // Add the transaction to the wallet

                DataFileService.saveData(data); // Сохраняем обновленный объект Data
                System.out.println("Транзакция успешно добавлена.");
                return; // Exit the method after adding the transaction
            }
        }

        // If user is not found
        System.out.println("Пользователь не найден.");
    }
}
