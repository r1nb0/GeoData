package com.example.geodata.service;

import com.example.geodata.entity.Language;
import com.example.geodata.entity.dto.LanguageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LanguageService {

    List<Language> findAll();

    Language save(LanguageDTO language);

    Language update(Language language);

    Boolean deleteByName(String name);

    Optional<Language> findByName(String name);

}
