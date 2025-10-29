package com.mllfjn.simyys.collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SerializableObservableList<E extends Serializable> implements Serializable, Iterable<E>{
    private final List<E> list = new ArrayList<>();
    private transient ObservableList<E> observableList;

    public ObservableList<E> getObservableList() {
        if (observableList == null) {
            observableList = FXCollections.observableList(list);
        }
        return observableList;
    }

    public boolean add(E e) {
        return observableList == null ? list.add(e) : observableList.add(e);
    }

    public void add(int index, E e) {
        Objects.requireNonNullElse(observableList, list).add(index, e);
    }

    public boolean remove(E e) {
        return observableList == null ? list.remove(e) : observableList.remove(e);
    }

    public E remove(int index) {
        return observableList == null ? list.remove(index) : observableList.remove(index);
    }

    public int size() {
        return list.size();
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        return list.iterator();
    }
}
