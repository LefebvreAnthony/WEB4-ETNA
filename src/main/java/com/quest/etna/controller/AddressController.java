package com.quest.etna.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.quest.etna.enums.UserRole;
import com.quest.etna.model.Address;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Autowired
    public AddressController(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Address> getAllAddress() {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(jwtUserDetails.getUsername());

        UserRole role = user.getRole();
        List<Address> addressList = List.of();

        if (role.equals(UserRole.ROLE_ADMIN)) {
            addressList = addressRepository.findAll();
        } else if (role.equals(UserRole.ROLE_USER)) {
            addressList = addressRepository.findByUser(user);
        }

        if (addressList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No address found");

        }

        return addressList;
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {

        if (address == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        }

        Optional<Address> addressExist = addressRepository.findAll().stream()
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Address getAddressById(@PathVariable Long id) {

        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(jwtUserDetails.getUsername());

        UserRole role = user.getRole();

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address id is required");
        }

        Optional<Address> address = addressRepository.findById(id);

        if (address.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }

        if (role.equals(UserRole.ROLE_USER) && !address.get().getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to access this address");
        }

        return address.get();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Address updateAddress(@PathVariable Long id, @RequestBody Address address) {

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address id is required");
        }

        Optional<Address> addressExist = addressRepository.findById(id);

        if (addressExist.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }

        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(jwtUserDetails.getUsername());

        UserRole role = user.getRole();

        if (role.equals(UserRole.ROLE_USER) && !addressExist.get().getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this address");
        }
        if (address.getCity() != null)
            addressExist.get().setCity(address.getCity());

        if (address.getCountry() != null)
            addressExist.get().setCountry(address.getCountry());

        if (address.getPostalCode() != null)
            addressExist.get().setPostalCode(address.getPostalCode());
        if (address.getStreet() != null)
            addressExist.get().setStreet(address.getStreet());

        try {
            addressExist.get().setUpdateAt(LocalDateTime.now());
            addressRepository.save(addressExist.get());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while updating address " + e.getMessage());
        }

        return addressExist.get();

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address id is required");
        }

        Map<String, Object> response = Map.of("success", true);
        Optional<Address> addressExist = addressRepository.findById(id);

        if (addressExist.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }

        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(jwtUserDetails.getUsername());

        UserRole role = user.getRole();

        if (role.equals(UserRole.ROLE_USER) && !addressExist.get().getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this address");
        }

        try {
            addressRepository.delete(addressExist.get());

        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("success", false, "message", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}