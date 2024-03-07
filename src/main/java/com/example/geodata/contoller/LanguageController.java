package com.example.geodata.contoller;

import com.example.geodata.entity.Language;
import com.example.geodata.service.LanguageService;
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
    ResponseEntity<List<Language>> getAll(){
        return new ResponseEntity<>(languageService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/find/{languageName}")
    Optional<Language> getByName(@PathVariable String languageName){
        return languageService.findByName(languageName);
    }

    @PostMapping("/create")
    ResponseEntity<Language> addLanguage(@RequestBody Language language){
        return new ResponseEntity<>(languageService.save(language), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{nameLanguage}")
    HttpStatus deleteLanguage(@PathVariable String nameLanguage){
        if (languageService.deleteByName(nameLanguage)){
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PutMapping("/update")
    ResponseEntity<Language> updateInfo(@RequestBody Language language){
        return new ResponseEntity<>(languageService.update(language), HttpStatus.OK);
    }

}
