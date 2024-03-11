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

    @GetMapping("/info/{cityId}")
    public Optional<City> findByName(@PathVariable Integer cityId){
        return cityService.findById(cityId);
    }

    @PostMapping("/create")
    public ResponseEntity<City> addCity(@RequestBody CityDTO cityDTO){
        City existCity = cityService.addCityWithExistingCountry(cityDTO);
        if (existCity == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(existCity, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cityId}")
    public HttpStatus deleteCityByName(@PathVariable Integer cityId){
        Boolean isExist = cityService.deleteById(cityId);
        if (Boolean.TRUE.equals(isExist)) {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PutMapping("/change_country")
    public ResponseEntity<City> changeCountry(@RequestBody CityDTO cityDTO){
        City city = cityService.replaceCountry(cityDTO);
        if (city == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @PutMapping("/update_info")
    public ResponseEntity<City> updateInfo(@RequestBody CityDTO cityDTO){
        City city = cityService.update(cityDTO);
        if (city == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @GetMapping("/distance/{firstCityId}+{secondCityId}")
    public ResponseEntity<Object> distance(@PathVariable Integer firstCityId, @PathVariable Integer secondCityId){
        Optional<City> first = cityService.findById(firstCityId);
        Optional<City> second = cityService.findById(secondCityId);
        if (first.isEmpty() || second.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objects = objectMapper.createObjectNode();
        objects.put("First city", first.get().getName());
        objects.put("Second city", second.get().getName());
        Double distance = distanceService.calculateDistance(first.get().getLatitude(), second.get().getLongitude(),
                second.get().getLatitude(), second.get().getLongitude());
        objects.put("Distance", distance.toString() + "km");
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

}
