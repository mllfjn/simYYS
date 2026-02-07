package com.mllfjn.simyys.collections;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SafeList<E> extends ArrayList<E> {
    private int count;

    private final Set<E> added = new HashSet<>();
    private final Set<E> removed = new HashSet<>();

    @Override
    public @NotNull Iterator<E> iterator() {
        count++;
        return super.iterator();
    }

    public void endIterator() {
        count--;
        if (count == 0) {
            if (!removed.isEmpty()) {
                super.removeAll(removed);
                removed.clear();
            }
            if (!added.isEmpty()) {
                super.addAll(added);
                added.clear();
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public void safeRemove(E o) {
        if (count == 0) {
            super.remove(o);
        } else {
            removed.add(o);
        }
    }

    @Override
    public boolean add(E e) {
        if (count == 0) {
            super.add(e);
        } else {
            added.add(e);
        }
        return false;
    }
}
