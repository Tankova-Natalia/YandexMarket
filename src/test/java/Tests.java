import helper.Assertions;
import helper.Properties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.YandexMarket;
import steps.Steps;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.lang.Thread.onSpinWait;
import static java.lang.Thread.sleep;

public class Tests {

    protected WebDriver driver;

    @BeforeAll
    public static void beforeAll() {
        File dir = new File("allure-results");
        File[] files = dir.listFiles();
        Arrays.stream(files).forEach(x -> x.delete());
    }

    @BeforeEach
    public void beforeEach() {
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @ParameterizedTest
    @CsvSource("Маркет, Ноутбуки и компьютеры, Ноутбуки, Цена, 10000, 900000, Производитель, Lenovo, HUAWEI, 12")
    public void findLaptops(String serviceName, String category, String subCategory,
                            String filter1, long min, long max,
                            String filter2, String company1, String company2,
                            int amountOfElements) {


        Steps.setDriver(driver);
        Steps.openSite(Properties.testProperties.yandexUrl());
        Steps.openService(serviceName);
        Steps.openCategory(category, subCategory);

        Steps.setFilter(filter1, min, max);
        Steps.setFilter(filter2, company1, company2);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        long start = System.currentTimeMillis();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(
                "//*[contains(@data-grabber,'SearchSerp')]/*[contains(@data-auto, 'preloader')]"),0));
        System.out.println(System.currentTimeMillis()-start);

        YandexMarket market = new YandexMarket(driver);
        List<WebElement> resultList = market.getResultList();
        Assertions.assertTrue(resultList.size() >= amountOfElements,
                "На первой странице отображается не больше " + amountOfElements + " элементов");

        //Steps.checkFilters(filter1, min, max, filter2, company1, company2);


        /*driver.get("https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013&allowCollapsing=1&local-offers-first=0&glfilter=7893318%3A459710%2C152981&pricefrom=10000&priceto=900000&page=30");
        String url = driver.getCurrentUrl();
        driver.get(url.substring(0, url.indexOf("&page")));
        rfv
         */
        //YandexMarket market = new YandexMarket(driver);
        /*
        WebElement first = market.getResultList().get(0).findElement(By.xpath(
                ".//*[contains(@data-baobab-name, 'title')]"));
        driver.findElement(By.xpath("//*[@id='header-search']")).sendKeys(first.getText());
        driver.findElement(By.xpath("//*[contains(text(),'Найти')]/..")).click();
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(market.getResultList().stream().anyMatch(x->x.findElement(By.xpath(
                ".//*[contains(@data-baobab-name, 'title')]")).getText().contains(first.getText())));

         */
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
    }

}
