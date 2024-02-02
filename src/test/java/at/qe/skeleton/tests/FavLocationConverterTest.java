package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.services.FavLocationConverter;
import at.qe.skeleton.internal.services.FavLocationService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class FavLocationConverterTest {

    @Autowired
    private FavLocationService favLocationService;

    @Autowired
    private FavLocationConverter favLocationConverter;

    @Test
    @DirtiesContext
    public void testGetAsObjectValidInput() {
        //create and save Test objects
        String value = "1";
        FavLocation expectedFavLocation = new FavLocation();
        expectedFavLocation.setId(1L);
        expectedFavLocation.setName("TestLocation");
        favLocationService.saveLocation(expectedFavLocation);

        //mock facesContext and uiComponent
        FacesContext context = mock(FacesContext.class);
        UIComponent component = mock(UIComponent.class);

        //test
        FavLocation result = favLocationConverter.getAsObject(context, component, value);

        //verify
        assertEquals(expectedFavLocation, result);
    }

    @Test
    public void testGetAsObjectNullInput() {
        String value = null;

        FacesContext context = mock(FacesContext.class);
        UIComponent component = mock(UIComponent.class);

        FavLocation result = favLocationConverter.getAsObject(context, component, value);

        assertNull(result);
    }

    @Test
    public void testGetAsStringValidInput() {
        FavLocation expectedFavLocation = new FavLocation();
        expectedFavLocation.setId(1L);
        expectedFavLocation.setName("TestLocation");
        favLocationService.saveLocation(expectedFavLocation);

        FacesContext context = mock(FacesContext.class);
        UIComponent component = mock(UIComponent.class);

        String result = favLocationConverter.getAsString(context, component, expectedFavLocation);

        assertEquals("1", result);
    }

    @Test
    public void testGetAsStringNullInput() {
        FavLocation favLocation = null;

        FacesContext context = mock(FacesContext.class);
        UIComponent component = mock(UIComponent.class);

        String result = favLocationConverter.getAsString(context, component, favLocation);

        assertNull(result);
    }
}
