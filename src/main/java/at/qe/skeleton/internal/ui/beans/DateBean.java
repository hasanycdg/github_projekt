package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO;
import at.qe.skeleton.external.model.geocoding.GeocodingDTO;
import at.qe.skeleton.external.services.GeocodingApiRequestService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.internal.services.DateRangeService;
import at.qe.skeleton.internal.services.WeatherService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static at.qe.skeleton.internal.services.DateRangeService.calculateMidpointDate;

/**
 * Managed bean for handling date-related operations within a user session.
 * This bean is responsible for managing start and end dates for weather data retrieval,
 * invoking weather and geocoding service calls, and maintaining the state of these operations.
 */
@Component
@Scope("session")
public class DateBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateBean.class);

    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    public
    SessionInfoBean sessionInfoBean;
    @Autowired
    private GeocodingApiRequestService geocodingApiRequestService;
    @Autowired
    private AutocompleteBean autocompleteBean;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DailyAggregationDTO> weatherDataList = new ArrayList<>();
    @Autowired
    private WeatherBean weatherBean;
    private Boolean buttonPressed = false;
    private DailyAggregationDTO averageWeatherData;
    @Autowired
    private DateRangeService dateRangeService;
    @Autowired
    private WeatherService weatherService;
    /**
     * Fetches and displays the average weather data.
     * This method is intended for UI interaction, and the result can be used for display purposes.
     *
     * @return A navigation outcome as a String, can be null. It indicates the result of the operation for UI purposes.
     */

    public DailyAggregationDTO fetchAverageWeatherData() {
        LocalDate midpointDate = DateRangeService.calculateMidpointDate(startDate, endDate);
        if (midpointDate == null) {
            LOGGER.error("Midpoint date calculation error.");
            return null;
        }

        List<DailyAggregationDTO> pastWeatherData = new ArrayList<>();
        GeocodingDTO geocodingData1 = autocompleteBean.getSelectedGeocodingDTO();
        double latitude = geocodingData1.lat();
        double longitude = geocodingData1.lon();

        for (int i = 0; i < 5; i++) {
            LocalDate dateToCheck = midpointDate.minusYears(i);

            try {
                DailyAggregationDTO yearlyWeather = this.weatherApiRequestService.retrieveDailyAggregationWeather(latitude, longitude, dateToCheck);
                pastWeatherData.add(yearlyWeather);
            } catch (Exception e) {
                LOGGER.error("Error retrieving weather data for date: " + dateToCheck, e);
            }
        }

        averageWeatherData = WeatherService.calculateAverageWeather(pastWeatherData);
        return averageWeatherData;
    }

    /**
     * Handles the submission of selected dates.
     * Validates the date range and retrieves weather data for the specified period and location.
     *
     * @return A string indicating the outcome of the operation, such as "success" or "error".
     */
    public String submitDates() {
        buttonPressed = true;
        if (!DateRangeService.isDateRangeValid(startDate, endDate)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid date range.\nThe range should not exceed 14 days.", "Invalid date range. Start date must be before end date, and the range should not exceed 14 days."));
            return "error";
        }

        GeocodingDTO geocodingData = autocompleteBean.getSelectedGeocodingDTO();
        if (geocodingData == null) {
            LOGGER.error("No location selected.");
            return "error"; // Return an appropriate outcome for no location selected
        }

        double latitude = geocodingData.lat();
        double longitude = geocodingData.lon();

        weatherDataList = weatherService.retrieveWeatherDataForRange(startDate, endDate, latitude, longitude);

        // Calculate average weather data
        averageWeatherData = weatherService.calculateAverageWeatherData(calculateMidpointDate(startDate, endDate), latitude, longitude);

        return "success"; // Return the appropriate outcome for success
    }




    /**
*Getters and setters for all the variables in this class.
*/

    public SessionInfoBean getSessionInfoBean() {
        return sessionInfoBean;
    }

    public void setSessionInfoBean(SessionInfoBean sessionInfoBean) {
        this.sessionInfoBean = sessionInfoBean;
    }

    public DailyAggregationDTO getAverageWeatherData() {
        return averageWeatherData;
    }

    public void setAverageWeatherData(DailyAggregationDTO averageWeatherData) {
        this.averageWeatherData = averageWeatherData;
    }

    public Boolean getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(Boolean buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    // Getters and Setters for start and end dates
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public List<DailyAggregationDTO> getWeatherDataList() {
        return weatherDataList;
    }

    public void setWeatherDataList(List<DailyAggregationDTO> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

}
