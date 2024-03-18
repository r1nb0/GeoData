package com.example.geodata.service;

import com.example.geodata.dto.CityDTO;
import com.example.geodata.entity.City;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CityService {

    List<City> getAll();

    Boolean deleteById(Integer id);

    Optional<City> findById(Integer id);

    City addCityWithExistingCountry(CityDTO cityDTO);

    City replaceCountry(CityDTO cityDTO);

    City update(CityDTO cityDTO);


}
