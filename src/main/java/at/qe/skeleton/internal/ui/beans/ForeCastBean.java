package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Managed bean for handling forecast information on the weather page.
 */
@Component
@Scope("view")
public class ForeCastBean {

    @Autowired
    WeatherBean weatherBean;

    @Autowired
    SessionInfoBean sessionInfoBean;

    /**
     * Returns the URL for the weather icon based on the provided icon code.
     *
     * @param icon The icon code for the weather.
     * @return The URL for the weather icon.
     */
    public String getImageUrl(String icon){
        return "https://openweathermap.org/img/wn/" + icon + "@2x.png";
    }

    /**
     * Retrieves the daily weather forecast based on a user's premium status.
     *
     * @return The list of DailyWeatherDTO representing the daily weather forecast.
     */
    public List<DailyWeatherDTO> getDailyWeather() {
        if (sessionInfoBean.isLoggedIn() && sessionInfoBean.isPremium()){
            return weatherBean.getWeather().dailyWeather();
        } else {
            return weatherBean.getWeather().dailyWeather().subList(0, 4);
        }
    }

    /**
     * Returns the title for the forecast section based on user's premium status.
     *
     * @return The title for the forecast section.
     */
    public String getTitle() {
        return sessionInfoBean.isLoggedIn() && sessionInfoBean.isPremium() ? "8-Day-Forecast" : "3-Day-Forecast";
    }
}
