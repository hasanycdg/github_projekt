package at.qe.skeleton.external.services;

import at.qe.skeleton.external.exceptions.WeatherApiException;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Service class for making requests to a weather API.
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University. It handles the communication
 * with the weather API to retrieve current and forecasted weather data, as well as daily weather aggregation.
 */
@Scope("application")
@Component
@Validated // makes sure the parameter validation annotations are checked during runtime
public class WeatherApiRequestService implements Serializable {

    private static final String CURRENT_AND_FORECAST_URI = "/data/3.0/onecall";
    private static final String DAILY_AGGREGATION_URI = "/data/3.0/onecall/day_summary";

    private static final String LONGITUDE_PARAMETER = "lon";
    private static final String LATITUDE_PARAMETER = "lat";

    @Autowired
    private RestClient restClient;

    /**
     * Makes an API call to get the current weather and a forecast for a specified location.
     * Use the geocoding API to determine latitude and longitude if they are unknown.
     *
     * @param latitude  Latitude of the location, must be between -90 and 90.
     * @param longitude Longitude of the location, must be between -180 and 180.
     * @return An instance of {@link CurrentAndForecastAnswerDTO} containing current and forecasted weather data.
     * @throws WeatherApiException If there is an error in retrieving data from the API.
     */
    public CurrentAndForecastAnswerDTO retrieveCurrentAndForecastWeather(@Min(-90) @Max(90) double latitude,
                                                                         @Min(-180) @Max(180) double longitude) {
        ResponseEntity<CurrentAndForecastAnswerDTO> responseEntity = this.restClient.get()
                .uri(UriComponentsBuilder.fromPath(CURRENT_AND_FORECAST_URI)
                        .queryParam(LATITUDE_PARAMETER, String.valueOf(latitude))
                        .queryParam(LONGITUDE_PARAMETER, String.valueOf(longitude))
                        .build().toUriString())
                .retrieve()
                .toEntity(CurrentAndForecastAnswerDTO.class);

        if (responseEntity.getStatusCode().isError()) {
            throw new WeatherApiException("Error while retrieving current and forecast weather. Status code: "
                    + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    /**
     * Retrieves daily aggregated weather data for a specific date and location.
     *
     * @param latitude  Latitude of the location, must be between -90 and 90.
     * @param longitude Longitude of the location, must be between -180 and 180.
     * @param date      The date for which to retrieve weather data.
     * @return An instance of {@link DailyAggregationDTO} containing aggregated weather data for the specified date.
     */
    public DailyAggregationDTO retrieveDailyAggregationWeather(@Min(-90) @Max(90) double latitude,
                                                               @Min(-180) @Max(180) double longitude,
                                                               LocalDate date) {
        ResponseEntity<DailyAggregationDTO> responseEntity = this.restClient.get()
                .uri(UriComponentsBuilder.fromPath(DAILY_AGGREGATION_URI)
                        .queryParam(LATITUDE_PARAMETER, String.valueOf(latitude))
                        .queryParam(LONGITUDE_PARAMETER, String.valueOf(longitude))
                        .queryParam("date", date.toString())
                        .build().toUriString())
                .retrieve()
                .toEntity(DailyAggregationDTO.class);
        if (responseEntity.getStatusCode().isError()) {
            //add message to inform user about the specific api error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while retrieving the weather. Status code: "
                    + responseEntity.getStatusCode(), null));

            throw new WeatherApiException("Error while retrieving the weather. Status code: "
                    + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }
}
