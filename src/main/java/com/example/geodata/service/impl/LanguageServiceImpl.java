package com.example.geodata.service.impl;


import com.example.geodata.cache.impl.LRUCache;
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
        Optional<Language> language = languageRepository.findById(languageDTO.getId());
        if (language.isEmpty()){
            return null;
        }
        if (languageDTO.getCode() != null){
            language.get().setCode(languageDTO.getCode());
        }
        if (languageDTO.getName() != null){
            language.get().setName(languageDTO.getName());
        }
        Language saveLanguage = languageRepository.save(language.get());
        languageCache.put(saveLanguage.getId(), saveLanguage);
        return saveLanguage;
    }

    @Override
    public Boolean deleteById(Integer id) {
        Optional<Language> language = findById(id);
        if (language.isPresent()) {
            languageRepository.removalOfLanguagesInCountries(id);
            languageCache.remove(id);
            languageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Language> findById(Integer id) {
        Optional<Language> language = languageCache.get(id);
        if (language.isEmpty()){
            language = languageRepository.findById(id);
            language.ifPresent(value -> languageCache.put(value.getId(), value));
        }
        return language;
    }
}
