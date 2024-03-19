package com.example.geodata.repository;

import com.example.geodata.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    @Query(value = "SELECT c.id, c.country_name, c.nationality, c.latitude, c.longitude FROM countries c " +
            "JOIN countries_languages cl ON c.id = cl.country_id " +
            "JOIN languages l ON cl.language_id = l.id " +
            "WHERE l.language_name = (?1)", nativeQuery = true)
    List<Country> findAllCountriesContainingSpecifiedLanguage(@Param("1") String name);

}
