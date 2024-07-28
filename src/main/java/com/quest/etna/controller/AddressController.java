package com.quest.etna.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.quest.etna.model.Adress;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.repositories.AdressRepository;
import com.quest.etna.repositories.UserRepository;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AdressRepository addressRepository;
    private final UserRepository userRepository;

    @Autowired
    public AddressController(AdressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Adress> getAllAddress() {

        List<Adress> addressList = addressRepository.findAll();

        if (addressList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No address found");
        }

        return addressRepository.findAll();
    }

    @PostMapping
    public Adress createAddress(@RequestBody Adress address) {

        if (address == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        }

        Optional<Adress> addressExist = addressRepository.findAll().stream()
                .filter(a -> a.equals(address))
                .findFirst();

        if (addressExist.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Address already exist");
        }

        JwtUserDetails jwDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(jwDetails.getUsername());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        address.setUser(user);

        try {
            addressRepository.save(address);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while saving address " + e.getMessage());
        }

        return address;
    }
}
