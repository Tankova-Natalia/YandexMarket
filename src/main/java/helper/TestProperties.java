package helper;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties",
        "system:env",
        "file:src/test/resources/tests.properties"
})
public interface TestProperties extends Config{

    @Config.Key("yandex.url")
    String yandexUrl();

}
