package com.example.geodata.service.impl;

import com.example.geodata.cache.impl.LRUCache;
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
    private final LRUCache<Integer, City> cityCache = new LRUCache<>(10);

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public Boolean deleteById(Integer id) {
        if (cityRepository.existsById(id)) {
            cityCache.remove(id);
            cityRepository.deleteById(id);
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
               cityRepository.save(city.get());
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
            cityCache.put(saveCity.getId(), saveCity);
            return saveCity;
        }
        return null;
    }

    @Override
    public void cacheInvalidationFromCountries(Integer countryId) {
        Optional<Country> country = countryRepository.findById(countryId);
        if (country.isPresent()){
            for (City city : country.get().getCities()){
                cityCache.remove(city.getId());
            }
        }
    }


}
