package at.qe.skeleton.internal.services;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import at.qe.skeleton.internal.model.FavLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * JSF Converter for FavLocation objects.
 * Converts between FavLocation objects and their String representations in JSF UI components.
 */
@Component
@Scope("application")
@FacesConverter("favLocationConverter")
public class FavLocationConverter implements Converter<FavLocation> {

    @Autowired
    private FavLocationService favLocationService;

    @Override
    public FavLocation getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            // Convert the String representation to FavLocation object using the service
            // Assuming favLocationService can retrieve a FavLocation by its ID (long)
            return favLocationService.loadLocation(Long.parseLong(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, FavLocation favLocation) {
        if (favLocation != null && favLocation.getId() != null) {
            // Convert the String representation to FavLocation object using the service
            // Assuming favLocationService can retrieve a FavLocation by its ID (long)
            return String.valueOf(favLocation.getId());
        }
        return null;
    }

}
