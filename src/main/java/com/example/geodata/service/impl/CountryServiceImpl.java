package com.example.geodata.service.impl;

import com.example.geodata.cache.LRUCacheCountry;
import com.example.geodata.cache.LRUCacheCity;
import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.City;
import com.example.geodata.entity.Country;
import com.example.geodata.entity.Language;
import com.example.geodata.repository.CountryRepository;
import com.example.geodata.repository.LanguageRepository;
import com.example.geodata.service.CountryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;
    private final LRUCacheCity cityCache;
    private final LRUCacheCountry countryCache;
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findById(Integer id) {
        Optional<Country> country = countryCache.get(id);
        if (country.isEmpty()){
            country = countryRepository.findById(id);
            if (country.isEmpty()){
                return Optional.empty();
            }
            countryCache.put(country.get().getId(), country.get());
            logger.info("Country with id = {} retrieved from repository and added into the cache", id);
        }else{
            logger.info("Country with id = {} retrieved from cache", id);
        }
        return country;
    }

    @Override
    public Country addCountryWithExistingLanguages(CountryDTO countryDTO) {
        List<Language> languages = languageRepository.findByNames(countryDTO.languages());
        Country country = Country.builder()
                .name(countryDTO.name())
                .nationality(countryDTO.nationality())
                .latitude(countryDTO.latitude())
                .longitude(countryDTO.longitude())
                .languages(new HashSet<>())
                .cities(new ArrayList<>())
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
        Optional<Country> country = countryRepository.findById(id);
        if (country.isPresent()) {
            countryCache.remove(id);
            for (City city : country.get().getCities()){
                cityCache.remove(city.getId());
            }
            countryRepository.delete(country.get());
            return true;
        }
        return false;
    }

    @Override
        public Country addLanguage(CountryDTO countryDTO) {
            Optional<Country> country = countryRepository.findById(countryDTO.id());
            if (country.isPresent()){
                List<Language> languageExist = languageRepository.findByNames(countryDTO.languages());
                for (Language language : languageExist){
                    country.get().addLanguage(language);
                }
                Country saveCountry = countryRepository.save(country.get());
                countryCache.put(saveCountry.getId(), saveCountry);
                return saveCountry;
            }
            return null;
    }

    @Override
    public Country deleteLanguage(CountryDTO countryDTO){
        Optional<Country> country = countryRepository.findById(countryDTO.id());
        if (country.isPresent()) {
            List<Language> languages = languageRepository.findByNames(countryDTO.languages());
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
        Optional<Country> country = countryRepository.findById(countryDTO.id());
        if (country.isPresent()) {
            if (countryDTO.longitude() != null) {
                country.get().setLongitude(countryDTO.longitude());
            }
            if (countryDTO.latitude() != null) {
                country.get().setLatitude(countryDTO.latitude());
            }
            if (countryDTO.nationality() != null) {
                country.get().setNationality(countryDTO.nationality());
            }
            if (countryDTO.name() != null) {
                country.get().setName(countryDTO.name());
            }
            Country saveCountry = countryRepository.save(country.get());
            countryCache.put(saveCountry.getId(), saveCountry);
            return saveCountry;
        }
        return null;
    }

    @Override
    public List<Country> findAllCountriesContainingSpecifiedLanguage(String name) {
        return countryRepository.findAllCountriesContainingSpecifiedLanguage(name);
    }

}