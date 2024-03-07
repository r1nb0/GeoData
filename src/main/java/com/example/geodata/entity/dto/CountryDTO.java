package com.example.geodata.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CountryDTO {

    private String name;

    private String nationality;

    private Double latitude;

    private Double longitude;

    List<String> languages;

}
