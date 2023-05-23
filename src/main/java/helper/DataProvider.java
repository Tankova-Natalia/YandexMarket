package helper;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс дата-провайдер
 * @author Наталья Танькова
 */
public class DataProvider {
    /**
     * Предоставляет данные для теста seleniumYandexMarket.
     * enumFilters - это список фильтров для полей типа checkbox, которые принимают перечень значений. Состоит из
     * названия фильтра и списка строковых значений.
     * rangeFilters - это список фильтров, принимающий диапазон значений. Состоит из названия фильтра, минимального и
     * максимального значений. Если необходимо передать только минимальное или только максимальное значение, следует
     * воспользоваться методами setMin или setMax класса RangeFilter.
     * @author Наталья Танькова
     * @return поток аргументов
     */
    public static Stream<Arguments> provideArguments(){
        List<EnumFilter> enumFilters = new ArrayList<>();
        enumFilters.add(new EnumFilter("Производитель", List.of("Lenovo", "HUAWEI")));
        List<RangeFilter> rangeFilters = new ArrayList<>();
        rangeFilters.add(new RangeFilter("Цена", 10000,900000));
        return Stream.of(
                Arguments.of("Маркет", "Ноутбуки и компьютеры", "Ноутбуки", enumFilters, rangeFilters, 12)
        );
    }
}
