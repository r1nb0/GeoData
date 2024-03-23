package com.example.geodata.controller;

import com.example.geodata.aspects.AspectAnnotation;
import com.example.geodata.dto.LanguageDTO;
import com.example.geodata.entity.Language;
import com.example.geodata.service.LanguageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/languages")
@AllArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping("/all")
    @AspectAnnotation
    ResponseEntity<List<Language>> getAll(){
        return new ResponseEntity<>(languageService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/info/{id}")
    @AspectAnnotation
    ResponseEntity<Optional<Language>> getById(@PathVariable Integer id){
        try {
            return ResponseEntity.ok(languageService.findById(id));
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/create")
    @AspectAnnotation
    ResponseEntity<Language> addLanguage(@RequestBody LanguageDTO languageDTO){
        return new ResponseEntity<>(languageService.save(languageDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idLanguage}")
    @AspectAnnotation
    HttpStatus deleteLanguage(@PathVariable Integer idLanguage){
        try {
            languageService.deleteById(idLanguage);
            return HttpStatus.OK;
        }catch(EntityNotFoundException e){
            return HttpStatus.NOT_FOUND;
        }
    }

    @PutMapping("/update_info")
    @AspectAnnotation
    ResponseEntity<Language> updateInfo(@RequestBody LanguageDTO languageDTO){
        Language language = languageService.update(languageDTO);
        if (language == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(language, HttpStatus.OK);
    }

}
