package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.Set;

/**
 * PageObject для страницы https://ya.ru/
 * @author Наталья
 */
public class YandexSearch{
    /**
     * Драйвер
     */
    private WebDriver chromeDriver;
    /**
     * Селектор для кнопки все сервисы
     */
    private String selectorAllServices = "//a[@title='Все сервисы']";
    /**
     * Кнопка все сервисы
     */
    private WebElement allServices;
    public YandexSearch(WebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        allServices = this.chromeDriver.findElement(By.xpath(selectorAllServices));
    }
    /**
     * Раскрывает меню сервисов
     */
    public void showAllServices(){
        allServices.click();
    }
    /**
     * Открывает заданный сервис
     * @param serviceName название сервиса
     */
    public void openService(String serviceName){
        String selectorService = "//*[contains(text(),'" + serviceName + "')]/..";
        WebElement service = chromeDriver.findElement(By.xpath(selectorService));
        service.click();
        Set<String> tabs = chromeDriver.getWindowHandles();
        for(String tab:tabs)
            chromeDriver.switchTo().window(tab);
    }
}
