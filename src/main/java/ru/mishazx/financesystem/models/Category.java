package ru.mishazx.financesystem.models;

public class Category {
    private final String name;
    private double budgetLimit;
    private double currentSpent;

    public Category(String name, double budgetLimit) {
        this.name = name;
        this.budgetLimit = budgetLimit;
        this.currentSpent = 0;
    }

    public String getName() {
        return name;
    }

    public double getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    public double getCurrentSpent() {
        return currentSpent;
    }

    public void addSpent(double amount) {
        this.currentSpent += Math.abs(amount);
    }

    public double getRemainingBudget() {
        return budgetLimit - currentSpent;
    }

    public boolean isOverBudget() {
        return currentSpent > budgetLimit;
    }
} 