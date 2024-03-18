package com.example.geodata.service.impl;

import com.example.geodata.cache.LRUCacheCity;
import com.example.geodata.cache.LRUCacheCountry;
import com.example.geodata.entity.Country;
import com.example.geodata.dto.CityDTO;
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
    private final LRUCacheCity cityCache;
    private final LRUCacheCountry countryCache;

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public Boolean deleteById(Integer id) {
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()) {
            cityCache.remove(id);
            countryCache.remove(city.get().getCountry().getId());
            cityRepository.delete(city.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<City> findById(Integer id) {
        Optional<City> city = cityCache.get(id);
        if (city.isEmpty()){
            city = cityRepository.findById(id);
            city.ifPresent(value -> cityCache.put(value.getId(), value));
        }
        return city;
    }

    @Override
    public City addCityWithExistingCountry(CityDTO cityDTO) {
        Optional<Country> country = countryRepository.findById(cityDTO.getCountryId());
        if (country.isPresent()) {
            City city = City.builder()
                    .name(cityDTO.getCityName())
                    .latitude(cityDTO.getLatitude())
                    .longitude(cityDTO.getLongitude())
                    .country(country.get())
                    .build();
            city = cityRepository.save(city);
            cityCache.put(city.getId(), city);
            countryCache.remove(city.getCountry().getId());
            return city;
        }
        return null;
    }

    @Override
    public City replaceCountry(CityDTO cityDTO) {
       Optional<City> city = cityRepository.findById(cityDTO.getId());
       if (city.isPresent()) {
           Optional<Country> country = countryRepository.findById(cityDTO.getCountryId());
           if (country.isPresent()) {
               city.get().setCountry(country.get());
               City saveCity = cityRepository.save(city.get());
               cityCache.put(saveCity.getId(), saveCity);
               countryCache.remove(saveCity.getCountry().getId());
           }
       }
       return null;
    }

    @Override
    public City update(CityDTO cityDTO) {
        Optional<City> city = cityRepository.findById(cityDTO.getId());
        if (city.isPresent()) {
            if (cityDTO.getLatitude() != null) {
                city.get().setLatitude(cityDTO.getLatitude());
            }
            if (cityDTO.getLongitude() != null) {
                city.get().setLongitude(cityDTO.getLongitude());
            }
            if (cityDTO.getCityName() != null) {
                city.get().setName(cityDTO.getCityName());
            }
            City saveCity = cityRepository.save(city.get());
            countryCache.remove(saveCity.getCountry().getId());
            cityCache.put(saveCity.getId(), saveCity);
            return saveCity;
        }
        return null;
    }

}
