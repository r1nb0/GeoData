package com.example.geodata.service.impl;

import com.example.geodata.service.DistanceService;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceImpl implements DistanceService {

    @Override
    public Double calculateDistance(Double latitudeFirst, Double latitudeSecond, Double longitudeFirst, Double longitudeSecond) {
        var radius = 6371;
        var dLat = Math.toRadians(latitudeSecond - latitudeFirst);
        var dLon = Math.toRadians(longitudeSecond - longitudeFirst);
        var a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(latitudeFirst)) *
                Math.cos(Math.toRadians(latitudeSecond)) *  Math.sin(dLon/2) * Math.sin(dLon/2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return radius * c;
    }

}
