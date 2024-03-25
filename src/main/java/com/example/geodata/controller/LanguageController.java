package com.example.geodata.controller;

import com.example.geodata.aspects.AspectAnnotation;
import com.example.geodata.dto.LanguageDTO;
import com.example.geodata.entity.Language;
import com.example.geodata.exceptions.ResourceNotFoundException;
import com.example.geodata.service.LanguageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "LanguageController")
@RestController
@RequestMapping("/api/v1/languages")
@AllArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping("/all")
    @AspectAnnotation
    ResponseEntity<List<Language>> getAll() {
        return new ResponseEntity<>(languageService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/info/{id}")
    @AspectAnnotation
    ResponseEntity<Optional<Language>> getById(@PathVariable final Integer id)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(languageService.findById(id));
    }

    @PostMapping("/create")
    @AspectAnnotation
    ResponseEntity<Language> addLanguage(
            @RequestBody final LanguageDTO languageDTO
    ) {
        return new ResponseEntity<>(languageService.save(languageDTO),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idLanguage}")
    @AspectAnnotation
    HttpStatus deleteLanguage(@PathVariable final Integer idLanguage)
            throws ResourceNotFoundException {
        languageService.deleteById(idLanguage);
        return HttpStatus.OK;
    }

    @PutMapping("/updateInfo")
    @AspectAnnotation
    ResponseEntity<Language> updateInfo(
            @RequestBody final LanguageDTO languageDTO
    )
            throws ResourceNotFoundException {
        return new ResponseEntity<>(languageService.update(languageDTO),
                HttpStatus.OK);
    }

}
