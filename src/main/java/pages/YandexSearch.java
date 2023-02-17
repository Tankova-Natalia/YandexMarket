package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Set;

import static java.lang.Thread.sleep;

public class YandexSearch{
    private WebDriver chromeDriver;
    private String selectorAllServices = "//a[@title='Все сервисы']";
    private WebElement allServices;
    private String selectorService;
    private WebElement service;
    public YandexSearch(WebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        allServices = this.chromeDriver.findElement(By.xpath(selectorAllServices));
    }

    public void showAllServices(){
        allServices.click();
    }
//
    public void openService(String serviceName){
        selectorService = "//*[contains(text(),'" + serviceName + "')]/..";
        service = chromeDriver.findElement(By.xpath(selectorService));
        service.click();
        Set<String> tabs = chromeDriver.getWindowHandles();
        for(String tab:tabs)
            chromeDriver.switchTo().window(tab);
        System.out.println(chromeDriver.getTitle());


    }
}
