package at.qe.skeleton.internal.services;

import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for retrieving and processing weather data.
 * This class communicates with a weather API to fetch daily aggregated weather data
 * and provides functionality to calculate average weather data over specified periods.
 */

@Component
public class WeatherService implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);
    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    /**
     * Retrieves weather data for a specified date range and geographical coordinates.
     *
     * @param startDate  the start date of the range for which weather data is required
     * @param endDate    the end date of the range for which weather data is required
     * @param latitude   the latitude of the location
     * @param longitude  the longitude of the location
     * @return a list of {@link DailyAggregationDTO} representing daily aggregated weather data
     */
    public List<DailyAggregationDTO> retrieveWeatherDataForRange(LocalDate startDate, LocalDate endDate, double latitude, double longitude) {
        List<DailyAggregationDTO> weatherDataList = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            try {
                DailyAggregationDTO dailyWeather = weatherApiRequestService.retrieveDailyAggregationWeather(latitude, longitude, date);
                weatherDataList.add(dailyWeather);
            } catch (Exception e) {
                LOGGER.error("Error while retrieving weather data for date: " + date);
            }
        }
        return weatherDataList;
    }

    /**
     * Calculates the average weather data for the past five years from a given midpoint date.
     *
     * @param midpointDate the date from which the past five years are considered
     * @param latitude     the latitude of the location
     * @param longitude    the longitude of the location
     * @return a {@link DailyAggregationDTO} representing the average weather data
     */

    public DailyAggregationDTO calculateAverageWeatherData(LocalDate midpointDate, double latitude, double longitude) {
        List<DailyAggregationDTO> pastWeatherData = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // Letzten 5 Jahre
            LocalDate dateToCheck = midpointDate.minusYears(i);
            try {
                DailyAggregationDTO yearlyWeather = weatherApiRequestService.retrieveDailyAggregationWeather(latitude, longitude, dateToCheck);
                pastWeatherData.add(yearlyWeather);
            } catch (Exception e) {
                LOGGER.error("Error while retrieving weather data for date: " + dateToCheck);
            }
        }
        return calculateAverageWeather(pastWeatherData);
    }
    /**
     * Calculates the average weather based on a list of daily weather data.
     *
     * @param weatherDataList a list of {@link DailyAggregationDTO} representing daily weather data
     * @return a {@link DailyAggregationDTO} representing the average of the provided weather data,
     *         or null if the input list is null or empty
     */

    public static DailyAggregationDTO calculateAverageWeather(List<DailyAggregationDTO> weatherDataList) {
        if (weatherDataList == null || weatherDataList.isEmpty()) {
            return null;
        }

        double avgLat = 0;
        double avgLon = 0;
        double avgPrecipitationTotal = 0;
        double avgPressureAfternoon = 0;
        double avgHumidityAfternoon = 0;
        double avgTempMin = 0;
        double avgTempMax = 0;
        double avgTempAfternoon = 0;
        double avgTempNight = 0;
        double avgTempEvening = 0;
        double avgTempMorning = 0;
        double avgWindSpeed = 0;
        double avgWindDirection = 0;

        for (DailyAggregationDTO weather : weatherDataList) {
            avgLat += weather.latitude();
            avgLon += weather.longitude();
            avgPrecipitationTotal += weather.precipitation().total();
            avgPressureAfternoon += weather.pressure().afternoon();
            avgHumidityAfternoon += weather.humidity().afternoon();

            DailyAggregationDTO.Temperature temp = weather.temperature();
            avgTempMin += temp.min();
            avgTempMin = Math.round(avgTempMin);
            avgTempMax += temp.max();
            avgTempMax = Math.round(avgTempMax);
            avgTempAfternoon += temp.afternoon();
            avgTempAfternoon = Math.round(avgTempAfternoon);
            avgTempNight += temp.night();
            avgTempNight = Math.round(avgTempNight);
            avgTempEvening += temp.evening();
            avgTempEvening = Math.round(avgTempEvening);
            avgTempMorning += temp.morning();
            avgTempMorning = Math.round(avgTempMorning);

            DailyAggregationDTO.Wind.Max windMax = weather.wind().max();
            avgWindSpeed += windMax.speed();
            avgWindSpeed = Math.round(avgWindSpeed);
            avgWindDirection += windMax.direction();
            avgWindDirection = Math.round(avgWindDirection);
        }

        int count = weatherDataList.size();
        return new DailyAggregationDTO(
                avgLat / count, avgLon / count, "Average", LocalDate.now(), "Average",
                new DailyAggregationDTO.CloudCover((int) (avgHumidityAfternoon / count)),
                new DailyAggregationDTO.Humidity((int) (avgHumidityAfternoon / count)),
                new DailyAggregationDTO.Precipitation((int) (avgPrecipitationTotal / count)),
                new DailyAggregationDTO.Pressure(avgPressureAfternoon / count),
                new DailyAggregationDTO.Temperature(
                        avgTempMin / count, avgTempMax / count, avgTempAfternoon / count, avgTempNight / count, avgTempEvening / count, avgTempMorning / count),
                new DailyAggregationDTO.Wind(new DailyAggregationDTO.Wind.Max(avgWindSpeed / count, avgWindDirection / count))
        );
    }
}
