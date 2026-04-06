package com.example.ipmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to the IP Manager API! The server is running successfully.\n\n" +
               "Please refer to the Postman collection to test the available endpoints.";
    }
}
