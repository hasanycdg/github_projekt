package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Managed bean for handling hourly weather information on the weather page.
 */
@Component
@Scope("view")
public class HourlyWeatherBean {
    @Autowired
    SessionInfoBean sessionInfoBean;

    @Autowired
    WeatherBean weatherBean;

    /**
     * Returns the title for the hourly weather section based on user's premium status.
     *
     * @return The title for the hourly weather section.
     */
    public String getTitle() {
        return sessionInfoBean.isLoggedIn() && sessionInfoBean.isPremium() ? "Next 48h Details" : "Next 24h Details";
    }

    /**
     * Retrieves the hourly weather details based on a user's premium status.
     *
     * @return The list of HourlyWeatherDTO representing the hourly weather details.
     */
    public List<HourlyWeatherDTO> getHourlyWeather() {
        if (sessionInfoBean.isLoggedIn() && sessionInfoBean.isPremium()){
            return weatherBean.getWeather().hourlyWeather().subList(0, 48);
        } else {
            return weatherBean.getWeather().hourlyWeather().subList(0, 24);
        }
    }
}
