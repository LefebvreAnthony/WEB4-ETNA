package com.quest.etna.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDetails>> getAllUser() {
        List<User> users = userRepository.findAll();

        List<UserDetails> userDetails = users.stream()
                .map(user -> new UserDetails(user.getUsername(), user.getRole())).collect(Collectors.toList());

        return ResponseEntity.ok().body(userDetails);
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDetails> getUserByUsername(@PathVariable String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        UserDetails userDetails = new UserDetails(user.getUsername(), user.getRole());

        return ResponseEntity.ok().body(userDetails);

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDetails> updateUser(@PathVariable long id, @RequestBody User user) {
        User userToUpdate = userRepository.findById(id);

        if (userToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User userConnected = userRepository.findByUsername(jwtUserDetails.getUsername());

        if (!userConnected.getRole().equals(UserRole.ROLE_ADMIN)
                && !userToUpdate.getUsername().equals(userConnected.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this user");
        }

        if (user.getUsername() != null && userRepository.findByUsername(user.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        userToUpdate.setUsername(user.getUsername());

        if (user.getPassword() != null) {
            userToUpdate.setPassword(user.getPassword());
        }

        if (userConnected.getRole().equals(UserRole.ROLE_ADMIN) && user.getRole() != null) {
            userToUpdate.setRole(user.getRole());
        }

        try {
            userToUpdate.setUpdateAt(LocalDateTime.now());
            userRepository.save(userToUpdate);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating user");
        }

        UserDetails userDetails = new UserDetails(userToUpdate.getUsername(), userToUpdate.getRole());

        return ResponseEntity.ok().body(userDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable long id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User userConnected = userRepository.findByUsername(jwtUserDetails.getUsername());

        if (!userConnected.getRole().equals(UserRole.ROLE_ADMIN)
                && !user.getUsername().equals(userConnected.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to delete this user");
        }
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }

        return ResponseEntity.ok().body(Map.of("success", true));
    }

}
