package com.example.geodata.service;

import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.Country;
import com.example.geodata.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CountryService {

    List<Country> getAll();

    Optional<Country> findById(Integer id)
            throws ResourceNotFoundException;

    Country addCountry(CountryDTO countryDTO);

    void deleteCountryById(Integer id)
            throws ResourceNotFoundException;

    Country deleteLanguage(CountryDTO countryDTO)
            throws ResourceNotFoundException;

    Country addLanguage(CountryDTO countryDTO)
            throws ResourceNotFoundException;

    Country updateInfo(CountryDTO countryDTO)
            throws ResourceNotFoundException;

    List<Country> findCountriesWithSpecifiedLanguage(String name);

}
