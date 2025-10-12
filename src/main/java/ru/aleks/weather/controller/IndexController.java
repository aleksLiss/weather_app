package ru.aleks.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private static final String NAMESESSION = "WEATHERAPPSESSIONID";
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    // todo change method: get user and all saved locations where location.userId equal this user

    @GetMapping("/")
    public String getIndex(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        String foundUserName = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                LOGGER.info("IndexController: cookie value with name 'username': " + cookie.getValue());
                foundUserName = cookie.getValue();
            }
        }
        if (foundUserName != null) {
            model.addAttribute("username", foundUserName);
            return "index";
        }
        return "redirect:/user/up";
    }

    // add button 'x' and method delete location from list user locations
}
