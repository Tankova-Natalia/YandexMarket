package helper;

import org.aeonbits.owner.Config;

/**
 * Интерфейс для взаимодействия с property файлом
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties",
        "system:env",
        "file:src/test/resources/tests.properties"
})
public interface TestProperties extends Config{
    /**
     * Возвращает из property файла адрес Yandex
     * @return адрес Yandex
     */
    @Config.Key("yandex.url")
    String yandexUrl();

}
