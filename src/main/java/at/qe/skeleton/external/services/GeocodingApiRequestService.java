package at.qe.skeleton.external.services;

import at.qe.skeleton.external.exceptions.GeocodingApiException;
import at.qe.skeleton.external.model.geocoding.GeocodingDTO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Scope("application")
@Component
@Validated
public class GeocodingApiRequestService implements Serializable {

    private static final String GEOCODING_URI = "/geo/1.0/direct";
    private static final String CITY_PARAMETER = "q";
    private static final String LIMIT_PARAMETER = "limit";

    @Autowired
    private RestClient restClient;

    public List<GeocodingDTO> retrieveGeocodingData(String city) {
        String encodedCity = encodeCity(city);
        String apiUrl = buildApiUrl(encodedCity);

        ResponseEntity<List<GeocodingDTO>> responseEntity = performApiCall(apiUrl);

        // error handling
        handleApiResponse(responseEntity);

        return responseEntity.getBody();
    }

    private String encodeCity(String city) {
        return URLEncoder.encode(city, StandardCharsets.UTF_8);
    }

    public String buildApiUrl(String encodedCity) {
        return UriComponentsBuilder.fromPath(GEOCODING_URI)
                .queryParam(CITY_PARAMETER, encodedCity)
                .queryParam(LIMIT_PARAMETER, 5)
                .build().toUriString();
    }

    private ResponseEntity<List<GeocodingDTO>> performApiCall(String apiUrl) {
        return restClient.get()
                .uri(apiUrl)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<GeocodingDTO>>() {});
    }

    public void handleApiResponse(ResponseEntity<List<GeocodingDTO>> responseEntity) {
        if (responseEntity.getStatusCode().isError()) {
            //add message to inform user about the specific api error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while retrieving the weather. Status code: "
                    + responseEntity.getStatusCode(), null));
            throw new GeocodingApiException("Error while retrieving geocoding data. Status code: "
                    + responseEntity.getStatusCode());
        }
    }
}
