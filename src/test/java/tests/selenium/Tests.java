package tests.selenium;

import helper.EnumFilter;
import helper.Properties;
import helper.Range;
import helper.RangeFilter;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.YandexMarket;
import steps.Steps;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Класс содержит тесты.
 * @author Наталья Танькова
 */
public class Tests {
    /**
     * Драйвер
     * @author Наталья Танькова
     */
    private WebDriver driver;
    /**
     * Время явного и неявного ожидания в секундах
     * @author Наталья Танькова
     */
    int timeout = 5;
    /**
     * Очищает папку allure-results от предыдущих отчетов.
     * @author Наталья Танькова
     */
    //@BeforeAll
    public static void beforeAll() {
        File dir = new File("allure-results");
        File[] files = dir.listFiles();
        Arrays.stream(files).forEach(x -> x.delete());
    }
    /**
     * Запускает браузер. Открывает окно на полный экран. Устанавливает неявное ожидание.
     * @author Наталья Танькова
     */
    @BeforeEach
    public void beforeEach() {
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }
    /**
     * Открывает https://ya.ru/. Переходит на заданный сервис. Выбирает категорию. Устанавливает значения фильтра.
     * Проверяет, что на странице отображается более заданного элементов. Проверяет, что полученные результаты
     * соответствуют заданным фильтрам. Переходит на первую страницу. Запоминает первый элемент. Ищет его в поиске.
     * Проверяет, что в результатах поиска есть искомый элемент.
     * @author Наталья Танькова
     * @param serviceName название сервиса
     * @param category название категории товаров
     * @param subCategory название подкатегории товаров
     * @param enumFilters Список фильтров для полей типа checkbox, которые принимают перечень значений. Состоит из
     *                    названия фильтра и списка строковых значений.
     * @param rangeFilters Список фильтров, принимающий диапазон значений. Состоит из названия фильтра, минимального и
     *                     максимального значений. Если необходимо передать только минимальное или только максимальное
     *                     значение, следует воспользоваться методами setMin или setMax класса RangeFilter.
     * @param amountOfElements значение для проверки количества элементов на первой странице
     */
    @Feature("Проверка результатов поиска")
    @DisplayName("Проверка результатов поиска")
    @ParameterizedTest(name="{displayName}, {arguments}")
    @MethodSource("helper.DataProvider#provideArguments")
    public void seleniumYandexMarket(String serviceName, String category, String subCategory,
                                     List<EnumFilter> enumFilters, List<RangeFilter> rangeFilters,
                                     int amountOfElements) {
        Steps.setDriver(driver);
        Steps.setTimeout(timeout);
        //Steps.openSite(Properties.testProperties.yandexUrl());
        //Steps.openService(serviceName);
        Steps.openSite(Properties.testProperties.yandexMarketUrl());
        Steps.openCatalog();
        Steps.pointOnCategory(category);
        Steps.openSubcategory(subCategory);
        for (EnumFilter filter : enumFilters)
            Steps.setFilter(filter.getName(), filter.getValues());
        for (RangeFilter filter : rangeFilters)
            if (filter.isMinSet() && filter.isMaxSet())
                Steps.setFilter(filter.getName(), filter.getMin(), filter.getMax());
            else if (filter.isMinSet())
                Steps.setFilter(filter.getName(), Range.MIN, filter.getMin());
            else
                Steps.setFilter(filter.getName(), Range.MAX, filter.getMax());
        YandexMarket market = new YandexMarket(driver,timeout);
        market.waitForDataRefresh();
        List<WebElement> resultList = market.getResultList();
        Assertions.assertTrue(resultList.size() >= amountOfElements,
                "На первой странице отображается не больше " + amountOfElements + " элементов");
        Steps.checkGoodsMatchFilters();
        Steps.goToFirstPage();
        String firstResult = Steps.getNameOfFirstResult();
        Steps.sendKeys(firstResult);
        Steps.pressSearchButton();
        Steps.checkElement(firstResult);
    }
    /**
     * Закрывает браузер.
     * @author Наталья Танькова
     */
    @AfterEach
    public void afterEach() {
        driver.quit();
    }
}
