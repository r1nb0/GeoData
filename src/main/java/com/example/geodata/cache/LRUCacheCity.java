package com.example.geodata.cache;

import com.example.geodata.cache.impl.LRUCache;
import com.example.geodata.entity.City;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LRUCacheCity {

    private final LRUCache<Integer, City> LRUCache = new LRUCache<>(100);

    public void put(Integer key, City city){
        LRUCache.put(key, city);
    }

    public Optional<City> get(Integer key){
        return LRUCache.get(key);
    }

    public void remove(Integer key){
        LRUCache.remove(key);
    }

}
