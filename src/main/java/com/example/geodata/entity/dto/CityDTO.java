package com.example.geodata.entity.dto;


import lombok.Data;

@Data
public class CityDTO {

    private Integer id;

    private String cityName;

    private String countryName;

    private Double longitude;

    private Double latitude;

}
