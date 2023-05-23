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
     * @author Наталья Танькова
     */
    private WebDriver chromeDriver;
    /**
     * Селектор для кнопки все сервисы
     * @author Наталья Танькова
     */
    private String selectorAllServices = "//a[@title='Все сервисы']";
    /**
     * Кнопка все сервисы
     * @author Наталья Танькова
     */
    private WebElement allServices;
    public YandexSearch(WebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        allServices = this.chromeDriver.findElement(By.xpath(selectorAllServices));
    }
    /**
     * Раскрывает меню сервисов.
     * @author Наталья Танькова
     */
    public void showAllServices(){
        allServices.click();
    }
    /**
     * Открывает заданный сервис.
     * @author Наталья Танькова
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
