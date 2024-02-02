package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Main bean for handling all functionality with the weather page.
 * Primary function is to retrieve the weather information from the API.
 */
@Component
@Scope("view")
public class WeatherBean {

    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    private AutocompleteBean autocompleteBean;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherBean.class);
    private double latitude;
    private double longitude;
    private String location; //same as displayName shown in autocomplete dropdown

    private Boolean buttonPressed = false;

    private CurrentAndForecastAnswerDTO weather;

    /**
     * Gets the image URL for the current weather icon.
     *
     * @return The image URL.
     */
    public String imageUrl() {
        return "https://openweathermap.org/img/wn/" + weather.currentWeather().weather().icon() + "@2x.png";
    }

    /**
     * Searches for weather based on the selected location.
     */
    public void searchWeather() {
        //alert the user if no weather is selected and never has been in this session
        if(autocompleteBean.getSelectedGeocodingDTO() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_ERROR, "Error", "Please select a location from the dropdown!"));
            return;
        }

        buttonPressed = true;

        latitude = autocompleteBean.getSelectedGeocodingDTO().lat();
        longitude = autocompleteBean.getSelectedGeocodingDTO().lon();
        location = autocompleteBean.getDisplayName(autocompleteBean.getSelectedGeocodingDTO());


        try {
            weather = this.weatherApiRequestService.retrieveCurrentAndForecastWeather(getLatitude(), getLongitude());
        } catch (final Exception e) {
            LOGGER.error("error in request", e);
        }
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CurrentAndForecastAnswerDTO getWeather() {
        return weather;
    }

    public void setWeather(CurrentAndForecastAnswerDTO weather) {
        this.weather = weather;
    }

    public Boolean getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(Boolean buttonPressed) {
        this.buttonPressed = buttonPressed;
    }
}
