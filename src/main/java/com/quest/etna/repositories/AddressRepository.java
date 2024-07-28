package com.quest.etna.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Address;
import com.quest.etna.model.User;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

    Optional<Address> findById(long id);

    List<Address> findAll();

    List<Address> findByUser(User user);

    List<Address> findByCity(String city);

    List<Address> findByPostalCode(String postalCode);

    List<Address> findByCountry(String country);

    List<Address> findByStreet(String street);

}
