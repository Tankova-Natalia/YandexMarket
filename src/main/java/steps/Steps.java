package steps;

import helper.Assertions;
import helper.CustomWait;
import helper.Range;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.YandexMarket;
import pages.YandexSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс содержит шаги.
 * @author Наталья Танькова
 */
public class Steps {
    /**
     * Драйвер
     * @author Наталья Танькова
     */
    private static WebDriver driver;
    /**
     * Время явного ожидания в секундах
     * @author Наталья Танькова
     */
    private static int timeout;
    /**
     * Максимальное время выполнения цикла в минутах
     * @author Наталья Танькова
     */
    private static long maxWaitInMinutes = 5;
    /**
     * Максимальное время выполнения цикла в секундах
     * @author Наталья Танькова
     */
    private static long maxWainInSec = maxWaitInMinutes * 60;
    /**
     * Максимальное время выполнения цикла в миллисекундах
     * @author Наталья Танькова
     */
    private static long maxWaitInMilliSec = maxWainInSec * 1000;
    public static void setDriver(WebDriver currentDriver){
        driver = currentDriver;
    }
    public static void setTimeout(int t) {
        timeout = t;
    }
    /**
     * Переходит на указанный сайт.
     * @author Наталья Танькова
     * @param url адрес сайта
     */
    @Step("Переходим на сайт {url}")
    public static void openSite(String url){
        driver.get(url);
    }
    /**
     * Открывает сервис Яндекса.
     * @author Наталья Танькова
     * @author Наталья Танькова
     * @param serviceName название сервиса
     */
    @Step("Открываем {serviceName}")
    public static void openService(String serviceName){
        YandexSearch yandexSearch = new YandexSearch(driver);
        yandexSearch.showAllServices();
        yandexSearch.openService(serviceName);
    }
    /**
     * Открывает каталог.
     * @author Наталья Танькова
     */
    @Step("Открываем каталог")
    public static void openCatalog(){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.openCatalog();
    }
    /**
     * Наводит мышь на заданную категорию.
     * @author Наталья Танькова
     * @param category название категории
     */
    @Step("Наводим курсор на категорию \"{category}\"")
    public static void pointOnCategory(String category){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.pointOnCategory(category);
    }
    /**
     * Открывает заданную подкатегорию.
     * @author Наталья Танькова
     * @param subcategory название подкатегории
     */
     @Step("Открываем подкатегорию \"{subcategory}\"")
     public static void openSubcategory(String subcategory){
         YandexMarket market = new YandexMarket(driver, timeout);
         market.openSubcategory(subcategory);
     }
    /**
     * Устанавливает диапазон значений указанного фильтра.
     * @author Наталья Танькова
     * @param filterName название фильтра
     * @param min минимальное значение
     * @param max максимальное значение
     */
    @Step("Устанавливаем диапазон значений [{min}, {max}] фильтра {filterName}")
    public static void setFilter(String filterName, double min, double max){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.setFilter(filterName, min, max);
    }
    /**
     * Устанавливает минимальное или максимальное значение диапазона указанного фильтра.
     * @author Наталья Танькова
     * @param filterName название фильтра
     * @param range какое значение необходимо установить минимальные или максимальное
     * @param value значение
     */
    @Step("Устанавливаем {range} значение фильтра {filterName} равно {value}")
    public static void setFilter(String filterName, Range range, double value){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.setFilter(filterName, range, value);
    }

    /**
     * Устанавливает перечень значений указанного фильтра типа checkbox.
     * @author Наталья Танькова
     * @param filterName название фильтра
     * @param values строковые значения
     */
    @Step("Устанавливаем перечень значений {values} фильтра {filterName}")
    public static void setFilter(String filterName, List<String> values){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.setFilter(filterName,values);
    }
    /**
     * Проверяет, что все товары на всех страницах соответствуют фильтру. Если не соответствует, возвращает список,
     * состоящий из названия товара, не соответствующего фильтрам, и сообщений об ошибке
     * Если цикл выполняется дольше maxWaitInMinutes минут, тест не проходит.
     * @author Наталья Танькова
     */
    @Step("Проверяем, что все товары соответствуют заданным фильтрам")
    public static void checkGoodsMatchFilters(){
        YandexMarket market = new YandexMarket(driver,timeout);
        Map<String, List<String>> filters = market.getFilters();
        List<WebElement> resultList = market.getResultList();
        Map<String,List<String>> elementsNotMatch = new HashMap<>();
        market.checkGoodsMatchFilters(resultList, filters,elementsNotMatch);
        long start = System.currentTimeMillis();
        while (market.containsNextPageButton()) {
            market.nextPage();
            resultList = market.getResultList();
            market.checkGoodsMatchFilters(resultList, filters,elementsNotMatch);
            if (System.currentTimeMillis() - start >= maxWaitInMilliSec)
                org.junit.jupiter.api.Assertions.fail("Время выполнения цикла превысило " + maxWaitInMinutes + " минут.");
        }
        Assertions.assertEquals(0, elementsNotMatch.size(),
                "Заданы фильтры:\n"+ filters +"\nСледующие товары не соответствуют фильтру:\n" + elementsNotMatch);
    }
    /**
     * Открывает первую страницу.
     * @author Наталья Танькова
     */
    @Step("Возвращаемся на первую страницу")
    public static void goToFirstPage(){
        String currentUrl = driver.getCurrentUrl();
        int idx = currentUrl.indexOf("&page");
        if (idx > 0)
            driver.get(currentUrl.substring(0,idx));
}
    /**
     * Возвращает наименование первого элемента.
     * @author Наталья Танькова
     * @return наименование первого элемента
     */
    @Step("Получаем наименование первого элемента")
    public static String getNameOfFirstResult(){
        YandexMarket market = new YandexMarket(driver, timeout);
        return market.getTitle(market.getResultList().get(0));
    }
    /**
     * Вводит в поисковую строку значение.
     * @author Наталья Танькова
     * @param value значение
     */
    @Step("Вводим в поисковую строку \"{value}\"")
    public static void sendKeys(String value){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.getSearchField().sendKeys(value);
    }
    /**
     * Нажимает кнопку поиска.
     * @author Наталья Танькова
     */
    @Step("Нажимаем кнопку поиска")
    public static void pressSearchButton(){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.getSearchButton().click();
    }
    /**
     * Проверяет, что заданное значение присутствует в поисковой выборке.
     * @author Наталья Танькова
     * @param name значение
     */
    @Step("Проверяем, что значение \"{name}\" присутствует в поисковой выборке")
    public static void checkElement(String name){
        YandexMarket market = new YandexMarket(driver, timeout);
        CustomWait wait = new CustomWait(driver, timeout);
        wait.untilPresenceOfElement("//footer");
        String result = market.getTitle(market.getResultList().get(0));
        Assertions.assertTrue(result.contains(name)||name.contains(result),
            "По запросу " + name + " нет подходящих элементов");
    }
}
