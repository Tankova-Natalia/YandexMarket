package helper;

import org.aeonbits.owner.Config;

/**
 * Интерфейс для взаимодействия с property файлом
 * @author Наталья Танькова
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties",
        "system:env",
        "file:src/test/resources/tests.properties"
})
public interface TestProperties extends Config{
    /**
     * Возвращает из property файла адрес Yandex
     * @author Наталья Танькова
     * @return адрес Yandex
     */
    @Config.Key("yandex.url")
    String yandexUrl();
@Config.Key("yandex.market.url")
    String yandexMarketUrl();

}
