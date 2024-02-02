package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.geocoding.GeocodingDTO;
import at.qe.skeleton.external.services.GeocodingApiRequestService;
import at.qe.skeleton.internal.ui.beans.AutocompleteBean;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.Behavior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primefaces.event.SelectEvent;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutocompleteBeanTest {

    @Mock
    private GeocodingApiRequestService geocodingApiRequestService;

    @InjectMocks
    private AutocompleteBean autocompleteBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAutocompletion() {
        // Mock the geocoding service to return a list of suggestions as we shouldn't call a real api here
        List<GeocodingDTO> mockSuggestions = Arrays.asList(
                new GeocodingDTO("Place1", null, 22.7, 77.0, "country", "state"),
                new GeocodingDTO("Place2", null, 25.9, 32.1, "country", "state")
        );
        when(geocodingApiRequestService.retrieveGeocodingData(anyString())).thenReturn(mockSuggestions);

        // Call the getAutocompletion method
        List<GeocodingDTO> autocompletions = autocompleteBean.getAutocompletion("input");

        // Verify that the suggestions are returned
        assertNotNull(autocompletions);
        assertEquals(2, autocompletions.size());

        // Verify that current suggestions are saved in the map
        assertEquals(2, autocompleteBean.getCurrentFiveSuggestedDTOs().size());

        // reset mocks
        reset(geocodingApiRequestService);
    }

    @Test
    void testOnItemSelect() {
        // Prepare a sample GeocodingDTO
        GeocodingDTO sampleDTO = new GeocodingDTO("Place1", null, 22.7, 77.0, "country", "state");

        // Set up the AutocompleteBean with the sample DTO
        autocompleteBean.getCurrentFiveSuggestedDTOs().put(sampleDTO.toString(), sampleDTO);

        // Create a SelectEvent with the DTO's string representation
        UIComponent uiComponent = mock(UIComponent.class);
        Behavior behavior = mock(Behavior.class);
        SelectEvent<String> selectEvent = new SelectEvent<>(uiComponent, behavior, sampleDTO.toString());

        // Call the onItemSelect method
        autocompleteBean.onItemSelect(selectEvent);

        // Verify that the selectedGeocodingDTO is set correctly
        assertNotNull(autocompleteBean.getSelectedGeocodingDTO());
        assertEquals(sampleDTO, autocompleteBean.getSelectedGeocodingDTO());

        // Reset the mocks if needed
        reset(geocodingApiRequestService);
    }

    @Test
    void testGetDisplayName() {
        // Prepare a sample GeocodingDTO
        GeocodingDTO sampleDTO = new GeocodingDTO("Place1", null, 22.7, 77.0, "country", "state");

        // Set up the AutocompleteBean with the sample DTO
        autocompleteBean.getCurrentFiveSuggestedDTOs().put(sampleDTO.toString(), sampleDTO);

        // Call the getDisplayName method
        String displayName = autocompleteBean.getDisplayName(sampleDTO);

        // Verify that the display name is returned correctly
        assertNotNull(displayName);
        assertEquals("Place1, country", displayName);

        // Reset the mocks if needed
        reset(geocodingApiRequestService);
    }
}

