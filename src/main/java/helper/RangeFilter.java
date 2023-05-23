package helper;

/**
 * Фильтр, принимающий диапазон значений
 * @author Наталья Танькова
 */
public class RangeFilter extends Filter{
    /**
     * Минимальное значение
     * @author Наталья Танькова
     */
    private double min;
    /**
     * Логическое значение, принимающее значение true, если минимальное значение установлено
     * @author Наталья Танькова
     */
    private boolean isMinSet = false;
    /**
     * Максимальное значение
     * @author Наталья Танькова
     */
    private double max;
    /**
     * Логическое значение, принимающее значение true, если максимальное значение установлено
     * @author Наталья Танькова
     */
    private boolean isMaxSet = false;

    public RangeFilter(String name) {
        super(name);
    }

    public RangeFilter(String name, double min, double max) {
        super(name);
        this.min = min;
        this.max = max;
        isMinSet = true;
        isMaxSet = true;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
        isMinSet = true;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
        isMaxSet = true;
    }

    /**
     * Проверяет, установлено ли минимальное значение
     * @author Наталья Танькова
     * @return true, если минимальное значение установлено, иначе false
     */
    public boolean isMinSet(){
        return isMinSet;
    }
    /**
     * Проверяет, установлено ли максимальное значение
     * @author Наталья Танькова
     * @return true, если максимальное значение установлено, иначе false
     */
    public boolean isMaxSet(){
        return isMaxSet;
    }
    @Override
    public String toString() {
        if (isMinSet && isMaxSet)
            return super.getName() + " от " + min + " до " + max;
        else if (isMinSet)
            return super.getName() + " от " + min;
        else
            return super.getName() + " до " + max;
    }
}
