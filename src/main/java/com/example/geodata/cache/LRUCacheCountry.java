package com.example.geodata.cache;

import com.example.geodata.cache.impl.LRUCache;
import com.example.geodata.entity.Country;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LRUCacheCountry {

    private final LRUCache<Integer, Country> LRUCache = new LRUCache<>(100);

    public void put(Integer key, Country country){
        LRUCache.put(key, country);
    }

    public Optional<Country> get(Integer key){
        return LRUCache.get(key);
    }

    public void remove(Integer key){
        LRUCache.remove(key);
    }

}
