package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO;
import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO.CloudCover;
import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO.Humidity;
import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO.Precipitation;
import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO.Pressure;
import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO.Temperature;
import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO.Wind;
import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO.Wind.Max;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class DailyAggregationDTOTest {

    @Test
    void testDailyAggregationDTO() {
        LocalDate testDate = LocalDate.now();
        CloudCover testCloudCover = new CloudCover(50);
        Humidity testHumidity = new Humidity(60);
        Precipitation testPrecipitation = new Precipitation(10.0);
        Pressure testPressure = new Pressure(1013.0);
        Temperature testTemperature = new Temperature(15.0, 25.0, 22.0, 18.0, 20.0, 16.0);
        Max testMaxWind = new Max(10.0, 180.0);
        Wind testWind = new Wind(testMaxWind);

        DailyAggregationDTO dto = new DailyAggregationDTO(45.0, -75.0, "EST", testDate, "metric", testCloudCover, testHumidity, testPrecipitation, testPressure, testTemperature, testWind);

        assertEquals(45.0, dto.latitude());
        assertEquals(-75.0, dto.longitude());
        assertEquals("EST", dto.timezone());
        assertEquals(testDate, dto.date());
        assertEquals("metric", dto.units());
        assertEquals(testCloudCover, dto.cloudCover());
        assertEquals(testHumidity, dto.humidity());
        assertEquals(testPrecipitation, dto.precipitation());
        assertEquals(testPressure, dto.pressure());
        assertEquals(testTemperature, dto.temperature());
        assertEquals(testWind, dto.wind());
    }

    @Test
    void testCloudCover(){
    CloudCover cloudCover = new CloudCover(75);
    assertEquals(75, cloudCover.afternoon());
}

    @Test
    void testHumidity() {
        Humidity humidity = new Humidity(65);
        assertEquals(65, humidity.afternoon());
    }

    @Test
    void testPrecipitation() {
        Precipitation precipitation = new Precipitation(12.5);
        assertEquals(12.5, precipitation.total());
    }

    @Test
    void testPressure() {
        Pressure pressure = new Pressure(1020.0);
        assertEquals(1020.0, pressure.afternoon());
    }

    @Test
    void testTemperature() {
        Temperature temperature = new Temperature(10.0, 20.0, 18.0, 15.0, 17.0, 13.0);
        assertEquals(10.0, temperature.min());
        assertEquals(20.0, temperature.max());
        assertEquals(18.0, temperature.afternoon());
        assertEquals(15.0, temperature.night());
        assertEquals(17.0, temperature.evening());
        assertEquals(13.0, temperature.morning());
    }

    @Test
    void testWindAndMax() {
        Max maxWind = new Max(20.0, 270.0);
        Wind wind = new Wind(maxWind);
        assertEquals(maxWind, wind.max());
        assertEquals(20.0, wind.max().speed());
        assertEquals(270.0, wind.max().direction());
    }
}
