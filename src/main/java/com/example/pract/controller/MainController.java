package com.example.pract.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String showWelcomePage(Model model) {
        log.info("Handling request to index page");
        return "index";
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        log.info("Handling request to home page");
        return "home";
    }

    @PostMapping("/multiply")
    public String handleMultiply(@RequestParam("number") String numberStr, Model model) {
        log.info("Handling request to multiplication page");
        int number = Integer.parseInt(numberStr);
        int result = number * 2;
        model.addAttribute("result", result);
        model.addAttribute("number", number);
        return "multiplication";
    }
}