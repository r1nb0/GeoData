package com.example.geodata.service.impl;


import com.example.geodata.entity.Country;
import com.example.geodata.entity.Language;
import com.example.geodata.entity.dto.LanguageDTO;
import com.example.geodata.repository.LanguageRepository;
import com.example.geodata.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Override
    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    @Override
    public Language save(LanguageDTO languageDTO) {
        Language language = Language.builder()
                .name(languageDTO.getName())
                .countries(new ArrayList<>())
                .code(languageDTO.getCode())
                .build();
        return languageRepository.save(language);
    }

    @Override
    public Language update(Language language) {
        Optional<Language> languageExist = languageRepository.findById(language.getId());
        if (languageExist.isPresent()){
            if (language.getCode() != null){
                languageExist.get().setCode(language.getCode());
            }
            if (language.getName() != null){
                languageExist.get().setName(language.getName());
            }
            return languageRepository.save(languageExist.get());
        }
        return null;
    }

    @Override
    public Boolean deleteByName(String name) {
        Optional<Language> language = languageRepository.findByName(name);
        if (language.isPresent()) {
            List<Country> existingCountries = language.get().getCountries();
            for (Country country : existingCountries)
                country.removeLanguage(language.get());
            languageRepository.delete(language.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<Language> findByName(String name) {
        return languageRepository.findByName(name);
    }

}
