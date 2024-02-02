package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.services.FavLocationService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;
import java.util.List;

/**
 * Bean for managing the change of the order of a OrderList.
 */
@Named
@Scope("request")
public class ReorderOrderlistBean implements Serializable {
    @Autowired
    private FavLocationService favLocationService;
    @Autowired
    private SessionInfoBean sessionInfoBean;
    @Autowired
    private FilterListBean filterListBean;

    /**
     * FavLocations = the original List of FavoriteLocations (shown if filter value = "")
     */
    private List<FavLocation> favLocations;
    /**
     * filteredFavLocations = the actual shown List (according to the filter value)
     */
    private List<FavLocation> filteredFavLocations;

    @PostConstruct
    public void init() {
        favLocations = favLocationService.getUserLocations(sessionInfoBean.getCurrentUser());
    }

    /**
     * Handles the case where a favorite location is selected from the list.
     * No test for this function because we weren't able to simulate the FacesContext.
     * @param event The selection event
     */
    public void onSelect(SelectEvent<?> event) {
        Object selectedObject = event.getObject();

        if (selectedObject instanceof FavLocation selectedLocation) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", selectedLocation.getName()));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Selection", "Selected object is not of type FavLocation"));
        }
    }

    /**
     * Handles the reordering of the favorite locations list.
     * No test for this function because we weren't able to simulate the FacesContext.
     */
    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
        favLocationService.updateIndexLocations(filteredFavLocations);
    }
    /**
     * Deletes the currently selected Location from the List.
     * The ID of the location to be deleted is obtained from the request parameter 'idFavLocation' (defined in the /secured/welcome.xhtml).
     * Not test because we weren't able to simulate the FacesContext
     */
    public void deleteLocation() {
        long id = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idFavLocation"));

        for (FavLocation favLocation : favLocations) {
            if (favLocation.getId() == id) {
                favLocationService.deleteLocation(favLocation);
                break;
            }
        }
    }
    /**
     * This function edits the FavLocation List according to the currently entered filteredValue
     * @return the List of favorite Locations associated with the current user
     */
    public List<FavLocation> getFilteredFavLocations() {
        filteredFavLocations = filterListBean.getFilteredFavLocations(favLocations);
        return filterListBean.getFilteredFavLocations(favLocations);
    }
    /**
     * setFilteredFavLocations(...) function is only for test purpose. Should not be used in real Appliaction
     * @param filteredFavLocations
     */
    public void setFilteredFavLocations(List<FavLocation> filteredFavLocations) {
        this.filteredFavLocations = filteredFavLocations;
    }
}