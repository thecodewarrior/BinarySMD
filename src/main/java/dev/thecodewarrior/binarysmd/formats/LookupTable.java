package dev.thecodewarrior.binarysmd.formats;

import java.util.*;

public class LookupTable<T> {
    private Map<T, Integer> map = new HashMap<>();
    private List<T> list = new ArrayList<>();

    public int size() {
        return list.size();
    }

    public List<T> values() {
        return Collections.unmodifiableList(list);
    }

    public void add(T value) {
        if (!map.containsKey(value)) {
            map.put(value, list.size());
            list.add(value);
        }
    }

    public T value(int index) {
        return list.get(index);
    }

    public int index(T value) {
        return map.get(value);
    }
}
