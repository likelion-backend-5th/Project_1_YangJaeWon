package com.example.miniproject2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping
    public String root() {
        return "hello";
    }

    @GetMapping("/no-auth")
    public String noAuth() {
        return "no auth success!";
    }

    @GetMapping("/require-auth")
    public String reAuth() {
        return "auth success!";
    }
}
