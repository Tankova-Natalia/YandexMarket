package helper;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.lang.Thread.sleep;

public class MyCondition {
    public static void waitForOrRestart(WebDriver driver, String selector){
        long start = System.currentTimeMillis();
        boolean isLoaded = false;
        while (!isLoaded){
            try {
                sleep(10);
                driver.findElement(By.xpath(selector));
            } catch (NoSuchElementException e){
                driver.get(driver.getCurrentUrl());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //if (System.currentTimeMillis() - start >= )

        }
    }
}
