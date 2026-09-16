package com.example.springboot.controller;

import com.example.springboot.implementations.Users;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/authorization")
public class UserController {

    @Autowired
    private UserService userService;

    // WRITE A CLIENT METHOD FOR THAT
    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {

        return userService.verify(user);
    }

//    @PostMapping("/login")
//    public String login(Users user){
//        return "Success";
//    }
}
