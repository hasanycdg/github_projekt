package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.services.FavLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
@Scope("session")
public class ConfigureFavListBean implements Serializable {
    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    private FavLocationService favLocationService;
    private FavLocation favLocation;
    private Boolean showCurrentSunrise;
    private Boolean showCurrentSunset;
    private Boolean showCurrentTemp = true;
    private Boolean showCurrentFeelsLike = true;
    private Boolean showCurrentPressure;
    private Boolean showCurrentHumidity;
    private Boolean showCurrentDewPoint;
    private Boolean showCurrentClouds;
    private Boolean showCurrentUvi;
    private Boolean showCurrentVisibility;
    private Boolean showCurrentWindSpeed;
    private Boolean showCurrentWindDeg;
    private Boolean showCurrentRain;
    private Boolean showCurrentSnow;
    private Boolean showWeatherIcon = true;
    private Boolean showWeatherMain;

    public FavLocation getFavLocation() {
        return favLocation;
    }

    public void setFavLocation(FavLocation favLocation) {
        this.favLocation = favLocation;
    }

    public CurrentWeatherDTO getCurrentWeather(FavLocation favLocation){
        return weatherApiRequestService.retrieveCurrentAndForecastWeather
                (favLocation.getLatitude(), favLocation.getLongitude()).currentWeather();
    }

    public String getCurrentSunriseTime(FavLocation favLocation){
        return "Sunrise time: " + getCurrentWeather(favLocation).sunrise().toString().subSequence(11,16) + " o'clock";
    }

    public String getCurrentSunsetTime(FavLocation favLocation){
        return "Sunset time: " + getCurrentWeather(favLocation).sunset().toString().subSequence(11,16) + " o'clock";
    }

    public String getCurrentTemperature(FavLocation favLocation){
        return "Current temperature: " + getCurrentWeather(favLocation).temperature() + "°C";
    }

    public String getFeelsLikeTemp(FavLocation favLocation){
        return "Feels like: " + getCurrentWeather(favLocation).feelsLikeTemperature() + "°C";
    }

    public String getCurrentHumidity(FavLocation favLocation){
        return "Current humidity: " + getCurrentWeather(favLocation).humidity() + "%";
    }

    public String getCurrentDewPoint(FavLocation favLocation){
        return "Current dew point: " + getCurrentWeather(favLocation).dewPoint() + "°C";
    }

    public String getCurrentPressure(FavLocation favLocation){
        return "Current pressure: " + getCurrentWeather(favLocation).pressure() + " hPa";
    }

    public String getCurrentClouds(FavLocation favLocation){
        return "Current clouds: " + getCurrentWeather(favLocation).clouds() + "%";
    }

    public String getCurrentUvi(FavLocation favLocation){
        return "Current UV index: " + getCurrentWeather(favLocation).uvi();
    }

    public String getCurrentVisibility(FavLocation favLocation){
        return "Current visibility: " + getCurrentWeather(favLocation).visibility() + " meters";
    }

    public String getCurrentWindSpeed(FavLocation favLocation){
        return "Current wind speed: " + getCurrentWeather(favLocation).windSpeed() + " m/s";
    }

    public String getCurrentWindDeg(FavLocation favLocation){
        return "Current wind degree: " + getCurrentWeather(favLocation).windDirection() + "°";
    }

    public String getCurrentRainVolume(FavLocation favLocation){
        return "Current rain: " + getCurrentWeather(favLocation).rain() + " mm";
    }

    public String getCurrentSnowVolume(FavLocation favLocation){
        return "Current snow: " + getCurrentWeather(favLocation).snow() + " mm";
    }

    public Boolean getShowCurrentSunrise() {
        return showCurrentSunrise;
    }

    public void setShowCurrentSunrise(Boolean showCurrentSunrise) {
        this.showCurrentSunrise = showCurrentSunrise;
    }

    public Boolean getShowCurrentSunset() {
        return showCurrentSunset;
    }

    public void setShowCurrentSunset(Boolean showCurrentSunset) {
        this.showCurrentSunset = showCurrentSunset;
    }

    public Boolean getShowCurrentTemp() {
        return showCurrentTemp;
    }

    public void setShowCurrentTemp(Boolean showCurrentTemp) {
        this.showCurrentTemp = showCurrentTemp;
    }

    public Boolean getShowCurrentFeelsLike() {
        return showCurrentFeelsLike;
    }

    public void setShowCurrentFeelsLike(Boolean showCurrentFeelsLike) {
        this.showCurrentFeelsLike = showCurrentFeelsLike;
    }

    public Boolean getShowCurrentPressure() {
        return showCurrentPressure;
    }

    public void setShowCurrentPressure(Boolean showCurrentPressure) {
        this.showCurrentPressure = showCurrentPressure;
    }

    public Boolean getShowCurrentHumidity() {
        return showCurrentHumidity;
    }

    public void setShowCurrentHumidity(Boolean showCurrentHumidity) {
        this.showCurrentHumidity = showCurrentHumidity;
    }

    public Boolean getShowCurrentDewPoint() {
        return showCurrentDewPoint;
    }

    public void setShowCurrentDewPoint(Boolean showCurrentDewPoint) {
        this.showCurrentDewPoint = showCurrentDewPoint;
    }

    public Boolean getShowCurrentClouds() {
        return showCurrentClouds;
    }

    public void setShowCurrentClouds(Boolean showCurrentClouds) {
        this.showCurrentClouds = showCurrentClouds;
    }

    public Boolean getShowCurrentUvi() {
        return showCurrentUvi;
    }

    public void setShowCurrentUvi(Boolean showCurrentUvi) {
        this.showCurrentUvi = showCurrentUvi;
    }

    public Boolean getShowCurrentVisibility() {
        return showCurrentVisibility;
    }

    public void setShowCurrentVisibility(Boolean showCurrentVisibility) {
        this.showCurrentVisibility = showCurrentVisibility;
    }

    public Boolean getShowCurrentWindSpeed() {
        return showCurrentWindSpeed;
    }

    public void setShowCurrentWindSpeed(Boolean showCurrentWindSpeed) {
        this.showCurrentWindSpeed = showCurrentWindSpeed;
    }

    public Boolean getShowCurrentWindDeg() {
        return showCurrentWindDeg;
    }

    public void setShowCurrentWindDeg(Boolean showCurrentWindDeg) {
        this.showCurrentWindDeg = showCurrentWindDeg;
    }

    public Boolean getShowCurrentRain() {
        return showCurrentRain;
    }

    public void setShowCurrentRain(Boolean showCurrentRain) {
        this.showCurrentRain = showCurrentRain;
    }

    public Boolean getShowCurrentSnow() {
        return showCurrentSnow;
    }

    public void setShowCurrentSnow(Boolean showCurrentSnow) {
        this.showCurrentSnow = showCurrentSnow;
    }

    public Boolean getShowWeatherIcon() {
        return showWeatherIcon;
    }

    public void setShowWeatherIcon(Boolean showWeatherIcon) {
        this.showWeatherIcon = showWeatherIcon;
    }

    public Boolean getShowWeatherMain() {
        return showWeatherMain;
    }

    public void setShowWeatherMain(Boolean showWeatherMain) {
        this.showWeatherMain = showWeatherMain;
    }
}
