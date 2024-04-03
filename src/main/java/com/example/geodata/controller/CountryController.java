package com.example.geodata.controller;

import com.example.geodata.aspects.AspectAnnotation;
import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.Country;
import com.example.geodata.exceptions.ResourceNotFoundException;
import com.example.geodata.service.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "CountryController")
@RestController
@RequestMapping("/api/v1/countries")
@AllArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/all")
    public ResponseEntity<List<Country>> getAll() {
        return ResponseEntity.ok(countryService.getAll());
    }

    @GetMapping("/info/{id}")
    @AspectAnnotation
    public ResponseEntity<Optional<Country>> findById(
            @PathVariable final Integer id
    )
            throws ResourceNotFoundException {
        return ResponseEntity.ok(countryService.findById(id));
    }

    @PostMapping("/create")
    @AspectAnnotation
    public ResponseEntity<Country> addCountry(
            @RequestBody final CountryDTO countryDTO
    ) {
        Country country = countryService.createCountry(countryDTO);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @AspectAnnotation
    public HttpStatus deleteCountryById(@PathVariable final Integer id)
            throws ResourceNotFoundException {
        countryService.deleteById(id);
        return HttpStatus.OK;
    }

    @PutMapping("/updateInfo")
    @AspectAnnotation
    public ResponseEntity<Country> updateInfo(
            @RequestBody final CountryDTO countryDTO
    )
            throws ResourceNotFoundException {
        return new ResponseEntity<>(countryService.updateInfo(countryDTO),
                HttpStatus.OK);
    }

    @PutMapping("/addLanguages")
    @AspectAnnotation
    public ResponseEntity<Country> addLanguages(
            @RequestBody final CountryDTO countryDTO
    )
            throws ResourceNotFoundException {
        return new ResponseEntity<>(countryService.addLanguage(countryDTO),
                HttpStatus.OK);
    }

    @PutMapping("/removeLanguages")
    @AspectAnnotation
    public ResponseEntity<Country> deleteLanguages(
            @RequestBody final CountryDTO countryDTO
    )
            throws ResourceNotFoundException {
        return new ResponseEntity<>(countryService.deleteLanguage(countryDTO),
                HttpStatus.OK);
    }

    @GetMapping("/info/countriesFromLanguage/{languageName}")
    public List<Country> getCountriesFromLanguage(
            @PathVariable final String languageName
    ) throws ResourceNotFoundException {
        return countryService
                .findCountriesWithSpecifiedLanguage(languageName);
    }

    @PostMapping("/bulkInsert")
    @AspectAnnotation
    public HttpStatus bulkInsert(@RequestBody final List<CountryDTO> countryDTOS) {
        countryService.bulkInsert(countryDTOS);
        return HttpStatus.OK;
    }

}
