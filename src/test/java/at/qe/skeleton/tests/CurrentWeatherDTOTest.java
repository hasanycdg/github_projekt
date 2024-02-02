package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.shared.WeatherDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;

class CurrentWeatherDTOTest {

    @Test
    void testRecordData() {
        Instant now = Instant.now();
        WeatherDTO weatherDTO = new WeatherDTO(123L, "Clear", "clear sky", "01d");

        CurrentWeatherDTO weather = new CurrentWeatherDTO(
                now,    // timestamp
                now,    // sunrise
                now,    // sunset
                25.0,   // temperature
                24.5,   // feelsLikeTemperature
                1013,   // pressure
                80,     // humidity
                15.0,   // dewPoint
                40,     // clouds
                5,      // uvi
                10000,  // visibility
                0.5,    // rain
                null,   // snow
                7.0,    // windSpeed
                10.0,   // windGust
                180.0,  // windDirection
                weatherDTO
        );

        assertAll("weather",
                () -> assertEquals(now, weather.timestamp()),
                () -> assertEquals(now, weather.sunrise()),
                () -> assertEquals(now, weather.sunset()),
                () -> assertEquals(25.0, weather.temperature()),
                () -> assertEquals(24.5, weather.feelsLikeTemperature()),
                () -> assertEquals(1013, weather.pressure()),
                () -> assertEquals(80, weather.humidity()),
                () -> assertEquals(15.0, weather.dewPoint()),
                () -> assertEquals(40, weather.clouds()),
                () -> assertEquals(5, weather.uvi()),
                () -> assertEquals(10000, weather.visibility()),
                () -> assertEquals(0.5, weather.rain()),
                () -> assertNull(weather.snow()),
                () -> assertEquals(7.0, weather.windSpeed()),
                () -> assertEquals(10.0, weather.windGust()),
                () -> assertEquals(180.0, weather.windDirection()),
                () -> assertEquals(weatherDTO, weather.weather()),
                () -> assertEquals(123L, weatherDTO.id()),
                () -> assertEquals("Clear", weatherDTO.title()),
                () -> assertEquals("clear sky", weatherDTO.description()),
                () -> assertEquals("01d", weatherDTO.icon())
        );
    }
}
