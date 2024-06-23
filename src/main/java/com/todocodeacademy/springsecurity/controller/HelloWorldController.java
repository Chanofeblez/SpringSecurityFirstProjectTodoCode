package com.todocodeacademy.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/holaseg")
    public String secHelloWorld(){
        return "Hola Mundo TodoCode con seguridad";
    }

    @GetMapping("/holanoseg")
    public String noSecHelloWorld(){
        return "Hola Mundo TodoCode sin seguridad";
    }


}
