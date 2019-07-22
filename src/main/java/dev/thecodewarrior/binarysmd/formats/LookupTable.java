package dev.thecodewarrior.binarysmd.formats;

import java.util.*;

public class LookupTable<T> {
    private Set<T> existing = new HashSet<>();
    private Map<T, Integer> map = new HashMap<>();
    private List<T> list = new ArrayList<>();
    private boolean singles;

    public LookupTable() {
        this(true);
    }

    public LookupTable(boolean singles) {
        this.singles = singles;
    }

    public int size() {
        return list.size();
    }

    public List<T> values() {
        return Collections.unmodifiableList(list);
    }

    public void add(T value) {
        if((singles || !existing.add(value)) && !map.containsKey(value)) {
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
