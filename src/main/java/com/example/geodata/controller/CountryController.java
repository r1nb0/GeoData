package com.example.geodata.controller;


import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.Country;
import com.example.geodata.service.CountryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/countries")
@AllArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/all")
    public List<Country> getAll(){
        return countryService.getAll();
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Optional<Country>> findById(@PathVariable Integer id){
        try {
            return ResponseEntity.ok(countryService.findById(id));
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Country> addCountry(@RequestBody CountryDTO countryDTO){
        return new ResponseEntity<>(countryService.addCountryWithExistingLanguages(countryDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteCountryById(@PathVariable Integer id){
        try {
            countryService.deleteCountryById(id);
            return HttpStatus.OK;
        }catch(EntityNotFoundException e){
            return HttpStatus.NOT_FOUND;
        }
    }

    @PutMapping("/update_info")
    public ResponseEntity<Country> updateInfo(@RequestBody CountryDTO countryDTO){
        Country updateCountry = countryService.updateInfo(countryDTO);
        if (updateCountry == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updateCountry, HttpStatus.OK);
    }

    @PutMapping("/add_languages")
    public ResponseEntity<Country> addLanguages(@RequestBody CountryDTO countryDTO){
        Country updateCountry = countryService.addLanguage(countryDTO);
        if (updateCountry == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updateCountry, HttpStatus.OK);
    }

    @PutMapping("/delete_languages")
    public ResponseEntity<Country> deleteLanguages(@RequestBody CountryDTO countryDTO){
        Country updateCountry = countryService.deleteLanguage(countryDTO);
        if (updateCountry == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updateCountry, HttpStatus.OK);
    }

    @GetMapping("/info/countries_from_language/{languageName}")
    public List<Country> getCountriesFromLanguage(@PathVariable String languageName){
        return countryService.findAllCountriesContainingSpecifiedLanguage(languageName);
    }

}
