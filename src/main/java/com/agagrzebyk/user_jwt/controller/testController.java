package com.agagrzebyk.user_jwt.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class testController {

    @GetMapping("/test")
    public String test1() {
        return "testowy tekst po zalogowaniu";
    }

    @GetMapping("/teacher/reports")
    public String test2() {
        return "To jest tekst dla test2 po autentyfikacji";
    }

    @GetMapping("/admin/user")
    public String test3() {
        return "test3";
    }
}
