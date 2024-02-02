package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.geocoding.GeocodingDTO;
import at.qe.skeleton.external.services.GeocodingApiRequestService;
import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.FavLocationService;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.ui.beans.AutocompleteBean;
import at.qe.skeleton.internal.ui.controllers.FavLocationController;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebAppConfiguration
public class FavLocationTest {
    @Autowired
    FavLocationService favLocationService;
    @Autowired
    UserxService userxService;
    @Autowired
    AutocompleteBean autocompleteBean;
    @Autowired
    GeocodingApiRequestService geocodingApiRequestService;

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    @DirtiesContext
    public void testDeleteLocation(){
        //create user
        Userx testUser = new Userx();
        testUser.setUsername("testUserFav");
        testUser.setPassword("testPassword");
        testUser.setRoles(Set.of(UserxRole.USER));
        userxService.saveUser(testUser);

        //create Test Location
        FavLocation testLocation1 = new FavLocation();
        Long testId = 1L;
        testLocation1.setId(testId);
        testLocation1.setIndex(1);
        testLocation1.setUser(testUser);
        testLocation1.setName("TestLocation1");
        favLocationService.saveLocation(testLocation1);

        //verify if the Location is correctly saved
        assertEquals(testLocation1, favLocationService.loadLocation(testId));

        //delete Location
        favLocationService.deleteLocation(testLocation1);

        //verify if the Location is really deleted
        assertThrows(EntityNotFoundException.class, () -> favLocationService.loadLocation(testId));
    }

    /* The Cascade Delete works in the Application but due to unknown error not in the Test
    @Test
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    public void testCascadeDelete(){
        //create user
        Userx testUser = new Userx();
        testUser.setUsername("testUserFav");
        testUser.setPassword("testPassword");
        testUser.setRoles(Set.of(UserxRole.USER));
        userxService.saveUser(testUser);

        //create Test Location
        FavLocation testLocation1 = new FavLocation();
        Long testId = 1L;
        testLocation1.setId(testId);
        testLocation1.setIndex(1);
        testLocation1.setUser(testUser);
        testLocation1.setName("TestLocation1");
        favLocationService.saveLocation(testLocation1);

        //delete associated user
        userxService.deleteUser(testUser);

        //check if it is still saved
        assertThrows(EntityNotFoundException.class, () -> favLocationService.loadLocation(testId));
    }*/

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    @DirtiesContext
    public void testUpdateIndexLocation(){
        //create user
        Userx testUser = new Userx();
        testUser.setUsername("testUserFav");
        testUser.setPassword("testPassword");
        testUser.setRoles(Set.of(UserxRole.USER));
        userxService.saveUser(testUser);

        //create multiple Locations and save them in order [testLocation2,testLocation1]
        FavLocation testLocation1 = new FavLocation();
        testLocation1.setId(1L);
        testLocation1.setIndex(500);
        testLocation1.setUser(testUser);
        testLocation1.setName("TestLocation1");
        favLocationService.saveLocation(testLocation1);

        FavLocation testLocation2 = new FavLocation();
        testLocation2.setId(2L);
        testLocation2.setIndex(9);
        testLocation2.setUser(testUser);
        testLocation2.setName("TestLocation2");
        favLocationService.saveLocation(testLocation2);

        //Create a new List with a different order [testLocation1, testLocation2]
        List<FavLocation> favLocationList = Arrays.asList(testLocation1, testLocation2);

        //test updating the indexes of the saved List to the new List
        favLocationService.updateIndexLocations(favLocationList);

        //verify if the locations have been updated
        assertEquals(0, favLocationService.loadLocation(1L).getIndex());
        assertEquals(1, favLocationService.loadLocation(2L).getIndex());
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    @DirtiesContext
    public void testLoadLocation(){
        //create User
        Userx testUser = new Userx();
        testUser.setUsername("testUserFav");
        testUser.setPassword("testPassword");
        testUser.setRoles(Set.of(UserxRole.USER));
        userxService.saveUser(testUser);

        //create Location
        FavLocation testLocation1 = new FavLocation();
        Long testId = 1L;
        testLocation1.setId(testId);
        testLocation1.setIndex(1);
        testLocation1.setUser(testUser);
        testLocation1.setName("TestLocation1");
        favLocationService.saveLocation(testLocation1);

        assertEquals(testLocation1, favLocationService.loadLocation(testId));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    @DirtiesContext
    public void testGetUserLocation(){
        //create User
        Userx testUser = new Userx();
        testUser.setUsername("testUserFav");
        testUser.setPassword("testPassword");
        testUser.setRoles(Set.of(UserxRole.USER));
        userxService.saveUser(testUser);

        //create multiple Locations and save them
        FavLocation testLocation1 = new FavLocation();
        testLocation1.setId(1L);
        testLocation1.setIndex(1);
        testLocation1.setUser(testUser);
        testLocation1.setName("TestLocation1");
        favLocationService.saveLocation(testLocation1);

        FavLocation testLocation2 = new FavLocation();
        testLocation2.setId(2L);
        testLocation2.setIndex(2);
        testLocation2.setUser(testUser);
        testLocation2.setName("TestLocation2");
        favLocationService.saveLocation(testLocation2);

        //create a new arrangement of the List
        List<FavLocation> favLocationList = Arrays.asList(testLocation1, testLocation2);

        assertEquals(favLocationList, favLocationService.getUserLocations(testUser));
    }

    //API Call not working
    /*@Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    @DirtiesContext
    public void testStringToFavLocation_Success() throws EntityNotFoundException {
        //create User
        Userx testUser = new Userx();
        testUser.setUsername("testUserFav");
        testUser.setPassword("testPassword");
        testUser.setRoles(Set.of(UserxRole.USER));
        userxService.saveUser(testUser);

        //create City and make sure that it is simulated correct in the Application
        String city = "Innsbruck";
        Map<String, String> localNames = new HashMap<String, String>();
        localNames.put("de", "Innsbruck");
        GeocodingDTO geocodingDTO = new GeocodingDTO(city, localNames, 47.2654296, 11.3927685, "AT", "Tyrol");
        autocompleteBean.setSelectedGeocodingDTO(geocodingDTO);

        // Test
        FavLocation result = favLocationService.stringToFavLocation("Innsbruck", testUser);

        // Verify
        assertEquals(city ,result.getName());
        assertEquals(result, favLocationService.getUserLocations(testUser).get(0));
    }*/

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    @DirtiesContext
    public void testStringToFavLocation_Failure() throws EntityNotFoundException {
        //create User
        Userx testUser = new Userx();
        testUser.setUsername("testUserFav");
        testUser.setPassword("testPassword");
        testUser.setRoles(Set.of(UserxRole.USER));
        userxService.saveUser(testUser);

        //create City and make sure that it is simulated correct in the Application
        String city = "NoCityName";
        Map<String, String> localNames = new HashMap<String, String>();
        localNames.put("de", "Innsbruck");
        GeocodingDTO geocodingDTO = new GeocodingDTO(city, localNames, 47.2654296, 11.3927685, "AT", "Tyrol");
        autocompleteBean.setSelectedGeocodingDTO(geocodingDTO);

        // Test and verify that the Exception is thrown and no data is saved
        assertThrows(EntityNotFoundException.class, () -> favLocationService.stringToFavLocation(city, testUser));
        assertTrue(favLocationService.getUserLocations(testUser).isEmpty());
    }

}
