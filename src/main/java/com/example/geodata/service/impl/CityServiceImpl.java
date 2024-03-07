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
    public Optional<City> findByName(String name) {
        return cityRepository.findCityByName(name);
    }

    @Override
    public Boolean deleteByName(String name) {
        Optional<City> city = cityRepository.findCityByName(name);
        if (city.isPresent()){
            cityRepository.delete(city.get());
            return true;
        }
        return false;
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
    public City update(City city) {
        Optional<City> cityExist = cityRepository.findById(city.getId());
        if (cityExist.isPresent()){
            if (city.getLatitude() != null){
                cityExist.get().setLatitude(city.getLatitude());
            }
            if (city.getLongitude() != null){
                cityExist.get().setLongitude(city.getLongitude());
            }
            if (city.getName() != null){
                cityExist.get().setName(city.getName());
            }
            return cityRepository.save(cityExist.get());
        }
        return null;
    }
}
