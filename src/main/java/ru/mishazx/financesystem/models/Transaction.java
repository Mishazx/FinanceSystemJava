package ru.mishazx.financesystem.models;

import java.time.LocalDateTime;

public class Transaction {
    private final int id;
    private final double amount;
    private final String category;
    private final boolean isIncome;
    private final LocalDateTime dateTime;

    public Transaction(int id, double amount, String category) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.isIncome = amount >= 0;
        this.dateTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f руб. (%s) - %s [ID: %d]", 
            isIncome ? "Доход" : "Расход",
            Math.abs(amount),
            category,
            dateTime,
            id);
    }
}
