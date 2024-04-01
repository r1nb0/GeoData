package com.example.geodata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "language_name")
    private String name;

    @Column(name = "language_code")
    private String code;

    @ManyToMany(mappedBy = "languages",
            cascade = { CascadeType.MERGE, CascadeType.PERSIST },
            fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Country> countries = new HashSet<>();

    @Override
    public String toString() {
        return "id=" + id + ", " + "name=" + name + ", " + "code=" + code;
    }

}
