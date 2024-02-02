package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.services.FavLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
@Scope("view")
public class FavLocationController implements Serializable {
    @Autowired
    private FavLocationService favLocationService;

    /**
     * Filters out dublicates and saves by cityname and user
     * @param city cityname to the according city which is to be saved
     * @param user that saves the city
     */
    public FavLocation doSafeLocationByName(String city, Userx user){
        if (favLocationService.loadLocation(city,user) == null) {
             return favLocationService.stringToFavLocation(city, user);
        }
        return null;
    }
}
