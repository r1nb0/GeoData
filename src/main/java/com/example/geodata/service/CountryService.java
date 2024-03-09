package com.example.geodata.service;

import com.example.geodata.entity.dto.CountryDTO;
import com.example.geodata.entity.Country;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CountryService {

    List<Country> getAll();

    Optional<Country> findByName(String name);

    Country addCountryWithExistingLanguages(CountryDTO countryDTO);

    Boolean deleteCountryByName(String name);

    Country deleteLanguage(CountryDTO countryDTO);

    Country update(CountryDTO countryDTO);

}
