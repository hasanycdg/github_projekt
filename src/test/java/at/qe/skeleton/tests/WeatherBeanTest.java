package at.qe.skeleton.tests;

import at.qe.skeleton.external.exceptions.WeatherApiException;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.*;
import at.qe.skeleton.external.model.geocoding.GeocodingDTO;
import at.qe.skeleton.external.model.shared.WeatherDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.internal.ui.beans.AutocompleteBean;
import at.qe.skeleton.internal.ui.beans.WeatherBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherBeanTest {

    @Mock
    private WeatherApiRequestService mockWeatherApiRequestService;
    @Mock
    private AutocompleteBean mockAutocompleteBean;
    @Mock
    private CurrentAndForecastAnswerDTO mockWeather;

    @InjectMocks
    private WeatherBean weatherBeanUnderTest;

    @BeforeEach
    void setUp() {
        weatherBeanUnderTest.setWeather(mockWeather);
    }

    @Test
    void testSearchWeather() {
        // Setup
        // Configure AutocompleteBean.getSelectedGeocodingDTO(...).
        final GeocodingDTO geocodingDTO = new GeocodingDTO("name", Map.ofEntries(Map.entry("value", "value")), 0.0, 0.0,
                "country", "state");
        when(mockAutocompleteBean.getSelectedGeocodingDTO()).thenReturn(geocodingDTO);

        when(mockAutocompleteBean.getDisplayName(
                new GeocodingDTO("name", Map.ofEntries(Map.entry("value", "value")), 0.0, 0.0, "country",
                        "state"))).thenReturn("result");

        // Configure WeatherApiRequestService.retrieveCurrentAndForecastWeather(...).
        final CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO = new CurrentAndForecastAnswerDTO(0.0, 0.0,
                "timezone", 0, new CurrentWeatherDTO(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC), 0.0, 0.0, 0, 0, 0.0, 0, 0, 0, 0.0,
                0.0, 0.0, 0.0, 0.0, new WeatherDTO(0L, "title", "description", "icon")),
                List.of(new PrecipitationByTimestampDTO(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC), 0.0)),
                List.of(new HourlyWeatherDTO(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC), 0.0,
                        0.0, 0, 0.0, 0.0, 0, 0, 0, 0.0, 0.0, 0.0, 0, 0.0, 0.0,
                        new WeatherDTO(0L, "title", "description", "icon"))),
                List.of(new DailyWeatherDTO(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC), 0.0, "summary",
                        new DailyTemperatureAggregationDTO(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                        new TemperatureAggregationDTO(0.0, 0.0, 0.0, 0.0), 0, 0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0.0, 0.0,
                        new WeatherDTO(0L, "title", "description", "icon"))), LocalDate.of(2020, 1, 1),
                List.of(new AlertDTO("senderName", "event",
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC), "description",
                        List.of("value"))));
        when(mockWeatherApiRequestService.retrieveCurrentAndForecastWeather(0.0, 0.0))
                .thenReturn(currentAndForecastAnswerDTO);

        weatherBeanUnderTest.searchWeather();

    }



    @Test
    void testSearchWeather_WeatherApiRequestServiceThrowsWeatherApiException() {

        final GeocodingDTO geocodingDTO = new GeocodingDTO("name", Map.ofEntries(Map.entry("value", "value")), 0.0, 0.0,
                "country", "state");
        when(mockAutocompleteBean.getSelectedGeocodingDTO()).thenReturn(geocodingDTO);

        when(mockAutocompleteBean.getDisplayName(
                new GeocodingDTO("name", Map.ofEntries(Map.entry("value", "value")), 0.0, 0.0, "country",
                        "state"))).thenReturn("result");
        when(mockWeatherApiRequestService.retrieveCurrentAndForecastWeather(0.0, 0.0))
                .thenThrow(WeatherApiException.class);


        weatherBeanUnderTest.searchWeather();

    }

    @Test
    void testGetLatitude() {
        assertEquals(0.0, weatherBeanUnderTest.getLatitude(), 0.0001);
    }

    @Test
    void testGetLongitude() {
        assertEquals(0.0, weatherBeanUnderTest.getLongitude(), 0.0001);
    }

    @Test
    void testLocationGetterAndSetter() {
        final String location = "location";
        weatherBeanUnderTest.setLocation(location);
        assertEquals(location, weatherBeanUnderTest.getLocation());
    }

    @Test
    void testGetWeather() {
        assertEquals(mockWeather, weatherBeanUnderTest.getWeather());
    }

    @Test
    void testButtonPressedGetterAndSetter() {
        final Boolean buttonPressed = false;
        weatherBeanUnderTest.setButtonPressed(buttonPressed);
        assertFalse(weatherBeanUnderTest.getButtonPressed());
    }
}
