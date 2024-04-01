package com.example.geodata.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CountryDTO(Integer id, String name,
                         String nationality, Double latitude,
                         Double longitude, List<String> languages) {

}
