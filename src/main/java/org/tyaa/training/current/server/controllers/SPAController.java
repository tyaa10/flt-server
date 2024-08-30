package org.tyaa.training.current.server.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SPAController {

    private final String contextPath;

    public SPAController(@Value("${server.servlet.context-path:}") String contextPath) {
        this.contextPath = contextPath;
    }

    @GetMapping(path = { "/", "/{name:^(?!api|swagger-ui|v3|error|css|js).+}/**" }, produces = MediaType.TEXT_HTML_VALUE)
    public String getSinglePage(Model model) {
        model.addAttribute("contextPath", contextPath);
        return "index";
    }
}
