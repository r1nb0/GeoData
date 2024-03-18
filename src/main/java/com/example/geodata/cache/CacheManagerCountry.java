package com.example.geodata.cache;

import com.example.geodata.cache.impl.LRUCache;
import com.example.geodata.entity.Country;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CacheManagerCountry {

    private final LRUCache<Integer, Country> lruCache = new LRUCache<>(100);

    public void put(Integer key, Country country){
        lruCache.put(key, country);
    }

    public Optional<Country> get(Integer key){
        return lruCache.get(key);
    }

    public void remove(Integer key){
        lruCache.remove(key);
    }

}
