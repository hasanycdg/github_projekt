package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.currentandforecast.DailyAggregationDTO;
import at.qe.skeleton.internal.ui.beans.DailyWeatherChartBean;
import at.qe.skeleton.internal.services.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartDataSet;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

class DailyWeatherChartBeanTest {

    @Test
    void testCreateDailyTemperatureChart() {
        // Mock the WeatherService
        WeatherService mockWeatherService = Mockito.mock(WeatherService.class);

        // Create instance of DailyWeatherChartBean and inject mock service
        DailyWeatherChartBean chartBean = new DailyWeatherChartBean();
        chartBean.weatherService = mockWeatherService;

        DailyAggregationDTO mockDTO = Mockito.mock(DailyAggregationDTO.class);
        DailyAggregationDTO.Temperature mockTemperature = Mockito.mock(DailyAggregationDTO.Temperature.class);

        // Setup mock behavior for temperature values
        when(mockTemperature.morning()).thenReturn(10.0);
        when(mockTemperature.afternoon()).thenReturn(15.0);
        when(mockTemperature.evening()).thenReturn(12.0);
        when(mockTemperature.night()).thenReturn(8.0);

        // Setup mock DTO to return the mock Temperature
        when(mockDTO.temperature()).thenReturn(mockTemperature);

        // Call the method under test
        BarChartModel chartModel = chartBean.createDailyTemperatureChart(mockDTO);

        // Assert that a BarChartModel is returned
        Assertions.assertNotNull(chartModel, "The returned BarChartModel should not be null");

        // Assert the dataset values
        BarChartDataSet dataSet = (BarChartDataSet) chartModel.getData().getDataSet().get(0);
        List<Number> expectedTemperatures = Arrays.asList(10.0, 15.0, 12.0, 8.0);
        Assertions.assertEquals(expectedTemperatures, dataSet.getData(), "The dataset temperatures should match expected values");

        // Assert chart labels
        List<String> expectedLabels = Arrays.asList("Morning", "Afternoon", "Evening", "Night");
        Assertions.assertEquals(expectedLabels, chartModel.getData().getLabels(), "The chart labels should match expected values");

        // Additional assertions can be added for chart options and scales
    }
}
