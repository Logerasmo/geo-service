package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.entity.Country;

public class GeoServiceTest {
    @ParameterizedTest
    @ValueSource(strings = {"127.0.0.1", "172.0.32.11", "96.44.183.149"})
    public void GeoServiceTst(String ip){
        GeoService geoService = new GeoServiceImpl();
        Assertions.assertThrows(RuntimeException.class,
                ()->{geoService.byCoordinates(12,12);});
        if (ip.equals("127.0.0.1")){
            Assertions.assertNull(geoService.byIp(ip).getCountry());
        } else if (ip.equals("172.0.32.11")) {
            Assertions.assertEquals(Country.RUSSIA, geoService.byIp(ip).getCountry());
        } else if (ip.equals("96.44.183.149")) {
            Assertions.assertEquals(Country.USA, geoService.byIp(ip).getCountry());
        }

    }
}
