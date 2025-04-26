package ru.netology.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocationTest {
    @Test
    public void LocationTst(){
        Location location =
                new Location("Kaluga", Country.RUSSIA, "Timoshenko", 4);
        Assertions.assertEquals(Country.RUSSIA, location.getCountry());
        Assertions.assertEquals("Kaluga", location.getCity());
        Assertions.assertEquals(4, location.getBuilding());
    }
}
