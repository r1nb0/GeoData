package com.example.geodata.controller;

import com.example.geodata.aspects.AspectAnnotation;
import com.example.geodata.dto.CityDTO;
import com.example.geodata.entity.City;
import com.example.geodata.service.CityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v2/cities")
@AllArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/all")
    public List<City> getAll() {
        return cityService.getAll();
    }

    @GetMapping("/info/{cityId}")
    @AspectAnnotation
    public ResponseEntity<Optional<City>> findById(@PathVariable final Integer cityId) {
        try {
            return ResponseEntity.ok(cityService.findById(cityId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/create")
    @AspectAnnotation
    public ResponseEntity<City> addCity(@RequestBody final CityDTO cityDTO) {
        City existCity = cityService.addCityWithExistingCountry(cityDTO);
        if (existCity == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(existCity, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cityId}")
    @AspectAnnotation
    public HttpStatus deleteCityById(@PathVariable final Integer cityId) {
        try {
            cityService.deleteById(cityId);
            return HttpStatus.OK;
        } catch (EntityNotFoundException e) {
            return HttpStatus.NOT_FOUND;
        }
    }

    @PutMapping("/change_country")
    @AspectAnnotation
    public ResponseEntity<City> changeCountry(@RequestBody final CityDTO cityDTO) {
        City city = cityService.replaceCountry(cityDTO);
        if (city == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @PutMapping("/update_info")
    @AspectAnnotation
    public ResponseEntity<City> updateInfo(@RequestBody final CityDTO cityDTO) {
        City city = cityService.update(cityDTO);
        if (city == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

}
