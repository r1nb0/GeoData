package com.example.geodata.service;

import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.Country;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CountryService {

    List<Country> getAll();

    Optional<Country> findByName(String name);

    Optional<Country> findById(Integer id);

    Country addCountryWithExistingLanguages(CountryDTO countryDTO);

    Boolean deleteCountryById(Integer id);

    Country deleteLanguage(CountryDTO countryDTO);

    Country addLanguage(CountryDTO countryDTO);

    Country updateInfo(CountryDTO countryDTO);

}
