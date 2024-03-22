package com.example.geodata.contoller;

import com.example.geodata.dto.CityDTO;
import com.example.geodata.entity.City;
import com.example.geodata.service.CityService;
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
    public List<City> getAll(){
        return cityService.getAll();
    }

    @GetMapping("/info/{cityId}")
    public Optional<City> findById(@PathVariable Integer cityId){
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
    public HttpStatus deleteCityById(@PathVariable Integer cityId){
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

}
