package com.example.geodata.dto;


import lombok.Data;

@Data
public class CityDTO {

    private Integer id;

    private String cityName;

    private Integer countryId;

    private Double longitude;

    private Double latitude;

}
