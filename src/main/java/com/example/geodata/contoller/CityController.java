package com.example.geodata.contoller;

import com.example.geodata.entity.dto.CityDTO;
import com.example.geodata.entity.City;
import com.example.geodata.service.CityService;
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
@RequestMapping("/api/v2/cities")
@AllArgsConstructor
public class CityController {

    private final CityService cityService;
    private final DistanceService distanceService;

    @GetMapping("/all")
    public List<City> getAll(){
        return cityService.getAll();
    }

    @GetMapping("/info/{nameCity}")
    public Optional<City> findByName(@PathVariable String nameCity){
        return cityService.findByName(nameCity);
    }

    @PostMapping("/create")
    public ResponseEntity<City> addCity(@RequestBody CityDTO cityDTO){
        return new ResponseEntity<>(cityService.addCityWithExistingCountry(cityDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{nameCity}")
    public HttpStatus deleteCityByName(@PathVariable String nameCity){
        Boolean isExist = cityService.deleteByName(nameCity);
        if (Boolean.TRUE.equals(isExist)) {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<City> updateInfo(@RequestBody City city){
        return new ResponseEntity<>(cityService.update(city), HttpStatus.OK);
    }

    @GetMapping("/distance/{firstCity}+{secondCity}")
    public ResponseEntity<Object> distance(@PathVariable String firstCity, @PathVariable String secondCity){
        Optional<City> first = cityService.findByName(firstCity);
        Optional<City> second = cityService.findByName(secondCity);
        if (first.isEmpty() || second.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objects = objectMapper.createObjectNode();
        objects.put("First city", firstCity);
        objects.put("Second city", secondCity);
        Double distance = distanceService.calculateDistance(first.get().getLatitude(), second.get().getLongitude(),
                second.get().getLatitude(), second.get().getLongitude());
        objects.put("Distance", distance.toString() + "km");
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

}
