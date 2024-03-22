package com.example.geodata.service.impl;

import com.example.geodata.cache.LRUCacheCountry;
import com.example.geodata.cache.LRUCacheLanguage;
import com.example.geodata.entity.Country;
import com.example.geodata.entity.Language;
import com.example.geodata.dto.LanguageDTO;
import com.example.geodata.repository.LanguageRepository;
import com.example.geodata.service.LanguageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LRUCacheCountry countryCache;
    private final LRUCacheLanguage languageCache;

    @Override
    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    @Override
    public Language save(LanguageDTO languageDTO) {
        Language language = Language.builder()
                .name(languageDTO.name())
                .countries(new ArrayList<>())
                .code(languageDTO.code())
                .build();
        language = languageRepository.save(language);
        languageCache.put(language.getId(), language);
        return language;
    }

    @Override
    public Language update(LanguageDTO languageDTO) {
        Optional<Language> language = languageRepository.findById(languageDTO.id());
        if (language.isEmpty()){
            return null;
        }
        if (languageDTO.code() != null){
            language.get().setCode(languageDTO.code());
        }
        if (languageDTO.name() != null){
            language.get().setName(languageDTO.name());
        }
        Language saveLanguage = languageRepository.save(language.get());
        for (Country country : saveLanguage.getCountries()){
            countryCache.remove(country.getId());
        }
        languageCache.put(saveLanguage.getId(), saveLanguage);
        return saveLanguage;
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Language> language = Optional.ofNullable(findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Language with id = " + id + " not found."
                )));
        if (language.isPresent()) {
            List<Integer> countriesIds = languageRepository.deleteLanguageByIdAndReturnCountryIds(id);
            for (Integer countryId : countriesIds){
                countryCache.remove(countryId);
            }
            languageCache.remove(id);
            languageRepository.deleteById(id);
        }
    }

    @Override
    public Optional<Language> findById(Integer id) {
        Optional<Language> language = languageCache.get(id);
        if (language.isEmpty()){
            language = Optional.ofNullable(languageRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Language with id = " + id + " not found."
                    )));
            if (language.isEmpty()){
                return Optional.empty();
            }
            languageCache.put(language.get().getId(), language.get());
        }
        return language;
    }
}
