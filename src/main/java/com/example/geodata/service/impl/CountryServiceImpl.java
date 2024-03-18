package com.example.geodata.service.impl;

import com.example.geodata.cache.impl.LRUCache;
import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.City;
import com.example.geodata.entity.Country;
import com.example.geodata.entity.Language;
import com.example.geodata.repository.CityRepository;
import com.example.geodata.repository.CountryRepository;
import com.example.geodata.repository.LanguageRepository;
import com.example.geodata.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;
    private final CityRepository cityRepository;
    private final LRUCache<Integer, Country> countryCache = new LRUCache<>(10);

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findById(Integer id) {
        Optional<Country> country = countryCache.get(id);
        if (country.isEmpty()){
            country = countryRepository.findById(id);
            country.ifPresent(value -> countryCache.put(value.getId(), value));
        }
        return country;
    }

    @Override
    public Country addCountryWithExistingLanguages(CountryDTO countryDTO) {
        List<Language> languages = languageRepository.findByNames(countryDTO.getLanguages());
        Country country = Country.builder()
                .name(countryDTO.getName())
                .nationality(countryDTO.getNationality())
                .latitude(countryDTO.getLatitude())
                .longitude(countryDTO.getLongitude())
                .languages(new HashSet<>())
                .build();
        for (Language language : languages) {
            country.addLanguage(language);
        }
        country = countryRepository.save(country);
        countryCache.put(country.getId(), country);
        return country;
    }

    @Override
    public Boolean deleteCountryById(Integer id) {
        if (countryRepository.existsById(id)) {
            countryCache.remove(id);
            countryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Country addLanguage(CountryDTO countryDTO) {
        Optional<Country> country = countryRepository.findById(countryDTO.getId());
        if (country.isPresent()){
            List<Language> languageExist = languageRepository.findByNames(countryDTO.getLanguages());
            for (Language language : languageExist){
                country.get().addLanguage(language);
            }
            Country saveCountry = countryRepository.save(country.get());
            countryCache.put(saveCountry.getId(), saveCountry);
            return saveCountry;}
        return null;
    }

    @Override
    public Country deleteLanguage(CountryDTO countryDTO){
        Optional<Country> country = countryRepository.findById(countryDTO.getId());
        if (country.isPresent()) {
            List<Language> languages = languageRepository.findByNames(countryDTO.getLanguages());
            for (Language language : languages) {
                country.get().removeLanguage(language);
            }
            Country saveCountry =  countryRepository.save(country.get());
            countryCache.put(saveCountry.getId(), saveCountry);
            return saveCountry;
        }
        return null;
    }

    @Override
    public Country updateInfo(CountryDTO countryDTO) {
        Optional<Country> country = countryRepository.findById(countryDTO.getId());
        if (country.isPresent()) {
            if (countryDTO.getLongitude() != null) {
                country.get().setLongitude(countryDTO.getLongitude());
            }
            if (countryDTO.getLatitude() != null) {
                country.get().setLatitude(countryDTO.getLatitude());
            }
            if (countryDTO.getNationality() != null) {
                country.get().setNationality(countryDTO.getNationality());
            }
            if (countryDTO.getName() != null) {
                country.get().setName(countryDTO.getName());
            }
            Country saveCountry = countryRepository.save(country.get());
            countryCache.put(saveCountry.getId(), saveCountry);
            return saveCountry;
        }
        return null;
    }

    @Override
    public void cacheInvalidationFromLanguages(Integer languageId) {
        Optional<Language> language = languageRepository.findById(languageId);
        if (language.isPresent()){
            for (Country country : language.get().getCountries()){
                countryCache.remove(country.getId());
            }
        }
    }

    @Override
    public void cacheInvalidationFromCities(Integer cityId) {
        Optional<City> city = cityRepository.findById(cityId);
        city.ifPresent(value -> countryCache.remove(value.getId()));
    }


}
