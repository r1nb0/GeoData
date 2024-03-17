package com.example.geodata.service.impl;


import com.example.geodata.cache.impl.LRUCache;
import com.example.geodata.entity.Country;
import com.example.geodata.entity.Language;
import com.example.geodata.dto.LanguageDTO;
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
    private final LRUCache<Integer, Language> languageCache = new LRUCache<>(10);

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
        language = languageRepository.save(language);
        languageCache.put(language.getId(), language);
        return language;
    }

    @Override
    public Language update(LanguageDTO languageDTO) {
        Optional<Language> language = getLanguageFromCacheOrRepositoryById(languageDTO.getId(), false);
        if (language.isEmpty()){
            return null;
        }
        if (languageDTO.getCode() != null){
            language.get().setCode(languageDTO.getCode());
        }
        if (languageDTO.getName() != null){
            language.get().setName(languageDTO.getName());
        }
        return languageRepository.save(language.get());
    }

    @Override
    public Boolean deleteById(Integer id) {
        Optional<Language> language = getLanguageFromCacheOrRepositoryById(id , true);
        if (language.isEmpty()){
            return false;
        }
        List<Country> existingCountries = language.get().getCountries();
        for (Country country : existingCountries)
            country.removeLanguage(language.get());
        languageCache.remove(id);
        languageRepository.delete(language.get());
        return true;
    }

    @Override
    public Optional<Language> findById(Integer id) {
        return getLanguageFromCacheOrRepositoryById(id, false);
    }

    private Optional<Language> getLanguageFromCacheOrRepositoryById(Integer id, Boolean isErase){
        Optional<Language> language = languageCache.get(id);
        if (language.isEmpty()){
            language = languageRepository.findById(id);
            if (language.isEmpty()){
                return Optional.empty();
            }
            if (Boolean.FALSE.equals(isErase)) {
                languageCache.put(id, language.get());
            }
        }
        return language;
    }

}
