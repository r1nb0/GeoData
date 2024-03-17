package com.example.geodata.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "country_name")
    private String name;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToMany(mappedBy = "country", cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<City> cities = new ArrayList<>();

   @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
   @JoinTable(name = "countries_languages",
                joinColumns = { @JoinColumn(name = "country_id") },
                inverseJoinColumns = { @JoinColumn(name = "language_id") })
   @JsonManagedReference
   Set<Language> languages = new HashSet<>();

   public void addLanguage(Language language){
       Integer id = language.getId();
       for (var i : languages){
           if (i.getId().equals(id)){
               return;
           }
       }
       languages.add(language);
   }

   public void removeLanguage(Language language) {
       Integer id = language.getId();
       Iterator<Language> iterator = languages.iterator();
       while (iterator.hasNext()) {
           Language i = iterator.next();
           if (i.getId().equals(id)) {
               iterator.remove();
               break;
           }
       }
   }

}
