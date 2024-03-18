package com.example.geodata.cache.impl;
import com.example.geodata.cache.Cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class LRUCache<K, V> implements Cache<K, V> {

    private final int capacity;
    protected HashMap<K, Node<K, V>> hashMap;
    protected LinkedList<Node<K, V>> linkedList;

    public LRUCache(int capacity){
        this.capacity = capacity;
        hashMap = new HashMap<>();
        linkedList = new LinkedList<>();
    }

    @Override
    public Optional<V> get(K key) {
        Optional<V> result = Optional.empty();
        if (containsKey(key)){
            final Node<K, V> node = hashMap.get(key);
            result = Optional.of(node.value);
            linkedList.remove(node);
            linkedList.addFirst(node);
        }
        return result;
    }

    @Override
    public int size() {
        return linkedList.size();
    }

    @Override
    public void put(K key, V value) {
        if (containsKey(key)){
            linkedList.remove(hashMap.get(key));
        }else{
            ensureCapacity();
        }
        final Node<K, V> newNode = new Node<>(key, value);
        hashMap.put(key, newNode);
        linkedList.addFirst(newNode);
    }

    private boolean isSizeExceeded(){
        return size() == capacity;
    }

    @Override
    public boolean containsKey(K key) {
        return hashMap.containsKey(key);
    }

    private void ensureCapacity(){
        if (isSizeExceeded()){
            final Node<K, V> executeNode = linkedList.removeLast();
            hashMap.remove(executeNode.key);
        }
    }

    public void remove(K key){
        if (containsKey(key)){
            linkedList.remove(hashMap.get(key));
            hashMap.remove(key);
        }
    }

    public record Node<K, V>(K key, V value) { }

}