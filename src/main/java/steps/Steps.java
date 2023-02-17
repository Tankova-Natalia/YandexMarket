package steps;

import helper.Assertions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.YandexMarket;
import pages.YandexSearch;

import java.util.List;

import static java.lang.Thread.sleep;

public class Steps {
    private static WebDriverWait wait;
    private static WebDriver driver;
    public static void setDriver(WebDriver currentDriver){
        driver = currentDriver;
        wait = new WebDriverWait(driver,30);
    }
    @Step("Переходим на сайт {url}")
    public static void openSite(String url){
        driver.get(url);
    }
    @Step("Переходим на {serviceName}")
    public static void openService(String serviceName){
        YandexSearch yandexSearch = new YandexSearch(driver);
        yandexSearch.showAllServices();
        yandexSearch.openService(serviceName);
    }
    @Step("Открываем категорию {category}: {subCategory}")
    public static void openCategory(String category, String subCategory){
        YandexMarket market = new YandexMarket(driver);
        market.openCatalog();

        market.openCategory(category, subCategory);
    }
    @Step("Фильтру {filter} устанавливаем минимальное значение {min} и максимальное {max}")
    public static void setFilter(String filter, long min, long max){
        YandexMarket market = new YandexMarket(driver);
        market.setFilter(filter, min, max);
    }

    @Step("Задаем фильтр {values}")
    public static void setFilter(String ... values){
        YandexMarket market = new YandexMarket(driver);
        market.setFilter(values);
    }
    @Step("Проверяем, что все товары соответствуют фильтру")
    public static void checkFilters(String filter1,long min, long max, String filter2, String company1, String company2){
        YandexMarket market = new YandexMarket(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        List<WebElement> resultList = market.getResultList();

        Actions actions = new Actions(driver);

        int i = 1;
        helper.Assertions.assertTrue(resultList.stream().allMatch(x->Integer.parseInt(x.findElement(By.xpath(
                                ".//*[contains(@data-auto, 'mainPrice')]/span[not(contains(text(),'₽'))]"))
                        .getText().replace(" ","")) >= min &&
                        Integer.parseInt(x.findElement(By.xpath(
                                        ".//*[contains(@data-auto, 'mainPrice')]/span[not(contains(text(),'₽'))]"))
                                .getText().replace(" ","")) < max)
                ,
                "На странице " + i + " не все товары удовлетворяют условию " + min + " " + max);
        helper.Assertions.assertTrue(resultList.stream().allMatch(x->(x.findElement(By.xpath(
                                ".//*[contains(@data-baobab-name, 'title')]")).
                        getText().toLowerCase().contains(company1.toLowerCase())||
                        x.findElement(By.xpath(
                                        ".//*[contains(@data-baobab-name, 'title')]"))
                                .getText().toLowerCase().contains(company2.toLowerCase()))),
                "На странице " + i + " не все товары удовлетворяют условию "
                        + filter2 + " = " + company1 + " или " + filter2 + " = " + company2);

        i++;
        while (driver.findElements(By.xpath("//*[contains(@data-auto,'pagination-next')]")).size() > 0) {
            actions.moveToElement(driver.findElement(By.xpath(
                    "//*[contains(@data-auto,'pagination-next')]"))).click().build().perform();

            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(
                    "//*[contains(@data-grabber,'SearchSerp')]/*[contains(@data-auto, 'preloader')]"),0));

            resultList = market.getResultList();
            helper.Assertions.assertTrue(resultList.stream().allMatch(x->Integer.parseInt(x.findElement(By.xpath(
                                    ".//*[contains(@data-auto, 'mainPrice')]/span[not(contains(text(),'₽'))]"))
                            .getText().replace(" ","")) >= min &&
                            Integer.parseInt(x.findElement(By.xpath(
                                            ".//*[contains(@data-auto, 'mainPrice')]/span[not(contains(text(),'₽'))]"))
                                    .getText().replace(" ","")) < max)
                    ,
                    "На странице " + i + " не все товары удовлетворяют условию "+  min + " " + max);

            Assertions.assertTrue(resultList.stream().allMatch(x->(x.findElement(By.xpath(
                                    ".//*[contains(@data-baobab-name, 'title')]")).
                            getText().toLowerCase().contains(company1.toLowerCase())||
                            x.findElement(By.xpath(
                                            ".//*[contains(@data-baobab-name, 'title')]"))
                                    .getText().toLowerCase().contains(company2.toLowerCase()))),
                    "На странице " + i + " не все товары удовлетворяют условию "
                            + filter2 + " = " + company1 + " или " + filter2 + " = " + company2);

            i++;
        }
    }

}
