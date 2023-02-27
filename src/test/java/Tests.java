import helper.Assertions;
import helper.Filter;
import helper.Properties;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.YandexMarket;
import steps.Steps;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Класс содержит тесты.
 * @author Наталья
 */
public class Tests {
    /**
     * Драйвер
     */
    private WebDriver driver;
    /**
     * Время явного и неявного ожидания в секундах
     */
    int timeout = 5;
    /**
     * Очищает папку allure-results от предыдущих отчетов
     */
    @BeforeAll
    public static void beforeAll() {
        File dir = new File("allure-results");
        File[] files = dir.listFiles();
        Arrays.stream(files).forEach(x -> x.delete());
    }
    /**
     * Запускает браузер. Открывает окно на полный экран. Устанавливает неявное ожидание.
     */
    @BeforeEach
    public void beforeEach() {
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }
    /**
     * Открывает https://ya.ru/. Переходит на Маркет. Выбирает категорию ноутбуки. Устанавливает значения фильтра.
     * Проверяет, что на странице отображается более 12 элементов. Проверяет, что полученные результаты соответствуют
     * заданному фильтру. Переходит на первую страницу. Запоминает первый элемент. Ищет его в поиске. Проверяет, что
     * в результатах есть искомый элемент.
     *
     * @param serviceName
     * @param category категория товаров
     * @param subCategory подкатегория товаров
     * @param rangeFilter фильтр, устанавливающий диапазон значений
     * @param min минимальное значение "Цена"
     * @param max максимальное значение цена
     * @param enumFilter фильтр, устанавливающий несколько значений
     * @param enumValue1 значение фильтра, устанавливающего несколько значений
     * @param enumValue2 значение фильтра, устанавливающего несколько значений
     * @param amountOfElements значение для проверки количества элементов на первой странице
     */
    @Feature("Проверка результатов поиска")
    @DisplayName("Проверка результатов поиска")
    @ParameterizedTest(name="{displayName}, {arguments}")
    //@CsvSource("Маркет, Ноутбуки и компьютеры, Ноутбуки, Цена, 30000, 35000, Производитель, HP, Acer, 12")
    @CsvSource("Маркет, Ноутбуки и компьютеры, Ноутбуки, Цена, 10000, 900000, Производитель, Lenovo, HUAWEI, 12")
    public void SeleniumYandexMarket(String serviceName, String category, String subCategory,
                            String rangeFilter, String min, String max,
                            String enumFilter, String enumValue1, String enumValue2,
                            int amountOfElements) {
        Steps.setDriver(driver);
        Steps.setTimeout(timeout);
        Steps.openSite(Properties.testProperties.yandexUrl());
        Steps.openService(serviceName);
        Steps.openCatalog();
        Steps.pointOnCategory(category);
        Steps.openSubcategory(subCategory);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter(enumFilter, "enum",List.of(enumValue1,enumValue2)));
        filters.add(new Filter(rangeFilter, "range", List.of(min,max)));
        YandexMarket market = new YandexMarket(driver,timeout);
        Steps.setFilter(filters);
        market.waitForDataRefresh();
        List<Filter> setFilters = market.getFilters();
        List<WebElement> resultList = market.getResultList();
        Assertions.assertTrue(resultList.size() >= amountOfElements,
                "На первой странице отображается не больше " + amountOfElements + " элементов");
        Steps.checkFilters(setFilters);
        Steps.goToFirstPage();
        String firstResult = Steps.getNameOfFirstResult();
        Steps.sendKeys(firstResult);
        Steps.pressSearchButton();
        Steps.checkElement(firstResult);
    }
    /**
     * Закрывает браузер.
     */
    @AfterEach
    public void afterEach() {
        driver.quit();
    }
}
