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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CityService.class);

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
        if (city.isEmpty()) {
            city = cityRepository.findById(id);
            if (city.isEmpty()) {
                return Optional.empty();
            }
            cityCache.put(city.get().getId(), city.get());
            logger.info("City with id = {} retrieved from repository and added into the cache", id);
        } else {
            logger.info("City with id = {} retrieved from cache", id);
        }
        return city;
    }

    @Override
    public City addCityWithExistingCountry(CityDTO cityDTO) {
        Optional<Country> country = countryRepository.findById(cityDTO.getCountryId());
        if (country.isPresent()) {
            City city = City.builder()
                    .name(cityDTO.getName())
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
            if (cityDTO.getName() != null) {
                city.get().setName(cityDTO.getName());
            }
            City saveCity = cityRepository.save(city.get());
            countryCache.remove(saveCity.getCountry().getId());
            cityCache.put(saveCity.getId(), saveCity);
            return saveCity;
        }
        return null;
    }

}
