package com.example.geodata.contoller;


import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.Country;
import com.example.geodata.service.CountryService;
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
    public Optional<Country> findById(@PathVariable Integer id){
        return countryService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Country> addCountry(@RequestBody CountryDTO countryDTO){
        return new ResponseEntity<>(countryService.addCountryWithExistingLanguages(countryDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteCountryById(@PathVariable Integer id){
        Boolean isExist = countryService.deleteCountryById(id);
        if (Boolean.TRUE.equals(isExist)){
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
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
