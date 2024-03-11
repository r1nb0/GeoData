package com.example.geodata.service.impl;

import com.example.geodata.entity.Country;
import com.example.geodata.entity.dto.CityDTO;
import com.example.geodata.entity.City;
import com.example.geodata.repository.CityRepository;
import com.example.geodata.repository.CountryRepository;
import com.example.geodata.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public Boolean deleteById(Integer id) {
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()){
            cityRepository.delete(city.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<City> findById(Integer id) {
        return cityRepository.findById(id);
    }

    @Override
    public City addCityWithExistingCountry(CityDTO cityDTO) {
        Optional<Country> country = countryRepository.findCountryByName(cityDTO.getCountryName());
        if (country.isPresent()) {
            City city = City.builder()
                    .name(cityDTO.getCityName())
                    .latitude(cityDTO.getLatitude())
                    .longitude(cityDTO.getLongitude())
                    .country(country.get())
                    .build();
            return cityRepository.save(city);
        }
        return null;
    }

    @Override
    public City replaceCountry(CityDTO cityDTO) {
        Optional<City> existCity = cityRepository.findById(cityDTO.getId());
        if (existCity.isPresent()){
            Optional<Country> existCountry = countryRepository.findCountryByName(cityDTO.getCountryName());
            if (existCountry.isPresent()) {
                existCity.get().setCountry(existCountry.get());
                return cityRepository.save(existCity.get());
            }
        }
        return null;
    }

    @Override
    public City update(CityDTO cityDTO) {
        Optional<City> cityExist = cityRepository.findById(cityDTO.getId());
        if (cityExist.isPresent()){
            if (cityDTO.getLatitude() != null){
                cityExist.get().setLatitude(cityDTO.getLatitude());
            }
            if (cityDTO.getLongitude() != null){
                cityExist.get().setLongitude(cityDTO.getLongitude());
            }
            if (cityDTO.getCityName() != null){
                cityExist.get().setName(cityDTO.getCityName());
            }
            return cityRepository.save(cityExist.get());
        }
        return null;
    }
}
