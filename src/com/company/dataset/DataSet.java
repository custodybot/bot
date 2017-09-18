package com.company.dataset;

import java.util.*;

public class DataSet<K extends Enum<K>> extends EnumMap<K, Object> {

    private List<DataChangeListener<K>> listeners = new ArrayList<>();
    protected Class<K> keyType;

    public DataSet(Class<K> keyType) {
        super(keyType);
        this.keyType = keyType;
    }

    public void registerListener(DataChangeListener<K> listener) {
        listeners.add(listener);
    }

    public void updateDataset(Map<String, Object> data) {
        if (!data.isEmpty()) {
            Arrays.stream(keyType.getEnumConstants())
                    .filter(key -> data.containsKey(key.name()))
                    .forEach(key -> put(key, data.get(key.name())));
            listeners.forEach(it -> it.dataChangeProcessing(this));

        }
    }

    @Override
    public void clear() {
        super.clear();
        listeners.forEach(it -> it.dataChangeProcessing(this));
    }
}
