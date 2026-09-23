package com.mllfjn.simyys.collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class SerializableObservableList<E extends Serializable> implements Serializable, Iterable<E> {
    @Serial
    private static final long serialVersionUID = 3451015018844167861L;
    private final List<E> list;
    private transient ObservableList<E> observableList;

    public SerializableObservableList() {
        list = new ArrayList<>();
    }

    public SerializableObservableList(SerializableObservableList<E> other) {
        list = new ArrayList<>(other.list);
    }

    public ObservableList<E> getObservableList() {
        if (observableList == null) {
            observableList = FXCollections.observableList(list);
        }
        return FXCollections.unmodifiableObservableList(observableList);
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

    public E get(int index) {
        return list.get(index);
    }

    public void clear() {
        Objects.requireNonNullElse(observableList, list).clear();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return observableList == null ? list.subList(fromIndex, toIndex) : observableList.subList(fromIndex, toIndex);
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        return list.iterator();
    }
}
