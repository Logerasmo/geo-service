import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

public class Tests {
    @Test
    public void LocationTest(){
        Location location =
                new Location("Kaluga", Country.RUSSIA, "Timoshenko", 4);
        Assertions.assertEquals(Country.RUSSIA, location.getCountry());
        Assertions.assertEquals("Kaluga", location.getCity());
        Assertions.assertEquals(4, location.getBuilding());
    }

    @ParameterizedTest
    @ValueSource(strings = {"127.0.0.1", "172.0.32.11", "96.44.183.149"})
    public void GeoServiceTest(String ip){
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
    @ParameterizedTest
    @EnumSource(Country.class)
    public void LocalizationServiceTest(Country country){
        LocalizationService localizationService = new LocalizationServiceImpl();
        if (country == Country.RUSSIA){
            Assertions.assertEquals("Добро пожаловать",
                    localizationService.locale(country));
        }else {
            Assertions.assertEquals("Welcome",
                    localizationService.locale(country));
        }
    }
    @Test
    public void MessageSenderTest(){
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("172.123.12.19"))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        Mockito.when(geoService.byIp("96.44.183.149"))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");
        Assertions.assertEquals("Добро пожаловать", messageSender.send(headers));
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149");
        Assertions.assertEquals("Welcome", messageSender.send(headers));
        Mockito.verify(geoService, Mockito.atLeast(2)).byIp(Mockito.anyString());
        Mockito.verify(localizationService, Mockito.atLeast(4)).locale(Mockito.any(Country.class));
    }





}
