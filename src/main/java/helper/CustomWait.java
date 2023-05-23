package helper;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.sleep;

/**
 * Пользовательское явное ожидание
 * @author Наталья Танькова
 */
public class CustomWait {
    /**
     * Драйвер
     * @author Наталья Танькова
     */
    private WebDriver driver;
    /**
     * Время явного ожидания в секундах
     * @author Наталья Танькова
     */
    private long timeout;
    public CustomWait(WebDriver driver, long timeout) {
        this.driver = driver;
        this.timeout = timeout;
    }
    /**
     * Ждет появления заданного элемента.
     * @author Наталья Танькова
     * @param selector селектор элемента
     */
    public void untilPresenceOfElement(String selector) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        long start = System.currentTimeMillis();
        while (driver.findElements(By.xpath(selector)).size()==0){
            try {
                sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if ((System.currentTimeMillis()-start)/1000 > timeout)
                Assertions.fail("Время ожидания присутствия элемента " + selector + " вышло");
        }
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }
    /**
     * Ждет исчезновения заданного элемента.
     * @author Наталья Танькова
     * @param selector селектор элемента
     */
    public void untilAbsenceOfElement(String selector) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        long start = System.currentTimeMillis();
        while (driver.findElements(By.xpath(selector)).size()>0){
            try {
                sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if ((System.currentTimeMillis()-start)/1000 > timeout)
                Assertions.fail("Время ожидания присутствия элемента " + selector + " вышло");
        }
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }
}