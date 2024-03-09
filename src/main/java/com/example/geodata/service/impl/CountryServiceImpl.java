package com.example.geodata.service.impl;

import com.example.geodata.entity.dto.CountryDTO;
import com.example.geodata.entity.Country;
import com.example.geodata.entity.Language;
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

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findByName(String name) {
        return countryRepository.findCountryByName(name);
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
            if (languageRepository.existsById(language.getId())) {
                country.addLanguage(language);
            }
        }
        return countryRepository.save(country);
    }

    @Override
    public Boolean deleteCountryByName(String name) {
        Optional<Country> country = countryRepository.findCountryByName(name);
        if (country.isPresent()) {
            countryRepository.delete(country.get());
            return true;
        }
        return false;
    }

    @Override
    public Country addLanguage(CountryDTO countryDTO) {
        Optional<Country> countryExist = countryRepository.findById(countryDTO.getId());
        if (countryExist.isPresent()){
            List<Language> languageExist = languageRepository.findByNames(countryDTO.getLanguages());
                for (Language language : languageExist){
                    countryExist.get().addLanguage(language);
                }
            return countryRepository.save(countryExist.get());
        }
        return null;
    }

    @Override
    public Country updateInfo(CountryDTO countryDTO) {
        Optional<Country> countryExist = countryRepository.findById(countryDTO.getId());
        if (countryExist.isPresent()) {
            if (countryDTO.getLongitude() != null) {
                countryExist.get().setLongitude(countryDTO.getLongitude());
            }
            if (countryDTO.getLatitude() != null) {
                countryExist.get().setLatitude(countryDTO.getLatitude());
            }
            if (countryDTO.getNationality() != null) {
                countryExist.get().setNationality(countryDTO.getNationality());
            }
            if (countryDTO.getName() != null) {
                countryExist.get().setName(countryDTO.getName());
            }
            return countryRepository.save(countryExist.get());
        }
        return null;
    }

    @Override
    public Country deleteLanguage(CountryDTO countryDTO){
        List<Language> languages = languageRepository.findByNames(countryDTO.getLanguages());
        Optional<Country> country = countryRepository.findById(countryDTO.getId());
        if (country.isPresent()) {
            for (Language language : languages) {
                country.get().removeLanguage(language);
            }
            return countryRepository.save(country.get());
        }
        return null;
    }
}
