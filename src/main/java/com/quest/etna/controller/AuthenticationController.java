package com.quest.etna.controller;

import com.quest.etna.repositories.UserRepository;

import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDetails register(@RequestBody User registration,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (registration == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body");
        }

        String username = registration.getUsername();
        String password = registration.getPassword();

        User existingUser = userRepository.findByUsername(username);

        System.out.println("Username: " + existingUser);
        // Check if the username is already taken
        if (existingUser != null && existingUser.getUsername().equalsIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        // Create a new user
        User user = new User(username, password);

        // Save the user to the database
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user");
        }

        return new UserDetails(username, user.getRole());
    }

}
