package com.example.geodata.contoller;


import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.Country;
import com.example.geodata.service.CityService;
import com.example.geodata.service.CountryService;
import com.example.geodata.service.DistanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private final CityService cityService;
    private final DistanceService distanceService;

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
        cityService.refreshCacheBeforeRemoveCountry(id);
        Boolean isExist = countryService.deleteCountryById(id);
        if (Boolean.TRUE.equals(isExist)){
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PutMapping("update_info")
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

    @GetMapping("/distance/{firstId}+{secondId}")
    public ResponseEntity<Object> distance(@PathVariable Integer firstId, @PathVariable Integer secondId){
        Optional<Country> first = countryService.findById(firstId);
        Optional<Country> second = countryService.findById(secondId);
        if (first.isEmpty() || second.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objects = objectMapper.createObjectNode();
        objects.put("First country", first.get().getName());
        objects.put("Second country", second.get().getName());
        Double distance = distanceService.calculateDistance(first.get().getLatitude(), second.get().getLongitude(),
                second.get().getLatitude(), second.get().getLongitude());
        objects.put("Distance", distance.toString() + "km");
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

}
