package com.example.geodata.service;

import com.example.geodata.dto.CityDTO;
import com.example.geodata.entity.City;
import com.example.geodata.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CityService {

    List<City> getAll();

    void deleteById(Integer id)
            throws ResourceNotFoundException;

    Optional<City> findById(Integer id)
            throws ResourceNotFoundException;

    City addCity(CityDTO cityDTO)
            throws ResourceNotFoundException;

    City replaceCountry(CityDTO cityDTO)
            throws ResourceNotFoundException;

    City update(CityDTO cityDTO)
            throws ResourceNotFoundException;


}
