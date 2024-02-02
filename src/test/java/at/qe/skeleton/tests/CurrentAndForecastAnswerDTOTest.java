package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.currentandforecast.*;
import at.qe.skeleton.external.model.currentandforecast.misc.*;
import at.qe.skeleton.external.model.shared.WeatherDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CurrentAndForecastAnswerDTOTest {

    @Test
    public void testConstructorAndGetters() {
        double latitude = 47.2692;
        double longitude = 11.4041;
        String timezone = "Europe/Vienna";
        int timezoneOffset = 7200;
        Instant now= Instant.now();
        WeatherDTO weatherDTO = new WeatherDTO(123L, "Clear", "clear sky", "01d");
        CurrentWeatherDTO currentWeather = new CurrentWeatherDTO( now, now,now,
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
                weatherDTO);
        List<PrecipitationByTimestampDTO> minutelyPrecipitation = Arrays.asList(
                new PrecipitationByTimestampDTO(Instant.parse("2024-01-20T12:00:00Z"), 0.5),
                new PrecipitationByTimestampDTO(Instant.parse("2024-01-20T12:01:00Z"), 0.4),
                new PrecipitationByTimestampDTO(Instant.parse("2024-01-20T12:02:00Z"), 0.6)
        );
        List<HourlyWeatherDTO> hourlyWeather = Arrays.asList(
                new HourlyWeatherDTO(
                        Instant.parse("2024-01-20T12:00:00Z"), // timestamp
                        15.0, // temperature
                        14.5, // feelsLikeTemperature
                        1013, // pressure
                        55, // humidity
                        10.0, // dewPoint
                        3, // uvi
                        75, // clouds
                        10000, // visibility
                        5.0, // windSpeed
                        7.0, // windGust
                        180, // windDirection
                        1, // probabilityOfPrecipitation
                        0.0, // rain
                        null, // snow (null if not snowing)
                        weatherDTO // weather
                ));

        // Mock data for DailyWeatherDTO
        List<DailyWeatherDTO> dailyWeather = Arrays.asList(
                new DailyWeatherDTO(
                        Instant.now(), Instant.now(), Instant.now(), Instant.now(), Instant.now(),
                        0.5, "Sunny", new DailyTemperatureAggregationDTO(            10.0, // morningTemperature
                        15.0, // dayTemperature
                        12.0, // eveningTemperature
                        8.0,  // nightTemperature
                        7.0,  // minimumDailyTemperature
                        16.0  // maximumDailyTemperature
                ),
                        new TemperatureAggregationDTO(            10.5, // morningTemperature
                                15.5, // dayTemperature
                                12.5, // eveningTemperature
                                9.0   // nightTemperature
                        ), 1013, 60, 10.0, 5.0, 7.5, 180, 20, 5, 0, null, null,
                       weatherDTO
                )
        );

        LocalDate date = LocalDate.now();
        List<AlertDTO> alerts = Arrays.asList(new AlertDTO("senderName", "event", Instant.parse("2024-01-20T21:00:00Z"), Instant.parse("2024-01-21T01:00:00Z"), "description", List.of("severe weather")));

        CurrentAndForecastAnswerDTO dto = new CurrentAndForecastAnswerDTO(
                latitude, longitude, timezone, timezoneOffset,
                currentWeather, minutelyPrecipitation, hourlyWeather,
                dailyWeather, date, alerts);

        Assertions.assertEquals(latitude, dto.latitude());
        Assertions.assertEquals(longitude, dto.longitude());
        Assertions.assertEquals(timezone, dto.timezone());
        Assertions.assertEquals(timezoneOffset, dto.timezoneOffset());
        Assertions.assertEquals(currentWeather, dto.currentWeather());
        Assertions.assertEquals(minutelyPrecipitation, dto.minutelyPrecipitation());

        // Asserting each field in the hourlyWeather list
        Assertions.assertFalse(hourlyWeather.isEmpty());
        HourlyWeatherDTO firstHourlyWeather = hourlyWeather.get(0);
        Assertions.assertEquals(Instant.parse("2024-01-20T12:00:00Z"), firstHourlyWeather.timestamp());
        Assertions.assertEquals(15.0, firstHourlyWeather.temperature());
        Assertions.assertEquals(14.5, firstHourlyWeather.feelsLikeTemperature());
        // Continue asserting other fields of HourlyWeatherDTO...

        // Asserting each field in the dailyWeather list
        Assertions.assertFalse(dailyWeather.isEmpty());
        DailyWeatherDTO firstDailyWeather = dailyWeather.get(0);
        Assertions.assertNotNull(firstDailyWeather.timestamp());
        Assertions.assertEquals(0.5, firstDailyWeather.moonPhase());
        Assertions.assertEquals("Sunny", firstDailyWeather.summary());
        DailyTemperatureAggregationDTO dailyTemp = firstDailyWeather.dailyTemperatureAggregation();
        Assertions.assertEquals(10.0, dailyTemp.morningTemperature());
        // Continue asserting other fields of DailyWeatherDTO...

        Assertions.assertEquals(dailyWeather, dto.dailyWeather());
        Assertions.assertEquals(date, dto.date());
        Assertions.assertEquals(alerts, dto.alerts());
    }
    }

