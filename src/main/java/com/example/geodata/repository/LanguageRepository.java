package com.example.geodata.repository;

import com.example.geodata.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    @Query(value = "SELECT * FROM languages obj where obj.language_name in (?1)", nativeQuery = true)
    List<Language> findByNames(@Param("1") List<String> names);

    @Query(value = "DELETE FROM countries_languages WHERE language_id = :languageId", nativeQuery = true)
    void removalOfLanguagesInCountries(@Param("languageId") Integer languageId);

}
