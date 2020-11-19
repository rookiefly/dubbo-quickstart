package com.rookiefly.quickstart.dubbo.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KeyValues {

    private List<Object> keyValues = new ArrayList(20);

    public KeyValues() {

    }

    public KeyValues(Object... keyValues) {
        if (keyValues != null && keyValues.length > 0) {
            for (int i = 0; i < keyValues.length; ++i) {
                this.keyValues.add(keyValues[i]);
            }
        }

    }

    public List<Object> getKeyValues() {
        return this.keyValues;
    }

    public KeyValues addKeyValue(Object key, Object value) {
        this.keyValues.add(key);
        this.keyValues.add(value);
        return this;
    }

    public KeyValues addAllKeyValues(Map<String, Object> keyValueMap) {
        if (null != keyValueMap) {
            Iterator entryIterator = keyValueMap.entrySet().iterator();

            while (entryIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) entryIterator.next();
                this.keyValues.add(entry.getKey());
                this.keyValues.add(entry.getValue());
            }
        }
        return this;
    }

    public static KeyValues create(Object... keyValues) {
        return new KeyValues(keyValues);
    }
}