package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.*;

/**
 * Managed bean for creating charts used on the weather page.
 */
@Component
@Scope("view")
public class ChartBean implements Serializable {

    /**
     * Creates a stacked bar chart model for daily temperature and feels-like temperature.
     *
     * @param dailyWeatherDTO The DailyWeatherDTO containing temperature data.
     * @return The BarChartModel for the stacked bar chart.
     */
    public BarChartModel createStackedBarModel(DailyWeatherDTO dailyWeatherDTO) {
        BarChartModel stackedBarModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet temperatureDataSet = new BarChartDataSet();
        temperatureDataSet.setLabel("Temperature");
        temperatureDataSet.setBackgroundColor("rgb(149, 167, 196)");
        List<Number> temperatureData = new ArrayList<>();
        temperatureData.add(dailyWeatherDTO.dailyTemperatureAggregation().morningTemperature());
        temperatureData.add(dailyWeatherDTO.dailyTemperatureAggregation().dayTemperature());
        temperatureData.add(dailyWeatherDTO.dailyTemperatureAggregation().eveningTemperature());
        temperatureData.add(dailyWeatherDTO.dailyTemperatureAggregation().nightTemperature());

        temperatureDataSet.setData(temperatureData);

        BarChartDataSet feelsLikeDataSet = new BarChartDataSet();
        feelsLikeDataSet.setLabel("Feels Like");
        feelsLikeDataSet.setBackgroundColor("rgb(114, 145, 196)");
        List<Number> feelsLikeData = new ArrayList<>();
        feelsLikeData.add(dailyWeatherDTO.feelsLikeTemperatureAggregation().morningTemperature());
        feelsLikeData.add(dailyWeatherDTO.feelsLikeTemperatureAggregation().dayTemperature());
        feelsLikeData.add(dailyWeatherDTO.feelsLikeTemperatureAggregation().eveningTemperature());
        feelsLikeData.add(dailyWeatherDTO.feelsLikeTemperatureAggregation().nightTemperature());
        feelsLikeDataSet.setData(feelsLikeData);

        data.addChartDataSet(temperatureDataSet);
        data.addChartDataSet(feelsLikeDataSet);

        List<String> labels = Arrays.asList("Morning", "Afternoon", "Evening", "Night");
        data.setLabels(labels);
        stackedBarModel.setData(data);

        // Options
        BarChartOptions options = new BarChartOptions();
        options.setMaintainAspectRatio(false);

        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setStacked(false);
        linearAxes.setOffset(true);
        cScales.addXAxesData(linearAxes);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltip(tooltip);

        stackedBarModel.setOptions(options);

        return stackedBarModel;
    }

    /**
     * Creates a mixed bar/line chart model for hourly rain and temperature.
     *
     * @param hourlyWeatherDTO List of HourlyWeatherDTO containing rain and temperature data.
     * @param offset           The offset for timestamp conversion.
     * @return The BarChartModel for the mixed bar/line chart.
     */
    public BarChartModel createMixedModel(List<HourlyWeatherDTO> hourlyWeatherDTO, int offset) {
        BarChartModel mixedModel = new BarChartModel();
        ChartData data = new ChartData();

        // Bar Dataset for Rain
        BarChartDataSet barDataSet = new BarChartDataSet();
        List<Number> rainValues = new ArrayList<>();
        for (int i=0; i<24; i++) {
            rainValues.add(Objects.isNull(hourlyWeatherDTO.get(i).rain()) ? 0 : hourlyWeatherDTO.get(i).rain());
        }
        barDataSet.setData(rainValues);
        barDataSet.setLabel("Rain (mm)");
        barDataSet.setBackgroundColor("rgba(75, 192, 192, 0.2)");
        barDataSet.setBorderColor("rgb(75, 192, 192)");

        // Line Dataset for Temperature
        LineChartDataSet lineDataSet = new LineChartDataSet();
        List<Object> temperatureValues = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            temperatureValues.add(hourlyWeatherDTO.get(i).temperature());
        }
        lineDataSet.setData(temperatureValues);
        lineDataSet.setLabel("Temperature (°C)");
        lineDataSet.setFill(false);
        lineDataSet.setBorderColor("rgb(255, 99, 132)");

        data.addChartDataSet(barDataSet);
        data.addChartDataSet(lineDataSet);

        // Labels for the next 24 hours
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            labels.add(ApiFormatter.getTimeFormattedFromTimestamp(hourlyWeatherDTO.get(i).timestamp(), offset));
        }
        data.setLabels(labels);

        mixedModel.setData(data);


        // Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);



        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
        mixedModel.setOptions(options);
        mixedModel.setExtender("chartExtender");
        return mixedModel;
    }
}
