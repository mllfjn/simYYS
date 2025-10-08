package com.mllfjn.simyys.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableObservableList<E extends Serializable> implements Serializable{
    private final List<E> list = new ArrayList<>();
    private transient ObservableList<E> observableList;

    public ObservableList<E> getObservableList() {
        if (observableList == null) {
            observableList = FXCollections.observableList(list);
        }
        return observableList;
    }

    public List<E> getList() {
        return observableList == null ? list : observableList;
    }

    public boolean add(E e) {
        return observableList == null ? list.add(e) : observableList.add(e);
    }
}
