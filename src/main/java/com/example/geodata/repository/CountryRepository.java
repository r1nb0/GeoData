package com.example.geodata.repository;

import com.example.geodata.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findCountryByName(String name);

}
