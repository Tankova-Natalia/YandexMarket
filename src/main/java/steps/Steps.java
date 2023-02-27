package steps;

import helper.Assertions;
import helper.CustomWait;
import helper.Filter;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.YandexMarket;
import pages.YandexSearch;
import java.util.List;

/**
 * Класс содержит шаги.
 * @author Наталья
 */
public class Steps {
    /**
     * Драйвер
     */
    private static WebDriver driver;
    /**
     * Время явного ожидания в секундах
     */
    private static int timeout;
    public static void setDriver(WebDriver currentDriver){
        driver = currentDriver;
    }
    public static void setTimeout(int t) {
        timeout = t;
    }
    /**
     * Переходит на сайт
     * @param url адрес сайта
     */
    @Step("Переходим на сайт {url}")
    public static void openSite(String url){
        driver.get(url);
    }
    /**
     * Открывает сервис Яндекса
     * @param serviceName название сервиса
     */
    @Step("Открываем {serviceName}")
    public static void openService(String serviceName){
        YandexSearch yandexSearch = new YandexSearch(driver);
        yandexSearch.showAllServices();
        yandexSearch.openService(serviceName);
    }
    /**
     * Открывает каталог
     */
    @Step("Открываем каталог")
    public static void openCatalog(){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.openCatalog();
    }
    /**
     * Наводит мышь на заданную категорию
     * @param category название категории
     */
    @Step("Наводим курсор на категорию \"{category}\"")
    public static void pointOnCategory(String category){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.pointOnCategory(category);
    }
    /**
     * Открывает заданную подкатегорию
     * @param subcategory название подкатегории
     */
     @Step("Открываем подкатегорию \"{subcategory}\"")
     public static void openSubcategory(String subcategory){
         YandexMarket market = new YandexMarket(driver, timeout);
         market.openSubcategory(subcategory);
     }
    /**
     * Устанавливает фильтры
     * @param filters список фильтров
     */
    @Step("Устанавливаем фильтры")
    public static void setFilter(List<Filter> filters){
        for (Filter filter : filters)
            setFilter(filter);
    }
    /**
     * Устанавливает фильтр
     * @param filter фильтр
     */
    @Step("Устанавливаем фильтр {filter}")
    public static void setFilter(Filter filter){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.setFilter(filter);
    }
    /**
     * Проверяет, что все товары соответствуют фильтру
     * @param filters список фильтров
     */
    @Step("Проверяем, что все товары соответствуют фильтру")
    public static void checkFilters(List<Filter> filters){
        YandexMarket market = new YandexMarket(driver,timeout);
        List<WebElement> resultList = market.getResultList();
        for (Filter filter : filters)
            market.checkFilter(resultList, filter);
        while (market.containsNextPageButton()) {
            market.nextPage();
            resultList = market.getResultList();
            for (Filter filter : filters)
                market.checkFilter(resultList, filter);
        }
    }
    /**
     * Открывает первую страницу
     */
    @Step("Возвращаемся на первую страницу")
    public static void goToFirstPage(){
        String currentUrl = driver.getCurrentUrl();
        int idx = currentUrl.indexOf("&page");
        if (idx > 0)
            driver.get(currentUrl.substring(0,idx));
}
    /**
     * Возвращает наименование первого элемента
     * @return наименование первого элемента
     */
    @Step("Получаем наименование первого элемента")
    public static String getNameOfFirstResult(){
        YandexMarket market = new YandexMarket(driver, timeout);
        return market.getTitle(market.getResultList().get(0));
    }
    /**
     * Вводит в поисковую строку значение
     * @param value значение
     */
    @Step("Вводим в поисковую строку \"{value}\"")
    public static void sendKeys(String value){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.getSearchField().sendKeys(value);
    }
    /**
     * Нажимает кнопку поиска
     */
    @Step("Нажимаем кнопку поиска")
    public static void pressSearchButton(){
        YandexMarket market = new YandexMarket(driver, timeout);
        market.getSearchButton().click();
    }
    /**
     * Проверяет, что заданное значение присутствует в поисковой выборке
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
