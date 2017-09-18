package com.company.dataset;

public interface DataChangeListener<K extends Enum<K>> {

    void dataChangeProcessing(DataSet<K> data);

}
