package com.example.geodata.cache;

import com.example.geodata.cache.impl.LRUCache;
import com.example.geodata.entity.City;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CacheManagerCity {

    private final LRUCache<Integer, City> lruCache = new LRUCache<>(100);

    public void put(Integer key, City city){
        lruCache.put(key, city);
    }

    public Optional<City> get(Integer key){
        return lruCache.get(key);
    }

    public void remove(Integer key){
        lruCache.remove(key);
    }

}
