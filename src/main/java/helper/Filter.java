package helper;

/**
 * Фильтр
 * @author Наталья Танькова
 */
public abstract class Filter {
    /**
     * Название фильтра
     * @author Наталья Танькова
     */
    private String name;

    public Filter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
