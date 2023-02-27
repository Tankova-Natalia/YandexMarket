package helper;

import java.util.List;

/**
 * Фильтр
 * @author Наталья
 */
public class Filter{
    /**
     * Название фильтра
     */
    private String name;
    /**
     * Тип фильтра
     */
    private String type;
    /**
     * Единицы измерения фильтра
     */
    private  String measurementUnit;
    /**
     * Значения фильтра
     */
    private List<String> values;
    public Filter() {
    }
    public Filter(String name, String type, List<String> values) {
        this.name = name;
        this.type = type;
        this.values = values;
    }
    public Filter(String name, String type, String measurementUnit, List<String> values) {
        this.name = name;
        this.type = type;
        this.measurementUnit = measurementUnit;
        this.values = values;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<String> getValues() {
        return values;
    }
    public void setValues(List<String> values) {
        this.values = values;
    }
    public String getMeasurementUnit() {
        return measurementUnit;
    }
    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }
    @Override
    public String toString() {
        return "Filter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", measurementUnit='" + measurementUnit + '\'' +
                ", values=" + values +
                '}';
    }
}