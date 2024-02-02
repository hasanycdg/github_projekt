package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.geocoding.GeocodingDTO;
import at.qe.skeleton.external.services.GeocodingApiRequestService;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Managed bean for handling autocomplete functionality of the main search bar.
 */
@Component
@Scope("session")
public class AutocompleteBean {

    private Map<String, GeocodingDTO> currentFiveSuggestedDTOs = new HashMap<>();
    private GeocodingDTO selectedGeocodingDTO;

    @Autowired
    GeocodingApiRequestService geocodingApiRequestService;

    /**
     * Retrieves autocompletion suggestions based on the provided input.
     * Not sure if it was intended to use the Geocoding API for this purpose.
     * Prior, an autocomplete API from Google was used.
     * Alternatively, a plain list of locations could be used for autocompletion.
     *
     * @param input The input for autocompletion.
     * @return List of GeocodingDTO representing the autocompletion suggestions.
     */
    public List<GeocodingDTO> getAutocompletion(String input) {
         List<GeocodingDTO> currentSuggestions = geocodingApiRequestService.retrieveGeocodingData(input);

         if (currentSuggestions != null){
             currentSuggestions.forEach(this::saveCurrentSuggestions); //save current suggestions in a map to be able to retrieve them later
         }
         return currentSuggestions;
    }

    /**
     * Handles the selection of an item from the autocompletion dropdown.
     * As soon as the user selects a suggestion, the selectedGeocodingDTO is set.
     *
     * @param event The SelectEvent containing the selected item.
     */
    public void onItemSelect(SelectEvent<String> event){
        String geocodingDTOasString = event.getObject();
        selectedGeocodingDTO = currentFiveSuggestedDTOs.get(geocodingDTOasString);
    }

    /**
     * Saves the current suggestions in a map for later retrieval.
     *
     * @param geocodingDTO The GeocodingDTO to be saved.
     */
    public void saveCurrentSuggestions(GeocodingDTO geocodingDTO){
        currentFiveSuggestedDTOs.put(geocodingDTO.toString(), geocodingDTO); //toString is needed to guarantee uniqueness
    }

    /**
     * Retrieves the display name for a GeocodingDTO, used in the autocomplete dropdown.
     *
     * @param geocodingDTO The GeocodingDTO for which to get the display name.
     * @return The display name for the GeocodingDTO.
     */
    public String getDisplayName(GeocodingDTO geocodingDTO){ //this is displayed in the autocomplete dropdown
        if (geocodingDTO == null){
            return "";
        }
        return geocodingDTO.name() + ", " + geocodingDTO.country();
    }

    public GeocodingDTO getSelectedGeocodingDTO() {
        return selectedGeocodingDTO;
    }

    public Map<String, GeocodingDTO> getCurrentFiveSuggestedDTOs() {
        return currentFiveSuggestedDTOs;
    }

    /**
     * This function is only for testing purpose
     * @param geocodingDTO
     */
    public void setSelectedGeocodingDTO(GeocodingDTO geocodingDTO) {selectedGeocodingDTO = geocodingDTO;}
}
