package com.example.geodata.service.impl;

import com.example.geodata.aspects.AspectAnnotation;
import com.example.geodata.cache.LRUCacheCity;
import com.example.geodata.cache.LRUCacheCountry;
import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.City;
import com.example.geodata.entity.Country;
import com.example.geodata.entity.Language;
import com.example.geodata.exceptions.BadRequestException;
import com.example.geodata.exceptions.ResourceNotFoundException;
import com.example.geodata.repository.CountryRepository;
import com.example.geodata.repository.LanguageRepository;
import com.example.geodata.repository.bulk.CountryBulkRepository;
import com.example.geodata.service.CountryService;
import com.example.geodata.service.utility.CountryDTOUtility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;
    private final LRUCacheCity cityCache;
    private final LRUCacheCountry countryCache;
    private final CountryBulkRepository countryBulkRepository;
    private static final String NO_EXIST = "Country don't exist with id =";

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    @AspectAnnotation
    public Optional<Country> findById(final Integer id)
            throws ResourceNotFoundException {
        Optional<Country> country = countryCache.get(id);
        if (country.isEmpty()) {
            country = countryRepository.findById(id);
            if (country.isEmpty()) {
                throw new ResourceNotFoundException(NO_EXIST + " " + id);
            }
            country.ifPresent(value -> countryCache.put(value.getId(), value));
        }
        return country;
    }

    @Override
    @AspectAnnotation
    public Country createCountry(final CountryDTO countryDTO) {
        if (Boolean.TRUE.equals(countryRepository
                .existsByName(countryDTO.name()))) {
            throw new BadRequestException("Country with name :: "
                    + countryDTO.name() + " is already exist.");
        }
        if (countryDTO.name() == null || countryDTO.longitude() == null
                || countryDTO.latitude() == null
                || countryDTO.nationality() == null) {
            throw new BadRequestException("All fields: "
                    + "[name, nationality, latitude, longitude]"
                    + "must be provided.");
        }
        Country country = CountryDTOUtility
                .buildCountryFromCountryDTO(countryDTO);
        country = countryRepository.save(country);
        countryCache.put(country.getId(), country);
        return country;
    }

    @Override
    @AspectAnnotation
    public void deleteCountryById(final Integer id)
            throws ResourceNotFoundException {
        Optional<Country> country = countryRepository.findById(id);
        if (country.isPresent()) {
            countryCache.remove(id);
            for (City city : country.get().getCities()) {
                cityCache.remove(city.getId());
            }
            countryRepository.delete(country.get());
        }
        throw new ResourceNotFoundException(NO_EXIST + " " + id);
    }

    @Override
    @AspectAnnotation
        public Country addLanguage(final CountryDTO countryDTO)
            throws ResourceNotFoundException {
            Optional<Country> country =
                    countryRepository.findById(countryDTO.id());
            if (country.isPresent()) {
                List<Language> languageExist = languageRepository
                        .findByNames(countryDTO.languages());
                if (languageExist.isEmpty()) {
                    throw new BadRequestException("List of languages"
                            + " for adding is empty"
                            + " or these languages don't exist.");
                }
                for (Language language : languageExist) {
                    country.get().addLanguage(language);
                }
                country = Optional.of(countryRepository.save(country.get()));
                countryCache.put(country.get().getId(), country.get());
                return country.get();
            }
            throw new ResourceNotFoundException(NO_EXIST
                    + " " + countryDTO.id());
    }

    @Override
    @AspectAnnotation
    public Country deleteLanguage(final CountryDTO countryDTO)
            throws ResourceNotFoundException {
        Optional<Country> country = countryRepository
                .findById(countryDTO.id());
        if (country.isPresent()) {
            List<Language> languages = languageRepository
                    .findByNames(countryDTO.languages());
            if (languages.isEmpty()) {
                throw new BadRequestException("List of languages"
                        + " for deleting is empty"
                        + " or these languages don't exist.");
            }
            for (Language language : languages) {
                country.get().removeLanguage(language);
            }
            country = Optional.of(countryRepository.save(country.get()));
            countryCache.put(country.get().getId(), country.get());
            return country.get();
        }
        throw new ResourceNotFoundException(NO_EXIST + " " + countryDTO.id());
    }

    @Override
    @AspectAnnotation
    public Country updateInfo(final CountryDTO countryDTO)
            throws ResourceNotFoundException {
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
            country = Optional.of(countryRepository.save(country.get()));
            countryCache.put(country.get().getId(), country.get());
            return country.get();
        }
        throw new ResourceNotFoundException(NO_EXIST + " " + countryDTO.id());
    }

    @Override
    public List<Country> findCountriesWithSpecifiedLanguage(final String name) {
        return countryRepository
                .findAllCountriesContainingSpecifiedLanguage(name);
    }

    @Transactional
    @Override
    public void bulkInsert(List<CountryDTO> countryDTOS) {
        List<Country> countries = new ArrayList<>();
        for (CountryDTO countryDTO : countryDTOS) {
            Country country = CountryDTOUtility
                    .buildCountryFromCountryDTO(countryDTO);
            countries.add(country);
        }
        countryBulkRepository.bulkInsert(countries);
    }

}
