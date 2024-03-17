package com.example.geodata.dto;

import lombok.Data;

import java.util.List;

@Data
public class CountryDTO {

    private Integer id;

    private String name;

    private String nationality;

    private Double latitude;

    private Double longitude;

    List<String> languages;

}
