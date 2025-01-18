package ru.mishazx.financesystem.services;

import ru.mishazx.financesystem.models.Data;
import ru.mishazx.financesystem.models.Transaction;
import ru.mishazx.financesystem.models.User;
import ru.mishazx.financesystem.models.Wallet;
import ru.mishazx.financesystem.utils.CustomIO;

import java.util.List;
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
                System.out.println("Транзакция успешно добавлена.");
                return;
            }
        }

        System.out.println("Пользователь не найден.");
    }

    public static void getAllTransaction(UUID userId) {
        Data data = DataFileService.loadData();

        for (User user : data.getUsers()) {
            if (user.getId().equals(userId)) {
                Wallet wallet = user.getWallet();
                List<Transaction> listTransaction = wallet.getTransactions();
                CustomIO.PrintSuccess(listTransaction.toString());
            }
        }
    }
}
