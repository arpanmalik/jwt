package com.example.demoPractiseSecurity.controller;


import com.example.demoPractiseSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> body) {
        userService.signup(body.get("username"), body.get("password"), body.get("role"));
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String token = userService.login(body.get("username"), body.get("password"));
        return ResponseEntity.ok(token);
    }

    @GetMapping("/api/hello")
    public String hello(){
        return "Hello User";
    }

    @GetMapping("/admin/hello")
    public String adminHello(){
        return "Hello Admin";
    }

}
