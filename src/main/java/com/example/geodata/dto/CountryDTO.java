package com.example.geodata.dto;

import java.util.List;

public record CountryDTO(Integer id, String name, String nationality, Double latitude, Double longitude,
                         List<String> languages) {

}
