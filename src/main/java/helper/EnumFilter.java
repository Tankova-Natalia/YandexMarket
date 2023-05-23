package helper;

import java.util.List;

/**
 * Фильтр для поля checkbox, который принимает перечень значений
 * @author Наталья Танькова
 */
public class EnumFilter extends Filter{
    /**
     * Список значений
     * @author Наталья Танькова
     */
    private List<String> values;

    public EnumFilter(String name) {
        super(name);
    }

    public EnumFilter(String name, List<String> values) {
        super(name);
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return super.getName() + " " + values;
    }
}
