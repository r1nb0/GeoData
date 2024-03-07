package com.example.geodata.service;

import org.springframework.stereotype.Service;

@Service
public interface DistanceService {

    Double calculateDistance(Double latitudeFirst, Double latitudeSecond, Double longitudeFirst, Double longitudeSecond);

}
