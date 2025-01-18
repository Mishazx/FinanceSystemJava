package ru.mishazx.financesystem.models;

import ru.mishazx.financesystem.services.DataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Wallet {
    private double balance;
    private final List<Transaction> transactions;
    private Map<String, Double> categoryBudgets;

    public Wallet(UUID user_id) {
        this.balance = DataService.checkBalance(user_id);
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return this.balance;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        if (transaction.getIsIncome()) {
            this.balance += transaction.getAmount();
        } else {
            this.balance -= transaction.getAmount();
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
