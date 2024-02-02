package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.currentandforecast.*;
import at.qe.skeleton.external.model.geocoding.GeocodingDTO;
import at.qe.skeleton.external.services.GeocodingApiRequestService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.internal.services.DateRangeService;
import at.qe.skeleton.internal.services.WeatherService;
import at.qe.skeleton.internal.ui.beans.AutocompleteBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import at.qe.skeleton.internal.ui.beans.DateBean;

@ExtendWith(MockitoExtension.class)
public class DateBeanTest {

    @InjectMocks
    private DateBean dateBean;

    private DailyAggregationDTO sampleWeatherData;
    private GeocodingDTO sampleLocation;

    @BeforeEach
    public void init() {
        sampleWeatherData = new DailyAggregationDTO(
                // Assuming these are the correct parameters for your constructor. Adjust as
                // needed.
                48.208174, // latitude
                16.373819, // longitude
                "Europe/Vienna", // timezone
                LocalDate.of(2022, 9, 28), // date
                "Celsius", // units
                new DailyAggregationDTO.CloudCover(50), // CloudCover
                new DailyAggregationDTO.Humidity(60), // Humidity
                new DailyAggregationDTO.Precipitation(5.0), // Precipitation
                new DailyAggregationDTO.Pressure(1013.25), // Pressure
                new DailyAggregationDTO.Temperature(286.75, 290.15, 288.75, 287.35, 288.25, 288.15), // Temperature
                new DailyAggregationDTO.Wind(new DailyAggregationDTO.Wind.Max(15.0, 180)) // Wind
        );

        Map<String, String> localNames = Map.of("en", "Vienna", "de", "Wien");
        sampleLocation = new GeocodingDTO(
                "Vienna", // name
                localNames, // local names
                48.208174, // latitude
                16.373819, // longitude
                "Austria", // country
                "Vienna" // state
        );
    }

    /*
     * SUBMIT DATES FUNCTION WILL BE TESTED TOO
     */

    @Test
    void testIsDateRangeValidFalse() {
        LocalDate startDate = LocalDate.of(2022, 9, 20);
        LocalDate endDate = LocalDate.of(2022, 8, 25);

        assertFalse(DateRangeService.isDateRangeValid(startDate, endDate));
    }

    @Test
    void testCalculateMidpointDateNullInput() {
        assertNull(DateRangeService.calculateMidpointDate(null, null));
    }

    @Test
    void testCalculateMidpointDateNormalInputs() {
        LocalDate startDate = LocalDate.of(2022, 9, 20);
        LocalDate endDate = LocalDate.of(2022, 9, 25);

        LocalDate result = DateRangeService.calculateMidpointDate(startDate, endDate);

        assertEquals(LocalDate.of(2022, 9, 22), result);
    }

    @Test
    void testSetStartDate() {
        LocalDate input = LocalDate.of(2022, 9, 20);
        dateBean.setStartDate(input);

        assertEquals(input, dateBean.getStartDate());
    }

    @Test
    void testSetEndDate() {
        LocalDate input = LocalDate.of(2022, 9, 25);
        dateBean.setEndDate(input);

        assertEquals(input, dateBean.getEndDate());
    }
}
