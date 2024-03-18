package com.example.geodata.cache;

import com.example.geodata.cache.impl.LRUCache;
import com.example.geodata.entity.Language;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CacheManagerLanguage {

    private final LRUCache<Integer, Language> lruCache = new LRUCache<>(100);

    public void put(Integer key, Language country){
        lruCache.put(key, country);
    }

    public Optional<Language> get(Integer key){
        return lruCache.get(key);
    }

    public void remove(Integer key){
        lruCache.remove(key);
    }

}
