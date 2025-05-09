package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;

public class LocalizationServiceTest {
    @ParameterizedTest
    @EnumSource(Country.class)
    public void LocalizationServiceTst(Country country){
        LocalizationService localizationService = new LocalizationServiceImpl();
        if (country == Country.RUSSIA){
            Assertions.assertEquals("Добро пожаловать",
                    localizationService.locale(country));
        }else {
            Assertions.assertEquals("Welcome",
                    localizationService.locale(country));
        }
    }
}
