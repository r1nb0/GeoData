package com.example.geodata.service;

import com.example.geodata.entity.Language;
import com.example.geodata.dto.LanguageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LanguageService {

    List<Language> findAll();

    Language save(LanguageDTO language);

    Language update(LanguageDTO languageDTO);

    void deleteById(Integer id);

    Optional<Language> findById(Integer id);

}
