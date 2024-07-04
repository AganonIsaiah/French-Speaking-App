package com.example.frenchlearningapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LevelController {

    /**
     * Displays language proficiency selection form
     *
     * @return the LevelController.html template
     */
    @GetMapping("/language")
    public String showLanguageForm() {
        return "LevelController";
    }

    /**
     *
     * @param proficiency User's chosen proficiency level
     * @param model
     */
    @PostMapping("/language")
    public void submitLanguageForm(@RequestParam String proficiency, Model model) {
        System.out.println("Test1 : "+proficiency);
        model.addAttribute("proficiency", proficiency);
    }




}