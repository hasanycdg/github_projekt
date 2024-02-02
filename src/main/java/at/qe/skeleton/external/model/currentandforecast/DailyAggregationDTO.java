package at.qe.skeleton.external.model.currentandforecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a daily aggregation of various weather parameters at a specific geographic location.
 * Includes details such as cloud cover, humidity, precipitation, pressure, temperature, and wind conditions.
 */
public record DailyAggregationDTO(
        @JsonProperty("lat") double latitude, // Latitude of the location
        @JsonProperty("lon") double longitude, // Longitude of the location
        @JsonProperty("timezone") String timezone, // Timezone of the location
        @JsonProperty("date") LocalDate date, // Date of the weather data
        @JsonProperty("units") String units, // Units of the measurements
        @JsonProperty("cloud_cover") CloudCover cloudCover, // Cloud cover details
        @JsonProperty("humidity") Humidity humidity, // Humidity details
        @JsonProperty("precipitation") Precipitation precipitation, // Precipitation details
        @JsonProperty("pressure") Pressure pressure, // Atmospheric pressure details
        @JsonProperty("temperature") Temperature temperature, // Temperature details
        @JsonProperty("wind") Wind wind // Wind details
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Represents the cloud cover details for a specific time of day.
     */
    public record CloudCover(@JsonProperty("afternoon") int afternoon) {}

    /**
     * Represents the humidity details for a specific time of day.
     */
    public record Humidity(@JsonProperty("afternoon") int afternoon) {}

    /**
     * Represents the total precipitation.
     */
    public record Precipitation(@JsonProperty("total") double total) {}

    /**
     * Represents the atmospheric pressure details for a specific time of day.
     */
    public record Pressure(@JsonProperty("afternoon") double afternoon) {}

    /**
     * Represents the temperature details at different times of the day.
     */
    public record Temperature(
            @JsonProperty("min") double min, // Minimum temperature
            @JsonProperty("max") double max, // Maximum temperature
            @JsonProperty("afternoon") double afternoon, // Afternoon temperature
            @JsonProperty("night") double night, // Night temperature
            @JsonProperty("evening") double evening, // Evening temperature
            @JsonProperty("morning") double morning // Morning temperature
    ) {}

    /**
     * Represents the wind conditions including maximum speed and direction.
     */
    public record Wind(@JsonProperty("max") Max max) {
        /**
         * Represents the maximum wind conditions including speed and direction.
         */
        public record Max(
                @JsonProperty("speed") double speed, // Maximum wind speed
                @JsonProperty("direction") double direction // Wind direction
        ) {}
    }
}
