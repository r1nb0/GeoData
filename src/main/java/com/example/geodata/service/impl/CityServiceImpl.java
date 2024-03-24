package com.example.geodata.service.impl;

import com.example.geodata.aspects.AspectAnnotation;
import com.example.geodata.cache.LRUCacheCity;
import com.example.geodata.cache.LRUCacheCountry;
import com.example.geodata.dto.CityDTO;
import com.example.geodata.entity.City;
import com.example.geodata.entity.Country;
import com.example.geodata.exceptions.BadRequestException;
import com.example.geodata.exceptions.ResourceNotFoundException;
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
    @AspectAnnotation
    public void deleteById(final Integer id)
            throws ResourceNotFoundException {
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()) {
            cityCache.remove(id);
            countryCache.remove(city.get().getCountry().getId());
            cityRepository.delete(city.get());
        } else {
            throw new ResourceNotFoundException("City with id :: "
                    + id + " not found.");
        }
    }

    @Override
    @AspectAnnotation
    public Optional<City> findById(final Integer id)
            throws ResourceNotFoundException {
        Optional<City> city = cityCache.get(id);
        if (city.isEmpty()) {
            city = cityRepository.findById(id);
            if (city.isEmpty()) {
                throw new ResourceNotFoundException("City with id :: "
                        + id + " not found.");
            }
            cityCache.put(city.get().getId(), city.get());
        }
        return city;
    }

    @Override
    @AspectAnnotation
    public City addCity(final CityDTO cityDTO)
            throws ResourceNotFoundException {
        Optional<Country> country = countryRepository
                .findCountryByName(cityDTO.countryName());
        if (country.isPresent()) {
            if (cityDTO.name() == null || cityDTO.longitude() == null
                    || cityDTO.latitude() == null) {
                throw new BadRequestException("All fields: "
                        + "[name, latitude, longitude]"
                        + "must be provided.");
            }
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
        } else {
            throw new ResourceNotFoundException("Country with name :: "
                    + cityDTO.countryName() + " not found.");
        }
    }

    @Override
    @AspectAnnotation
    public City replaceCountry(final CityDTO cityDTO)
            throws ResourceNotFoundException {
        Optional<City> city = cityRepository.findById(cityDTO.id());
        if (city.isPresent()) {
            Optional<Country> country = countryRepository
                    .findCountryByName(cityDTO.countryName());
            if (country.isPresent()) {
                city.get().setCountry(country.get());
                city = Optional.of(cityRepository.save(city.get()));
                cityCache.put(city.get().getId(), city.get());
                countryCache.remove(city.get().getCountry().getId());
                return city.get();
            } else {
                throw new ResourceNotFoundException("Country with name :: "
                        + cityDTO.countryName() + " not found.");
            }
        } else {
            throw new ResourceNotFoundException("City with id :: "
                    + cityDTO.id() + " not found.");
        }
    }

    @Override
    @AspectAnnotation
    public City update(final CityDTO cityDTO) throws ResourceNotFoundException {
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
            city = Optional.of(cityRepository.save(city.get()));
            countryCache.remove(city.get().getCountry().getId());
            cityCache.put(city.get().getId(), city.get());
            return city.get();
        } else {
            throw new ResourceNotFoundException("City with id :: "
                    + cityDTO.id() + " not found.");
        }
    }

}
