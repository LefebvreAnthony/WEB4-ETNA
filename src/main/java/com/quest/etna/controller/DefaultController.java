package com.quest.etna.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/")
public class DefaultController {

    @RequestMapping("/")
    public String index() {
        return "Hello World!";
    }

    @GetMapping("/testSuccess")
    @ResponseStatus(HttpStatus.OK)
    public String testSuccess() {
        return "success!";
    }

    @GetMapping("/testError")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String testError() {
        return "error!";
    }

    @GetMapping("/testNotFound")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String testNotFound() {
        return "not found!";
    }
}
