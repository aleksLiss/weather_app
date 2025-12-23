package com.weather.app.controller;

import com.weather.app.service.IndexPageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/", "index"})
public class IndexController {

    private final IndexPageService indexPageService;

    public IndexController(IndexPageService indexPageService) {
        this.indexPageService = indexPageService;
    }

    @GetMapping
    public String getIndexPage(Model model,
                               HttpServletRequest request) {
        boolean isAuthenticated = indexPageService.isPopulateIndexModel(model, request);
        if (isAuthenticated) {
            return "index";
        } else {
            return "redirect:/sign/up";
        }
    }
}
