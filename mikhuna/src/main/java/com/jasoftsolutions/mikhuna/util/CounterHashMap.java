package com.jasoftsolutions.mikhuna.util;

import java.util.HashMap;

/**
 * Created by pc07 on 09/04/2014.
 */
public class CounterHashMap<K> extends HashMap<K, Integer> {

    @Override
    public Integer get(Object key) {
        Integer result = super.get(key);
        if (result==null) {
            result = 0;
            put((K)key, result);
        }
        return result;
    }

    public Integer inc(K key) {
        return add(key, 1);
    }

    public Integer add(K key, int value) {
        return put(key, get(key)+value);
    }

    public Integer substract(K key, int value) {
        return add(key, -value);
    }

    public Integer dec(K key) {
        return substract(key, 1);
    }
}
