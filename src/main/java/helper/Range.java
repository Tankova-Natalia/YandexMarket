package helper;

/**
 * Перечисление границ диапазона
 * @author Наталья Танькова
 */
public enum Range {
    /**
     * Минимальное значение
     * @author Наталья Танькова
     */
    MIN("минимальное"),
    /**
     * Максимальное значение
     * @author Наталья Танькова
     */
    MAX("максимальное");
    private String name;
    Range(String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
