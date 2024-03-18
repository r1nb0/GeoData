package com.example.geodata.cache;

import com.example.geodata.cache.impl.LRUCache;
import com.example.geodata.entity.Language;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LRUCacheLanguage {

    private final LRUCache<Integer, Language> LRUCache = new LRUCache<>(100);

    public void put(Integer key, Language country){
        LRUCache.put(key, country);
    }

    public Optional<Language> get(Integer key){
        return LRUCache.get(key);
    }

    public void remove(Integer key){
        LRUCache.remove(key);
    }

}
