package ru.mishazx.financesystem.models;

import java.time.LocalDateTime;

public class Transaction {
    private final double amount;
    private final String category;
    private final boolean isIncome;
    private final LocalDateTime dateTime;

    public Transaction(double amount, String category) {
        this.amount = amount;
        this.category = category;
        this.isIncome = amount >= 0;
        this.dateTime = LocalDateTime.now();
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
        return String.format("%s: %.2f руб. (%s) - %s", 
            isIncome ? "Доход" : "Расход",
            Math.abs(amount),
            category,
            dateTime);
    }
}
