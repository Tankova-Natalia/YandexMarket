package helper;

import org.aeonbits.owner.ConfigFactory;

/**
 * Предоставляет доступ к методам интерфейса TestProperties
 * @author Наталья Танькова
 */
public class Properties {
    public  static TestProperties testProperties = ConfigFactory.create(TestProperties.class);
}
