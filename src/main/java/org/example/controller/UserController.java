package org.example.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan
public class UserController {
    @RequestMapping("/hello")
    public String sayHello() {
        System.out.println("hello");
        return "hello";
    }
}
