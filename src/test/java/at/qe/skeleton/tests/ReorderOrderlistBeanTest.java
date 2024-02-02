package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.ui.beans.ReorderOrderlistBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;
import org.primefaces.event.SelectEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class ReorderOrderlistBeanTest {
    @Test
    public void testOnSelectWithFavLocation() {
        ContextMocker.mockFacesContext();
        // Mock the event object
        SelectEvent<FavLocation> mockEvent = mock(SelectEvent.class);
        FavLocation mockLocation = new FavLocation();
        when(mockEvent.getObject()).thenReturn(mockLocation);

        // Call the onSelect method
        ReorderOrderlistBean yourInstance = new ReorderOrderlistBean();
        yourInstance.onSelect(mockEvent);

        // Verify that the FacesMessage is created and added to the context
        FacesContext context = FacesContext.getCurrentInstance();
        verify(context, times(1)).addMessage(eq(null), any(FacesMessage.class));
    }
}
