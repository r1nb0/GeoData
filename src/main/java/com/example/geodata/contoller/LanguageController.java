package com.example.geodata.contoller;

import com.example.geodata.entity.Language;
import com.example.geodata.dto.LanguageDTO;
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

    @GetMapping("/info/{id}")
    Optional<Language> getById(@PathVariable Integer id){
        return languageService.findById(id);
    }

    @PostMapping("/create")
    ResponseEntity<Language> addLanguage(@RequestBody LanguageDTO languageDTO){
        return new ResponseEntity<>(languageService.save(languageDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idLanguage}")
    HttpStatus deleteLanguage(@PathVariable Integer idLanguage){
        Boolean isExist = languageService.deleteById(idLanguage);
        if (Boolean.TRUE.equals(isExist)){
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PutMapping("/update_info")
    ResponseEntity<Language> updateInfo(@RequestBody LanguageDTO languageDTO){
        Language language = languageService.update(languageDTO);
        if (language == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(language, HttpStatus.OK);
    }

}
