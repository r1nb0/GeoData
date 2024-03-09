package com.example.geodata.contoller;


import com.example.geodata.entity.dto.CountryDTO;
import com.example.geodata.entity.Country;
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
    private final DistanceService distanceService;

    @GetMapping("/all")
    public List<Country> getAll(){
        return countryService.getAll();
    }

    @GetMapping("/info/{nameCountry}")
    public Optional<Country> findByName(@PathVariable String nameCountry){
        return countryService.findByName(nameCountry);
    }

    @PostMapping("/create")
    public ResponseEntity<Country> addCountry(@RequestBody CountryDTO countryDTO){
        return new ResponseEntity<>(countryService.addCountryWithExistingLanguages(countryDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{nameCountry}")
    public HttpStatus deleteCountryByName(@PathVariable String nameCountry){
        Boolean isExist = countryService.deleteCountryByName(nameCountry);
        if (Boolean.TRUE.equals(isExist)){
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PutMapping("/update_add")
    public ResponseEntity<Country> updateInfo(@RequestBody CountryDTO countryDTO){
        return new ResponseEntity<>(countryService.update(countryDTO), HttpStatus.OK);
    }

    @PutMapping("/update_delete")
    public ResponseEntity<Country> deleteLanguage(@RequestBody CountryDTO countryDTO){
        return new ResponseEntity<>(countryService.deleteLanguage(countryDTO), HttpStatus.OK);
    }

    @GetMapping("/distance/{firstCountry}+{secondCountry}")
    public ResponseEntity<Object> distance(@PathVariable String firstCountry, @PathVariable String secondCountry){
        Optional<Country> first = countryService.findByName(firstCountry);
        Optional<Country> second = countryService.findByName(secondCountry);
        if (first.isEmpty() || second.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objects = objectMapper.createObjectNode();
        objects.put("First country", firstCountry);
        objects.put("Second country", secondCountry);
        Double distance = distanceService.calculateDistance(first.get().getLatitude(), second.get().getLongitude(),
                second.get().getLatitude(), second.get().getLongitude());
        objects.put("Distance", distance.toString() + "km");
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }



}
