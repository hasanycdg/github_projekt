package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO;
import at.qe.skeleton.internal.services.WeatherService;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Managed bean for creating charts based on daily weather data.
 */
@Component
@Scope("view")
public class DailyWeatherChartBean implements Serializable {

    @Autowired
    public WeatherService weatherService;

    /**
     * Creates a bar chart model for daily temperature at different times of the day.
     *
     * @param dailyWeatherDTO The DailyAggregationDTO containing daily temperature data.
     * @return The BarChartModel for daily temperature.
     */
    public BarChartModel createDailyTemperatureChart(DailyAggregationDTO dailyWeatherDTO) {
        BarChartModel dailyTemperatureChart = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet temperatureDataSet = new BarChartDataSet();
        temperatureDataSet.setLabel("Temperature (°C)");
        temperatureDataSet.setBackgroundColor("rgba(75, 192, 192, 0.2)");
        temperatureDataSet.setBorderColor("rgb(75, 192, 192)");

        List<Number> temperatureData = new ArrayList<>();
        temperatureData.add(dailyWeatherDTO.temperature().morning());
        temperatureData.add(dailyWeatherDTO.temperature().afternoon());
        temperatureData.add(dailyWeatherDTO.temperature().evening());
        temperatureData.add(dailyWeatherDTO.temperature().night());

        temperatureDataSet.setData(temperatureData);

        data.addChartDataSet(temperatureDataSet);

        // Labels for different times of the day
        List<String> labels = new ArrayList<>();
        labels.add("Morning");
        labels.add("Afternoon");
        labels.add("Evening");
        labels.add("Night");

        data.setLabels(labels);
        dailyTemperatureChart.setData(data);

        // Chart options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
        dailyTemperatureChart.setOptions(options);

        return dailyTemperatureChart;
    }


}