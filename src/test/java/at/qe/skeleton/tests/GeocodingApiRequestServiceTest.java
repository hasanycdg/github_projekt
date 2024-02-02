package at.qe.skeleton.tests;

import at.qe.skeleton.external.exceptions.GeocodingApiException;
import at.qe.skeleton.external.model.geocoding.GeocodingDTO;
import at.qe.skeleton.external.services.GeocodingApiRequestService;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GeocodingApiRequestServiceTest {

    @Autowired
    private GeocodingApiRequestService geocodingApiRequestService;

    @BeforeAll
    public static void setUp() {
        FacesContext context = ContextMocker.mockFacesContext();
    }

    @Test
    void buildApiUrl_CorrectParameters_ReturnsApiUrl() {
        // Arrange
        String city = "Vienna";
        String expectedEncodedCity = UriComponentsBuilder.fromPath("/geo/1.0/direct")
                .queryParam("q", "Vienna")
                .queryParam("limit", 5)
                .build().toUriString();

        // Act
        String result = geocodingApiRequestService.buildApiUrl(city);

        // Assert
        assertEquals(expectedEncodedCity, result);
    }

    @Test
    void handleApiResponse_SuccessfulResponse_DoesNotThrowException() {
        // Arrange
        ResponseEntity<List<GeocodingDTO>> successResponseEntity = new ResponseEntity<>(HttpStatus.OK);

        // Act & Assert
        assertDoesNotThrow(() -> geocodingApiRequestService.handleApiResponse(successResponseEntity));
    }

    @Test
    void handleApiResponse_ErrorResponse_ThrowsWeatherApiException() {
        // Arrange
        ResponseEntity<List<GeocodingDTO>> errorResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Act & Assert
        GeocodingApiException exception = assertThrows(GeocodingApiException.class, () -> geocodingApiRequestService.handleApiResponse(errorResponseEntity));

        assertEquals("Error while retrieving geocoding data. Status code: " + HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
