package com.quest.etna.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Adress;
import com.quest.etna.model.User;

@Repository
public interface AdressRepository extends CrudRepository<Adress, Long> {

    Optional<Adress> findById(long id);

    List<Adress> findAll();

    List<Adress> findByUser(User user);

    List<Adress> findByCity(String city);

    List<Adress> findByPostalCode(String postalCode);

    List<Adress> findByCountry(String country);

    List<Adress> findByStreet(String street);

}
