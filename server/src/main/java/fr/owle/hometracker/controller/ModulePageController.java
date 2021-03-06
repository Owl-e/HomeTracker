package fr.owle.hometracker.controller;

import fr.owle.hometracker.event.*;
import fr.owle.hometracker.modules.HTModule;
import fr.owle.hometracker.modules.ModuleManager;
import fr.owle.hometracker.services.PagesService;
import fr.owle.hometracker.utils.exception.HTModuleNotFoundException;
import fr.owle.hometracker.utils.exception.PageMissingIndexAnnotationException;
import fr.owle.hometracker.utils.exception.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RestController
public class ModulePageController {

    @Autowired
    private PagesService pagesService;

    @Autowired
    private ModuleManager moduleManager;

    @GetMapping("/{module}/{page}/**")
    public byte[] getRequest(@PathVariable String module, @PathVariable String page, @RequestBody(required = false) String body, HttpServletRequest request) throws PageNotFoundException, PageMissingIndexAnnotationException, HTModuleNotFoundException {
        final int size = module.length() + page.length() + 2;
        final String uri = request.getRequestURI();
        final String path = uri.substring(size);
        final String param = request.getQueryString();
        final GetRequestEvent getRequestEvent = new GetRequestEvent(module, page, path, param, body);
        return Base64.getDecoder().decode(pagesService.readContent(getRequestEvent));
    }

    @PostMapping("/{module}/{page}/**")
    public byte[] postRequest(@PathVariable String module, @PathVariable String page, @RequestBody(required = false) String body, HttpServletRequest request) throws PageNotFoundException, PageMissingIndexAnnotationException, HTModuleNotFoundException {
        final int size = module.length() + page.length() + 2;
        final String uri = request.getRequestURI();
        final String path = uri.substring(size);
        final String param = request.getQueryString();
        final PostRequestEvent postRequestEvent = new PostRequestEvent(module, page, path, param, body);
        return Base64.getDecoder().decode(pagesService.readContent(postRequestEvent));
    }

    @PutMapping("/{module}/{page}/**")
    public byte[] putRequest(@PathVariable String module, @PathVariable String page, @RequestBody(required = false) String body, HttpServletRequest request) throws PageNotFoundException, PageMissingIndexAnnotationException, HTModuleNotFoundException {
        final int size = module.length() + page.length() + 2;
        final String uri = request.getRequestURI();
        final String path = uri.substring(size);
        final String param = request.getQueryString();
        final PutRequestEvent putRequestEvent = new PutRequestEvent(module, page, path, param, body);
        return Base64.getDecoder().decode(pagesService.readContent(putRequestEvent));
    }

    @DeleteMapping("/{module}/{page}/**")
    public byte[] deleteRequest(@PathVariable String module, @PathVariable String page, @RequestBody(required = false) String body, HttpServletRequest request) throws PageNotFoundException, PageMissingIndexAnnotationException, HTModuleNotFoundException {
        final int size = module.length() + page.length() + 2;
        final String uri = request.getRequestURI();
        final String path = uri.substring(size);
        final String param = request.getQueryString();
        final DeleteRequestEvent deleteRequestEvent = new DeleteRequestEvent(module, page, path, param, body);
        return Base64.getDecoder().decode(pagesService.readContent(deleteRequestEvent));
    }
    
    @GetMapping(path = {"/{module}", "/{module}/"})
    public RedirectView redirectToMainPage(@PathVariable String module) throws HTModuleNotFoundException {
        final HTModule htModule = moduleManager.getModule(module);
        final RedirectView redirectView = new RedirectView();
        final String url = "/" + module + "/" + htModule.getMainPage();
        redirectView.setContextRelative(true);
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping("/")
    public RedirectView redirectView() {
        final RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(true);
        redirectView.setUrl("/HomeTracker");
        return redirectView;
    }
}
