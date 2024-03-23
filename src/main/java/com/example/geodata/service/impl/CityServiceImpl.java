package com.example.geodata.service.impl;

import com.example.geodata.aspects.AspectAnnotation;
import com.example.geodata.cache.LRUCacheCity;
import com.example.geodata.cache.LRUCacheCountry;
import com.example.geodata.entity.Country;
import com.example.geodata.dto.CityDTO;
import com.example.geodata.entity.City;
import com.example.geodata.repository.CityRepository;
import com.example.geodata.repository.CountryRepository;
import com.example.geodata.service.CityService;
import jakarta.persistence.EntityNotFoundException;
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
    @AspectAnnotation
    public void deleteById(final Integer id) {
        Optional<City> city = Optional.ofNullable(cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "City with id :: " + id + " not found."
                )));
        if (city.isPresent()) {
            cityCache.remove(id);
            countryCache.remove(city.get().getCountry().getId());
            cityRepository.delete(city.get());
        }
    }

    @Override
    @AspectAnnotation
    public Optional<City> findById(final Integer id) {
        Optional<City> city = cityCache.get(id);
        if (city.isEmpty()) {
            city = Optional.ofNullable(cityRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "City with id :: " + id + " not found.")));
            if (city.isEmpty()) {
                return Optional.empty();
            }
            cityCache.put(city.get().getId(), city.get());
        }
        return city;
    }

    @Override
    @AspectAnnotation
    public City addCityWithExistingCountry(final CityDTO cityDTO) {
        Optional<Country> country = countryRepository.findById(cityDTO.countryId());
        if (country.isPresent()) {
            City city = City.builder()
                    .name(cityDTO.name())
                    .latitude(cityDTO.latitude())
                    .longitude(cityDTO.longitude())
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
    @AspectAnnotation
    public City replaceCountry(final CityDTO cityDTO) {
        Optional<City> city = cityRepository.findById(cityDTO.id());
        if (city.isPresent()) {
            Optional<Country> country = countryRepository.findById(cityDTO.countryId());
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
    @AspectAnnotation
    public City update(final CityDTO cityDTO) {
        Optional<City> city = cityRepository.findById(cityDTO.id());
        if (city.isPresent()) {
            if (cityDTO.latitude() != null) {
                city.get().setLatitude(cityDTO.latitude());
            }
            if (cityDTO.longitude() != null) {
                city.get().setLongitude(cityDTO.longitude());
            }
            if (cityDTO.name() != null) {
                city.get().setName(cityDTO.name());
            }
            City saveCity = cityRepository.save(city.get());
            countryCache.remove(saveCity.getCountry().getId());
            cityCache.put(saveCity.getId(), saveCity);
            return saveCity;
        }
        return null;
    }

}
