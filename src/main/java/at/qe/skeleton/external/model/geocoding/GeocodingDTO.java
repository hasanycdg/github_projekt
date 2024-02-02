package at.qe.skeleton.external.model.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a Data Transfer Object (DTO) for geocoding information.
 * This record is used to store data obtained from geocoding services.
 *
 * @param name       The name of the location.
 * @param localNames A map containing local names of the location in different languages.
 * @param lat        The latitude coordinate of the location.
 * @param lon        The longitude coordinate of the location.
 * @param country    The country in which the location is situated.
 * @param state      The state or region within the country (if applicable).
 */
public record GeocodingDTO(
        @JsonProperty("name") String name,
        @JsonProperty("local_names") Map<String, String> localNames,
        @JsonProperty("lat") double lat,
        @JsonProperty("lon") double lon,
        @JsonProperty("country") String country,
        @JsonProperty("state") String state
) implements Serializable {
    // No need to add getters and setters with records
}

