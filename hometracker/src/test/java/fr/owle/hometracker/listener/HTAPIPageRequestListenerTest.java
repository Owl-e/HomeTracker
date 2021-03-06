package fr.owle.hometracker.listener;

import fr.owle.hometracker.event.*;
import fr.owle.hometracker.modules.HTModule;
import fr.owle.hometracker.pages.*;
import fr.owle.hometracker.utils.exception.ResourceNotExistingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.Mockito.*;

public class HTAPIPageRequestListenerTest {

    private HTAPIPageRequestListener htapiPageRequestListener;
    private PageManager pageManager;

    private String moduleName, pageName, path, param, body;

    @BeforeEach
    public final void init() throws InvocationTargetException, ResourceNotExistingException, IllegalAccessException, IOException, NoSuchFieldException {
        htapiPageRequestListener = mock(HTAPIPageRequestListener.class, CALLS_REAL_METHODS);
        moduleName = "EpicTestModule";
        pageName = "page";
        path = "the/path";
        param = "param=value";
        body = "{\"message\":\"hello\"}";

        pageManager = mock(PageManager.class);
        when(pageManager.getPagePathContent(any(HTModule.class), any(Page.class), anyString(), anyString(), anyString(), any())).thenReturn("Ee chee wa maa !");
        FieldSetter.setField(htapiPageRequestListener, HTAPIPageRequestListener.class.getDeclaredField("pageManager"), pageManager);
    }

    @Test
    public final void getRequestTest() throws InvocationTargetException, ResourceNotExistingException, IllegalAccessException, IOException {
        final GetRequestEvent event = new GetRequestEvent(moduleName, pageName, path, param, body);
        htapiPageRequestListener.getRequest(event);
        verify(pageManager, times(1)).getPagePathContent(eq(event.getModule()), eq(event.getPage()), eq(path), eq(param), eq(body), eq(GetRequest.class));
    }

    @Test
    public final void postRequestTest() throws InvocationTargetException, ResourceNotExistingException, IllegalAccessException, IOException {
        final PostRequestEvent event = new PostRequestEvent(moduleName, pageName, path, param, body);
        htapiPageRequestListener.postRequest(event);
        verify(pageManager, times(1)).getPagePathContent(eq(event.getModule()), eq(event.getPage()), eq(path), eq(param), eq(body), eq(PostRequest.class));
    }

    @Test
    public final void putRequest() throws InvocationTargetException, ResourceNotExistingException, IllegalAccessException, IOException {
        final PutRequestEvent event = new PutRequestEvent(moduleName, pageName, path, param, body);
        htapiPageRequestListener.putRequest(event);
        verify(pageManager, times(1)).getPagePathContent(eq(event.getModule()), eq(event.getPage()), eq(path), eq(param), eq(body), eq(PutRequest.class));
    }

    @Test
    public final void deleteRequest() throws InvocationTargetException, ResourceNotExistingException, IllegalAccessException, IOException {
        final DeleteRequestEvent event = new DeleteRequestEvent(moduleName, pageName, path, param, body);
        htapiPageRequestListener.deleteRequest(event);
        verify(pageManager, times(1)).getPagePathContent(eq(event.getModule()), eq(event.getPage()), eq(path), eq(param), eq(body), eq(DeleteRequest.class));
    }

    /*
    @Test
    public final void throwsTest() throws InvocationTargetException, ResourceNotExistingException, IllegalAccessException, IOException, NoSuchFieldException {
        final ResourceNotExistingException exception = mock(ResourceNotExistingException.class);
        when(exception.getMessage()).thenReturn("lol tu pu");

        final PageManager pageManager = mock(PageManager.class);
        final HTAPIPageRequestListener htapiPageRequestListener = mock(HTAPIPageRequestListener.class, CALLS_REAL_METHODS);

        doThrow(exception).when(pageManager).getPagePathContent(any(HTModule.class), any(Page.class), anyString(), anyString(), anyString(), any());
        FieldSetter.setField(htapiPageRequestListener, HTAPIPageRequestListener.class.getDeclaredField("pageManager"), pageManager);

        final GetRequestEvent event = new GetRequestEvent(moduleName, pageName, path, param, body);

        htapiPageRequestListener.getRequest(event);
        verify(pageManager, times(1)).getPagePathContent(eq(event.getModule()), eq(event.getPage()), eq(path), eq(param), eq(body), eq(GetRequest.class));
        assertEquals( "<h1>404</h1><h3><span style=\"color: red\">Error: </span>lol tu pu</h1>", event.getContent());
    }
    */
}
