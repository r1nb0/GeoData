package com.example.geodata.service;

import com.example.geodata.entity.dto.CityDTO;
import com.example.geodata.entity.City;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CityService {

    List<City> getAll();

    Optional<City> findByName(String name);

    Boolean deleteByName(String name);

    City addCityWithExistingCountry(CityDTO cityDTO);

    City update(City city);

}
