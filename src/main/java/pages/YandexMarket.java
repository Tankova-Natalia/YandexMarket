package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static java.lang.Thread.sleep;

public class YandexMarket {

    private WebDriver driver;
    private WebElement catalogButton;
    private String catalogButtonSelector = "//*[@id='catalogPopupButton']";
    private String categorySelector;
    private WebElement category;
    private static WebDriverWait wait;
    private long timeout = 10;
    //private String listSelector = "//*[@data-test-id='virtuoso-item-list']//*[contains(@data-zone-name, 'snippet-card')]";
    private String listSelector = "//*[contains(@data-zone-name, 'snippet-card')]";

    private String titleSelector = ".//*[contains(@data-baobab-name, 'title')]";
    private String priceSelector = ".//*[contains(@data-auto, 'mainPrice')]";

    public YandexMarket(WebDriver driver) {
        this.driver = driver;
        catalogButton = driver.findElement(By.xpath(catalogButtonSelector));
    }

    public void openCatalog() {
        wait = new WebDriverWait(driver, timeout);

        driver.get(driver.getCurrentUrl());
        boolean isLoaded = false;

        //while (!isLoaded) {

            driver.findElement(By.xpath(catalogButtonSelector)).click();
           /* try {
                sleep(10);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                        "//*[@data-zone-name='catalog-content']")));
                isLoaded = true;
            } catch (TimeoutException e) {
                driver.get(driver.getCurrentUrl());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

 */
    }


    public void openCategory(String categoryName, String subCategory) {
        categorySelector = "//*[@role='tablist']//*[contains(text(),'" + categoryName + "')]/..";
        category = driver.findElement(By.xpath(categorySelector));
        Actions action = new Actions(driver);
        action.moveToElement(category).build().perform();
        //category.click();

        driver.findElement(By.xpath(
                "//*[@role='tabpanel']//ul//*[contains(text(), '" + subCategory + "')]")).click();

    }

    public void setFilter(String fieldName, long minValue, long maxValue) {
        WebElement min = driver.findElement(By.xpath(
                "//*[contains(@data-zone-data, '\"filterName\":\"" + fieldName + "\"')]//input[contains(@id, 'min')]"));
        min.sendKeys(Long.toString(minValue));

        WebElement max = driver.findElement(By.xpath(
                "//*[contains(@data-zone-data, '\"filterName\":\"" + fieldName + "\"')]//input[contains(@id, 'max')]"));
        max.sendKeys(Long.toString(maxValue));

    }

    public void setFilter(String... values) {

        Actions actions = new Actions(driver);
        if (driver.findElements(By.xpath(
                "//*[contains(@data-zone-data, '\"filterName\":\"" + values[0] + "\"')]" +
                        "//*[contains(text(),'Ещё') or contains(text(),'Показать всё')]")).size()>0) {
            driver.findElement(By.xpath(
                    "//*[contains(@data-zone-data, '\"filterName\":\"" + values[0] + "\"')]" +
                            "//*[contains(text(),'Ещё') or contains(text(),'Показать всё')]")).click();
        }
        WebElement searchField = null;
        if (driver.findElements(By.xpath(
                "//*[contains(@data-zone-data, '\"filterName\":\"" + values[0] + "\"')]" +
                        "//input[contains(@type,'text')]")).size()>0){
            searchField = driver.findElement(By.xpath(
                    "//*[contains(@data-zone-data, '\"filterName\":\"" + values[0] + "\"')]" +
                            "//input[contains(@type,'text')]"));


        }
        for (int i = 1; i < values.length; i++) {
            if (searchField != null)
                searchField.sendKeys(values[i]);
            driver.findElement(By.xpath(
                    "//*[contains(@data-zone-data, '\"filterName\":\"" + values[0] + "\"')]" +
                            "//*[contains(text(),'" + values[i] + "')]")).click();
            if (searchField!=null)
                searchField.clear();
        }
    }
    public List<WebElement> getResultList(){
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//*[contains(@data-zone-name,'SearchPager')]")))
                .build().perform();
        List<WebElement> resultList = driver.findElements(By.xpath(listSelector));
        return resultList;
    }
}
