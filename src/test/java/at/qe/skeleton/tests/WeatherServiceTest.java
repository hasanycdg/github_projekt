package at.qe.skeleton.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.internal.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class WeatherServiceTest {

    @Mock
    private WeatherApiRequestService weatherApiRequestService;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private DailyAggregationDTO createMockDailyAggregationDTO(LocalDate date) {
        return new DailyAggregationDTO(
                40.7128, // latitude
                -74.0060, // longitude
                "EST", // timezone
                date, // date
                "metric", // units
                new DailyAggregationDTO.CloudCover(50), // cloud cover
                new DailyAggregationDTO.Humidity(60), // humidity
                new DailyAggregationDTO.Precipitation(5), // precipitation
                new DailyAggregationDTO.Pressure(1013), // pressure
                new DailyAggregationDTO.Temperature(10, 20, 15, 12, 18, 14), // temperature
                new DailyAggregationDTO.Wind(new DailyAggregationDTO.Wind.Max(10, 180)) // wind
        );
    }

    @Test
    void testRetrieveWeatherDataForRange() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 1, 10);

        when(weatherApiRequestService.retrieveDailyAggregationWeather(anyDouble(), anyDouble(), any(LocalDate.class)))
                .thenAnswer(invocation -> createMockDailyAggregationDTO(invocation.getArgument(2, LocalDate.class)));

        List<DailyAggregationDTO> result = weatherService.retrieveWeatherDataForRange(startDate, endDate, 40.7128, -74.0060);

        assertNotNull(result);
        assertEquals(10, result.size());
    }

    @Test
    void testCalculateAverageWeatherData() {
        LocalDate midpointDate = LocalDate.of(2023, 1, 15);

        for (int i = 0; i < 5; i++) {
            when(weatherApiRequestService.retrieveDailyAggregationWeather(anyDouble(), anyDouble(), any(LocalDate.class)))
                    .thenAnswer(invocation -> createMockDailyAggregationDTO(invocation.getArgument(2, LocalDate.class)));
        }

        DailyAggregationDTO averageWeather = weatherService.calculateAverageWeatherData(midpointDate, 40.7128, -74.0060);

        assertNotNull(averageWeather);

    }

    @Test
    void testCalculateAverageWeather_NormalConditions() {
        List<DailyAggregationDTO> weatherDataList = Arrays.asList(
                createMockDailyAggregationDTO(LocalDate.of(2020, 1, 1)),
                createMockDailyAggregationDTO(LocalDate.of(2020, 1, 2))

        );

        DailyAggregationDTO averageWeather = WeatherService.calculateAverageWeather(weatherDataList);

        assertNotNull(averageWeather);

        double expectedAvgLat = (40.7128 + 40.7128) / 2;
        double expectedAvgLon = (-74.0060 + -74.0060) / 2;
        double expectedAvgTempMin = (10 + 10) / 2;

        assertEquals(expectedAvgLat, averageWeather.latitude());
        assertEquals(expectedAvgLon, averageWeather.longitude());
        assertEquals(expectedAvgTempMin, averageWeather.temperature().min());
    }

    @Test
    void testCalculateAverageWeather_EmptyList() {
        List<DailyAggregationDTO> weatherDataList = Collections.emptyList();

        DailyAggregationDTO averageWeather = WeatherService.calculateAverageWeather(weatherDataList);

        assertNull(averageWeather);
    }

    @Test
    void testCalculateAverageWeather_NullList() {
        DailyAggregationDTO averageWeather = WeatherService.calculateAverageWeather(null);

        assertNull(averageWeather);
    }
}
