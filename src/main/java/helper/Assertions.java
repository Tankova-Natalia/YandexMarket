package helper;

import io.qameta.allure.Step;

/**
 * Пользовательские проверки
 * @author Наталья
 */
public class Assertions {
    /**
     * Проверяет, что нет указанной ошибки
     * @param condition условие
     * @param message сообщение об ошибке
     */
    @Step("Проверяем, что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }
}
