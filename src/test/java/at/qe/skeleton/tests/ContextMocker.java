package at.qe.skeleton.tests;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * This class is used to mock the FacesContext for testing purposes.
 *
 * source: <a href="https://illegalargumentexception.blogspot.com/2011/12/jsf-mocking-facescontext-for-unit-tests.html#mockFacesCurrentInstance">...</a>
 */
public abstract class ContextMocker extends FacesContext {
    public static FacesContext mockFacesContext() {
        FacesContext context = mock(FacesContext.class);
        setCurrentInstance(context);

        // Mock getExternalContext ("one path deeper")
        ExternalContext externalContext = mock(ExternalContext.class);
        when(context.getExternalContext()).thenReturn(externalContext);

        // Mock getRequestParameterMap ("two paths deeper")
        Map<String, String> requestParameterMap = new HashMap<>();
        when(externalContext.getRequestParameterMap()).thenReturn(requestParameterMap);
        return context;
    }
}
