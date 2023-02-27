package pages;

import helper.Assertions;
import helper.CustomWait;
import helper.Filter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * PageObject для страницы Яндекс Маркета
 * @author Наталья
 */
public class YandexMarket {
    /**
     * Драйвер
     */
    private WebDriver driver;
    /**
     * Время для явного ожидания в секундах
     */
    private long timeout;
    /**
     * Селектор для кнопки, которая открывает каталог
     */
    private String catalogButtonSelector = "//*[@id='catalogPopupButton']";
    /**
     * Селектор для элементов из результата поиска
     */
    private String listSelector = "//*[@data-zone-name='snippet-card']";
    /**
     * Селектор для названия элементов из результатов поиска
     */
    private String titleSelector = ".//*[@data-zone-name='title']";
    /**
     * Селектор для поля поиска
     */
    private String searchFieldSelector = "//input[@id='header-search']";
    /**
     * Селектор для кнопки поиска
     */
    private String searchButtonSelector = "//button[contains(.,'Найти')]";
    /**
     * Селектор для кнопки для перехода на следующую страницу
     */
    private String nextPageButtonSelector = "//*[contains(@data-auto,'pagination-next')]";
    /**
     * Селектор для элемента, который появляется во время загрузки данных
     */
    private String preloaderSelector = "//*[contains(@data-grabber,'SearchSerp')]/*[contains(@data-auto, 'preloader')]";
    public YandexMarket(WebDriver driver, int timeout) {
        this.driver = driver;
        this.timeout = timeout;
    }
    /**
     * Открывает каталог
     */
    public void openCatalog() {
        driver.findElement(By.xpath(catalogButtonSelector)).click();
    }
    /**
     * Наводит курсор на заданную категорию
     * @param categoryName название категории
     */
    public void pointOnCategory(String categoryName) {
        String categorySelector = "//*[@data-zone-name='catalog-content']//*[contains(text(),'" + categoryName + "')]";
        long start = System.currentTimeMillis();
        while (driver.findElements(By.xpath(categorySelector)).size() == 0){
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long duration = System.currentTimeMillis()- start;
            if (duration  >= timeout){
                if (duration >= 10000 )
                    throw new RuntimeException("Время ожидания элемента с селектором " +categorySelector + " вышло.");
                driver.get(driver.getCurrentUrl());
                openCatalog();
            }
        }
        WebElement category = driver.findElement(By.xpath(categorySelector));
        Actions action = new Actions(driver);
        action.moveToElement(category).build().perform();
    }
    /**
     * Открывает заданную подкатегорию
     * @param subcategory название подкатегории
     */
    public void openSubcategory(String subcategory){
        driver.findElement(By.xpath(
                "//*[@role='tabpanel']//ul//*[contains(text(), '" + subcategory + "')]")).click();
    }
    /**
     * Устанавливает фильтр
     * @param filter
     */
    public void setFilter(Filter filter){
        String name = filter.getName();
        switch (filter.getType()){
            case "enum":
                String showAllSelector = "//*[contains(@data-zone-data, '" + name + "')]" +
                        "//*[contains(text(),'Ещё') or contains(text(),'Показать всё')]";
                if (driver.findElements(By.xpath(showAllSelector)).size() > 0)
                    driver.findElement(By.xpath(showAllSelector)).click();
                List<String> valuesEnum = filter.getValues();
                for (int i = 0; i < valuesEnum.size(); i++){
                    WebElement searchField = null;
                    String value = valuesEnum.get(i);
                    String searchFieldSelector = "//*[contains(@data-zone-data, '" + name + "')]" +
                            "//input[contains(@type,'text')]";
                    if (driver.findElements(By.xpath(searchFieldSelector)).size() > 0)
                        searchField = driver.findElement(By.xpath(searchFieldSelector));
                    if (searchField != null)
                        searchField.sendKeys(value);
                    String fieldSelector = "//*[contains(@data-zone-data, '" + name + "')]" +
                            "//*[contains(text(),'" + value + "')]";
                    WebElement field = driver.findElement(By.xpath(fieldSelector));
                    String inputSelector = "//*[contains(@data-zone-data, '" + name + "')]" +
                            "//input[contains(following-sibling::*,'" + value + "')]";
                    WebElement input = driver.findElement(By.xpath(inputSelector));
                    if (!input.isSelected())
                        field.click();
                    if (searchField != null) {
                        String clear = "//*[contains(@data-zone-data, '" + name + "')]" + "//button[@title='Очистить']";
                        driver.findElement(By.xpath(clear)).click();
                    }
                }
                break;
            case "range":
                List<String> valuesRange = filter.getValues();
                if (!valuesRange.get(0).equals("") && !valuesRange.get(1).equals("")){
                    driver.findElement(By.xpath(
                            "//*[contains(@data-zone-data, '" + name + "')]" +
                                    "//input[contains(@id, 'min')]")).sendKeys(valuesRange.get(0));
                    driver.findElement(By.xpath(
                            "//*[contains(@data-zone-data, '" + name + "')]" +
                                    "//input[contains(@id, 'max')]")).sendKeys(valuesRange.get(1));
                } else if (!valuesRange.get(0).equals("")){
                    driver.findElement(By.xpath(
                            "//*[contains(@data-zone-data, '" + name  + "')]" +
                                    "//input[contains(@id, 'min')]")).sendKeys(valuesRange.get(0));
                } else if (!valuesRange.get(1).equals("")){
                    driver.findElement(By.xpath(
                            "//*[contains(@data-zone-data, '" + name  + "')]" +
                                    "//input[contains(@id, 'max')]")).sendKeys(valuesRange.get(1));
                }
                break;
            case "radio":
                System.out.println("radio");
                break;
        }
    }
    /**
     * Переходит на следующую страницу
     */
    public void nextPage(){
        driver.findElement(By.xpath(nextPageButtonSelector)).click();
        CustomWait wait = new CustomWait(driver, timeout);
        wait.untilPresenceOfElement(preloaderSelector);
        wait.untilAbsenceOfElement(preloaderSelector);
    }
    /**
     * Возвращает результат поиска
     * @return список элементов
     */
    public List<WebElement> getResultList() {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//footer")))
                .build().perform();
        List<WebElement> resultList = driver.findElements(By.xpath(listSelector));
        return resultList;
    }
    /**
     * Возвращает название заданного элемента
     * @param element элемент
     * @return название элемента
     */
    public String getTitle(WebElement element){
        return element.findElement(By.xpath(titleSelector)).getText();
    }
    /**
     * Возвращает поле ввода для поиска
     * @return поле ввода
     */
    public WebElement getSearchField(){
        return driver.findElement(By.xpath(searchFieldSelector));
    }
    /**
     * Возвращает кнопку для поиска
     * @return кнопка для поиска
     */
    public WebElement getSearchButton(){
        return driver.findElement(By.xpath(searchButtonSelector));
    }
    /**
     * Получает заданные фильтры из текущего адреса
     * @return список фильтров
     */
    public List<Filter> getFilters(){
        String url = driver.getCurrentUrl();
        List<Filter> filters = new ArrayList<>();
        String priceFromRegex = "&pricefrom=\\d+";
        Pattern patternPriceFrom = Pattern.compile(priceFromRegex);
        Matcher matcherPriceFrom = patternPriceFrom.matcher(url);
        Filter priceFilter = null;
        if (matcherPriceFrom.find()) {
            String priceFrom = url.substring(matcherPriceFrom.start(), matcherPriceFrom.end());
            if (priceFilter == null)
                priceFilter = new Filter("Цена", "range","₽", new ArrayList<>());
            priceFilter.getValues().add(priceFrom.substring("&pricefrom=".length()));
        }
        String priceToRegex = "&priceto=\\d+";
        Pattern priceToPattern = Pattern.compile(priceToRegex);
        Matcher priceToMatcher = priceToPattern.matcher(url);
        if (priceToMatcher.find()) {
            String priceTo = url.substring(priceToMatcher.start(), priceToMatcher.end());
            if (priceFilter == null) {
                priceFilter = new Filter("Цена", "range","₽", new ArrayList<>());
                priceFilter.getValues().add("");
            }
            priceFilter.getValues().add(priceTo.substring("&priceto=".length()));
        }
        if (priceFilter != null) {
            if (priceFilter.getValues().size() == 1)
                priceFilter.getValues().add("");
            filters.add(priceFilter);
        }
        String glfilterEnumRegex = "&glfilter=\\d+%3A\\d{6,}(%2C\\d+)*";
        Pattern glfilterEnumPattern = Pattern.compile(glfilterEnumRegex);
        Matcher glfilterEnumMatcher = glfilterEnumPattern.matcher(url);
        while (glfilterEnumMatcher.find()) {
            String filterSubString = url.substring(glfilterEnumMatcher.start(), glfilterEnumMatcher.end());
            String filterId = filterSubString.substring("&glfilter=".length(),filterSubString.indexOf("%3A"));
            WebElement filterElement = driver.findElement(By.xpath(
                    "//*[@data-zone-name='SearchFilters']//*[@data-filter-id= '"
                            + filterId + "']"));
            String type = filterElement.getAttribute("data-filter-type");
            String filterName = filterElement.findElement(By.xpath(".//legend")).getText();
            filterSubString = filterSubString.substring(filterSubString.indexOf("%3A") + "%3A".length());
            String[] values = filterSubString.split("%2C");
            List<String> list = new ArrayList<>();
            for (int j = 0; j < values.length; j++)
                list.add(driver.findElement(By.xpath(
                        "//*[@data-filter-value-id='" + values[j] + "']")).getText());
            Filter filterEnum = new Filter(filterName, type,list);
            filters.add(filterEnum);
        }
        String glfilterRangeRegex = "&glfilter=\\d+%3A\\d*~\\d*";
        Pattern glfilterRangePatter = Pattern.compile(glfilterRangeRegex);
        Matcher glfilterRangeMatcher = glfilterRangePatter.matcher(url);
        while (glfilterRangeMatcher.find()) {
            String filterSubString = url.substring(glfilterRangeMatcher.start(), glfilterRangeMatcher.end());
            String filterId = filterSubString.substring("&glfilter=".length(),filterSubString.indexOf("%3A"));
            WebElement filterElement = driver.findElement(By.xpath(
                    "//*[@data-zone-name='SearchFilters']//*[@data-filter-id= '"
                            + filterId + "']"));
            String type = filterElement.getAttribute("data-filter-type");
            String[] ar = filterElement.findElement(By.xpath(".//legend")).getText().split(", ");
            String filterName = ar[0];
            String measurementUnit = ar[1];
            filterSubString = filterSubString.substring(filterSubString.indexOf("%3A") + "%3A".length());
            String[] values = filterSubString.split("~");
            List<String> list = new ArrayList<>();
            for (int j = 0; j < values.length; j++)
                list.add(values[j]);
            Filter filterEnum = new Filter(filterName, type, measurementUnit,list);
            filters.add(filterEnum);
        }
        return filters;
    }
    /**
     * Проверяет, удовлетворяют ли элементы заданному фильтру
     * @param resultList список элементов
     * @param filter фильтр
     */
    public void checkFilter(List<WebElement> resultList, Filter filter){
        boolean ans = false;
        switch (filter.getType()){
            case "enum":
                ans = resultList.stream().allMatch(x->{
                    List<String> values = filter.getValues();
                    boolean contains = false;
                    String text = x.getText().toLowerCase();
                    for (String value : values)
                        contains = contains || text.contains(value.toLowerCase());
                    return contains;
                });
                break;
            case "range":
                String measurementUnit = filter.getMeasurementUnit();
                     ans = resultList.stream().allMatch(x->{
                        List<String> values = filter.getValues();
                        String text = x.getText();
                        String rangeRagex = "\\d* ?\\d+.?\\d* ?"+measurementUnit;
                        Pattern rangePattern = Pattern.compile(rangeRagex);
                        Matcher rangeMatcher = rangePattern.matcher(text);
                        if (rangeMatcher.find()) {
                            String stringValue = text.substring(rangeMatcher.start(), rangeMatcher.end()).trim().replace(
                                            measurementUnit, "")
                                    .replace(" ", "");
                            Double value = Double.parseDouble(stringValue);
                            if (!values.get(0).equals("") && !values.get(1).equals("")){
                              return value >=Double.parseDouble(values.get(0)) && value <=Double.parseDouble(values.get(1));
                            } else if (!values.get(0).equals("")){
                                return value >= Double.parseDouble(values.get(0));
                            } else if (!values.get(1).equals("")){
                                return value <=Double.parseDouble(values.get(1));
                            } else
                                return true;
                        } else
                            return false;
                    });
                break;
            case "radio":
                System.out.println("radio");
                break;
        }
        Assertions.assertTrue(ans,
                "Полученные значения не соответствуют заданным значениям \"" + filter.getValues() +
                        "\" фильтра \"" + filter.getName() + "\"");
    }
    /**
     * Проверяет, содержится ли на странице кнопка для перехода на следующую страницу
     * @return логическое значение
     */
    public boolean containsNextPageButton(){
        return driver.findElements(By.xpath("//*[contains(@data-auto,'pagination-next')]")).size() > 0;
    }

    /**
     * Ждет обновления данных
     */
    public void waitForDataRefresh(){
        CustomWait wait = new CustomWait(driver, timeout);
        wait.untilPresenceOfElement(preloaderSelector);
        wait.untilAbsenceOfElement(preloaderSelector);
    }
}