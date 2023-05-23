package helper;

import io.qameta.allure.Step;

/**
 * Пользовательские проверки
 * @author Наталья Танькова
 */
public class Assertions {
    /**
     * Проверяет, что нет указанной ошибки
     * @author Наталья Танькова
     * @param condition условие
     * @param message сообщение об ошибке
     */
    @Step("Проверяем, что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }
    /**
     * Проверяет, что нет указанной ошибки
     * @author Наталья Танькова
     * @param expected ожидаемое значение
     * @param actual фактическое значение
     * @param message сообщение об ошибке
     */
    @Step("Проверяем, что нет ошибки: {message}")
    public static void assertEquals(Object expected, Object actual, String message){
        org.junit.jupiter.api.Assertions.assertEquals(expected,actual,message);
    }
}
