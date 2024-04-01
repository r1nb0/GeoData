package com.example.geodata.service;

import com.example.geodata.entity.Language;
import com.example.geodata.dto.LanguageDTO;
import com.example.geodata.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LanguageService {

    List<Language> findAll();

    Language createLanguage(LanguageDTO language);

    Language update(LanguageDTO languageDTO)
            throws ResourceNotFoundException;

    void deleteById(Integer id)
            throws ResourceNotFoundException;

    Optional<Language> findById(Integer id)
            throws ResourceNotFoundException;

    void bulkInsert(List<LanguageDTO> languageDTOS);

}
