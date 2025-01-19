package ru.mishazx.financesystem.models;

import ru.mishazx.financesystem.services.WalletService;
import ru.mishazx.financesystem.utils.CustomIO;
import java.util.*;

import static ru.mishazx.financesystem.handlers.MenuHandler.askToTransaction;

public class Wallet {
    private double balance;
    private List<Transaction> transactions;
    private Map<String, Category> categories;
    private int maxTransactionId; // Поле для хранения максимального идентификатора транзакции

    // Конструктор без параметров для GSON
    public Wallet() {
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.categories = new HashMap<>();
        this.maxTransactionId = 0; // Инициализация максимального идентификатора
        initializeDefaultCategories();
    }

    public Wallet(UUID user_id) {
        this();  // Вызываем конструктор без параметров
    }

    private void initializeDefaultCategories() {
        categories.put("Еда", new Category("Еда", 0));
        categories.put("Развлечения", new Category("Развлечения", 0));
        categories.put("Коммунальные услуги", new Category("Коммунальные услуги", 0));
        categories.put("Транспорт", new Category("Транспорт", 0));
        categories.put("Зарплата", new Category("Зарплата", 0));
        categories.put("Прочее", new Category("Прочее", 0));
    }

    public double getBalance() {
        return this.balance;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            CustomIO.PrintDebug("[ИНФО] - Пустая транзакция!");
            return;
        }

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        if (categories == null) {
            categories = new HashMap<>();
            initializeDefaultCategories();
        }

        String categoryName = transaction.getCategory();
        if (!categories.containsKey(categoryName)) {
            CustomIO.PrintError("Ошибка: категория '" + categoryName + "' не существует. Доступные категории:");
            categories.keySet().forEach(cat -> CustomIO.PrintInfo("- " + cat));
            return;
        }

        if (!transaction.isIncome()) {
            Category category = categories.get(categoryName);
            double newSpent = category.getCurrentSpent() + Math.abs(transaction.getAmount());
            if (category.getBudgetLimit() > 0 && newSpent > category.getBudgetLimit()) {
                CustomIO.PrintWarning("Превышен бюджет категории '" + categoryName + "'");
                CustomIO.PrintInfo(String.format("Текущие траты: %.2f, Бюджет: %.2f, Пытаетесь потратить: %.2f",
                        category.getCurrentSpent(), category.getBudgetLimit(), Math.abs(transaction.getAmount())));
                return;
            }
        }

        int id = transactions.size() + 1;
        Transaction newTransaction = new Transaction(id, transaction.getAmount(), transaction.getCategory());
        
        transactions.add(newTransaction);
        this.balance += newTransaction.getAmount();
        
        if (!transaction.isIncome()) {
            Category category = categories.get(categoryName);
            category.addSpent(transaction.getAmount());
        }
        
        if (balance < 0) {
            CustomIO.PrintWarning("Внимание! Ваши расходы превысили доходы!");
        }

        CustomIO.PrintSuccess("Транзакция успешно добавлена.");
    }

    public List<Transaction> getTransactions() {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return new ArrayList<>(transactions);
    }

    public void setBudget(String categoryName, double limit) {
        if (categories == null) {
            categories = new HashMap<>();
            initializeDefaultCategories();
        }
        Category category = categories.get(categoryName);
        if (category != null) {
            category.setBudgetLimit(limit);
        } else {
            CustomIO.PrintError("Ошибка: категория '" + categoryName + "' не существует. Доступные категории:");
            categories.keySet().forEach(cat -> CustomIO.PrintInfo("- " + cat));
        }
    }

    public Map<String, Category> getCategories() {
        if (categories == null) {
            categories = new HashMap<>();
            initializeDefaultCategories();
        }
        return new HashMap<>(categories);
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions.stream()
                .filter(t -> t.getCategory().equals(category))
                .toList();
    }

    public double getCategorySpent(String category) {
        if (categories == null) {
            categories = new HashMap<>();
            initializeDefaultCategories();
        }
        Category cat = categories.get(category);
        return cat != null ? cat.getCurrentSpent() : 0;
    }

    public double getCategoryBudget(String category) {
        if (categories == null) {
            categories = new HashMap<>();
            initializeDefaultCategories();
        }
        Category cat = categories.get(category);
        return cat != null ? cat.getBudgetLimit() : 0;
    }

    public double getCategoryRemaining(String category) {
        if (categories == null) {
            categories = new HashMap<>();
            initializeDefaultCategories();
        }
        Category cat = categories.get(category);
        return cat != null ? cat.getRemainingBudget() : 0;
    }

    public void editTransaction(int index, Transaction newTransaction) {
        if (index < 0 || index >= transactions.size()) {
            CustomIO.PrintError("Ошибка: индекс транзакции вне диапазона.");
            return;
        }

        String categoryName = newTransaction.getCategory();
        if (!categories.containsKey(categoryName)) {
            CustomIO.PrintError("Ошибка: категория '" + categoryName + "' не существует. Доступные категории:");
            categories.keySet().forEach(cat -> CustomIO.PrintInfo("- " + cat));
            return;
        }

        transactions.set(index, newTransaction);
        CustomIO.PrintSuccess("Транзакция успешно отредактирована.");
    }

    public void removeTransaction(int index) {
        if (index < 0 || index >= transactions.size()) {
            CustomIO.PrintError("Ошибка: индекс транзакции вне диапазона.");
            return;
        }
        transactions.remove(index);
        CustomIO.PrintSuccess("Транзакция успешно удалена.");
    }
}
