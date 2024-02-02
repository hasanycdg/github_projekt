package at.qe.skeleton.tests;
import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.FavLocationService;
import at.qe.skeleton.internal.ui.controllers.FavLocationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class FavLocationControllerTest {

    @Mock
    private FavLocationService favLocationService;

    @InjectMocks
    private FavLocationController favLocationController;

    @Test
    public void testDoSafeLocationByName_LocationDoesNotExist() {
        String city = "TestCity";
        Userx user = new Userx();

        //mock that the location is not saved in the database
        Mockito.when(favLocationService.loadLocation(city, user)).thenReturn(null);

        FavLocation expectedLocation = new FavLocation();  // Create a mock FavLocation
        //so that the calles service is not null we have to mock it
        Mockito.when(favLocationService.stringToFavLocation(city, user)).thenReturn(expectedLocation);

        FavLocation result = favLocationController.doSafeLocationByName(city, user);

        //make sure that the favLocationService was called once
        Mockito.verify(favLocationService, Mockito.times(1)).stringToFavLocation(city, user);

        // if the result is the saved location then it was correcly given to the service. if it is null then the location is not saved.
        assertEquals(expectedLocation, result);
    }

    // This Tests checks if the duplicates are correctly filtered (if it is already saved in the database it should not be saved again
    @Test
    public void testDoSafeLocationByName_LocationExists() {
        String city = "TestCity";
        Userx user = new Userx();

        FavLocation existingLocation = new FavLocation();  // Create a mock FavLocation or use a real instance
        Mockito.when(favLocationService.loadLocation(city, user)).thenReturn(existingLocation);

        FavLocation result = favLocationController.doSafeLocationByName(city, user);

        Mockito.verify(favLocationService, Mockito.never()).stringToFavLocation(Mockito.anyString(), Mockito.any(Userx.class));

        assertNull(result);
    }
}
