package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyTemperatureAggregationDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.TemperatureAggregationDTO;
import at.qe.skeleton.internal.ui.beans.ChartBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primefaces.model.charts.bar.BarChartModel;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChartBeanTest {

    @Mock
    private DailyWeatherDTO dailyWeatherDTO;

    @InjectMocks
    private ChartBean chartBean;

    @Test
    public void testCreateStackedBarModel() {
        // Mock data for dailyWeatherDTO
        DailyTemperatureAggregationDTO temperatureAggregationMock = mock(DailyTemperatureAggregationDTO.class);
        TemperatureAggregationDTO feelsLikeTemperatureAggregationMock = mock(TemperatureAggregationDTO.class);
        when(dailyWeatherDTO.dailyTemperatureAggregation()).thenReturn(temperatureAggregationMock);
        when(dailyWeatherDTO.feelsLikeTemperatureAggregation()).thenReturn(feelsLikeTemperatureAggregationMock);
        when(dailyWeatherDTO.dailyTemperatureAggregation().morningTemperature()).thenReturn(20.0);
        when(dailyWeatherDTO.dailyTemperatureAggregation().dayTemperature()).thenReturn(25.0);
        when(dailyWeatherDTO.dailyTemperatureAggregation().eveningTemperature()).thenReturn(22.0);
        when(dailyWeatherDTO.dailyTemperatureAggregation().nightTemperature()).thenReturn(18.0);
        when(dailyWeatherDTO.feelsLikeTemperatureAggregation().morningTemperature()).thenReturn(18.0);
        when(dailyWeatherDTO.feelsLikeTemperatureAggregation().dayTemperature()).thenReturn(23.0);
        when(dailyWeatherDTO.feelsLikeTemperatureAggregation().eveningTemperature()).thenReturn(20.0);
        when(dailyWeatherDTO.feelsLikeTemperatureAggregation().nightTemperature()).thenReturn(16.0);

        // Call the method
        BarChartModel stackedBarModel = chartBean.createStackedBarModel(dailyWeatherDTO);

        // verify the results
        assert stackedBarModel != null;

        // called 8-times each -> all data inserted correctly
        verify(dailyWeatherDTO, times(8)).dailyTemperatureAggregation();
        verify(dailyWeatherDTO, times(8)).feelsLikeTemperatureAggregation();
    }

    @Test
    public void testCreateMixedModel() {
        // Mock data for hourlyWeatherDTOList
        List<HourlyWeatherDTO> hourlyWeatherDTOList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            HourlyWeatherDTO hourlyWeatherDTOMock = mock(HourlyWeatherDTO.class);
            when(hourlyWeatherDTOMock.rain()).thenReturn(0.0); // Mock rain value as needed
            when(hourlyWeatherDTOMock.temperature()).thenReturn(25.0); // Mock temperature value as needed
            when(hourlyWeatherDTOMock.timestamp()).thenReturn(Instant.ofEpochSecond(Instant.now().plus(Duration.ofHours(i)).toEpochMilli())); // Mock timestamp value as needed

            hourlyWeatherDTOList.add(hourlyWeatherDTOMock);
        }

        // Call the method
        BarChartModel mixedModel = chartBean.createMixedModel(hourlyWeatherDTOList, 0);

        // Verify the result or any interactions you want to test
        // For simplicity, you can check if the chart model is not null or other basic assertions
        assert mixedModel != null;

        // Verify method calls on mocked HourlyWeatherDTO instances
        for (int i = 0; i < 24; i++) {
            verify(hourlyWeatherDTOList.get(i)).temperature();
            verify(hourlyWeatherDTOList.get(i)).timestamp();
        }
    }

}

