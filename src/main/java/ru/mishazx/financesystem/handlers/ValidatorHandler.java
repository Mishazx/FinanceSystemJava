package ru.mishazx.financesystem.handlers;

import ru.mishazx.financesystem.exceptions.InvalidAmountException;
import ru.mishazx.financesystem.exceptions.InvalidLoginException;
import ru.mishazx.financesystem.exceptions.InvalidPasswordException;

import java.util.regex.Pattern;

public class ValidatorHandler {
    private static final Pattern latinOnlyPattern = Pattern.compile("^[a-zA-Z0-9@#$%^&+=!]*$");
    private static final Pattern loginPattern = Pattern.compile("^[a-zA-Z0-9._-]*$");

    private static final double MAX_AMOUNT = 999999999.00;

    public static String validLogin(String login) throws InvalidLoginException {
        if (!isLoginValid(login)) {
            throw new InvalidLoginException("Логин должен содержать только английские буквы и быть не короче 3 символов");
        }
        return login;
    }

    private static boolean isLoginValid(String login) throws InvalidLoginException {
        StringBuilder errorMessage = new StringBuilder("Логин не соотвествует следующим требованиям:");

        if (login == null || login.isEmpty()) {
            errorMessage.append("\n- Логин не может быть пустым");
        } else {
            if (login.length() < 3) {
                errorMessage.append("\n- Логин должен быть длиннее 3 символов");
            }
            if (!loginPattern.matcher(login).matches()) {
                errorMessage.append("\n- Логин должен содержать только латинские буквы, цифры и символы: ._- ");
            }
        }
        if (errorMessage.length() > 44) {
            throw new InvalidLoginException(errorMessage.toString());
        }

        return true;
    }

    public static String validPassword(String password) throws InvalidPasswordException {
        if (!isPasswordValid(password)) {
            throw new InvalidPasswordException("Пароль не удовлетворяет требованиям безопасности!");
        }
        return password;
    }

    private static boolean isPasswordValid(String password) throws InvalidPasswordException {
        StringBuilder errorMessage = new StringBuilder("Пароль не соответствует следующим требованиям:");
        boolean isValid = true;

        if (password == null || password.isEmpty()) {
            errorMessage.append("\n- Пароль не может быть пустым");
            isValid = false;
        } else {
            String[] requirements = {
                    "должен быть длиннее 8 символов",
                    "должен содержать хотя бы одну цифру",
                    "должен содержать хотя бы одну английскую строчную букву",
                    "должен содержать хотя бы одну английскую заглавную букву",
                    "должен содержать хотя бы один специальный символ из набора @#$%^&+=!",
                    "не должен содержать пробелы",
                    "должен содержать только латинские буквы, цифры и специальные символы"
            };

            boolean[] checks = {
                    password.length() >= 8,
                    password.matches(".*[0-9].*"),
                    password.matches(".*[a-z].*"),
                    password.matches(".*[A-Z].*"),
                    password.matches(".*[@#$%^&+=!].*"),
                    !password.contains(" "),
                    latinOnlyPattern.matcher(password).matches()
            };

            for (int i = 0; i < requirements.length; i++) {
                if (!checks[i]) {
                    errorMessage.append("\n- Пароль " + requirements[i]);
                    isValid = false;
                }
            }
        }

        if (!isValid) {
            throw new InvalidPasswordException(errorMessage.toString());
        }

        return true;
    }

    private static double getAmount(String amountStr) throws InvalidAmountException {
        String sanitizedAmountStr = amountStr.trim().replace(',', '.');

        if (!sanitizedAmountStr.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            throw new InvalidAmountException("Некорректный формат суммы! Используйте числа и точку для копеек.");
        }

        double amount;
        try {
            amount = Double.parseDouble(sanitizedAmountStr);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Ошибка при преобразовании суммы. Пожалуйста, проверьте ввод.");
        }
        return amount;
    }

    public static double validateAmount(String amountStr) throws InvalidAmountException {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            throw new InvalidAmountException("Сумма не может быть пустой!");
        }

        double amount = getAmount(amountStr);

        if (amount <= 0) {
            throw new InvalidAmountException("Сумма должна быть больше нуля!");
        }
        if (amount > MAX_AMOUNT) {
            throw new InvalidAmountException(String.format("Слишком большая сумма! Максимум %.2f", MAX_AMOUNT));
        }

        return amount;
    }
}
