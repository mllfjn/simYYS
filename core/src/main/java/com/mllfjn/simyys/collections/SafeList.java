package com.mllfjn.simyys.collections;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class SafeList<E> implements Iterable<E>, Serializable {
    private final ArrayList<E> list = new ArrayList<>();
    private final Set<E> added = new HashSet<>();
    private final Set<E> removed = new HashSet<>();

    private int count;

    public @NotNull Iterator<E> iterator() {
        count++;
        return new SafeIterator(list.iterator());
    }

    public void endIterator() {
        count--;
        if (count == 0) {
            if (!removed.isEmpty()) {
                list.removeAll(removed);
                removed.clear();
            }
            if (!added.isEmpty()) {
                list.addAll(added);
                added.clear();
            }
        }
    }

    public void remove(E o) {
        if (count == 0) {
            list.remove(o);
        } else {
            removed.add(o);
        }
    }

    public void add(E e) {
        if (count == 0) {
            list.add(e);
        } else {
            added.add(e);
        }
    }

    public void removeIf(Predicate<E> predicate) {
        if (count == 0) {
            list.removeIf(predicate);
        } else {
            for (E e : list) {
                if (predicate.test(e)) {
                    removed.add(e);
                }
            }
        }
    }

    private class SafeIterator implements Iterator<E> {
        private final Iterator<E> it;

        private E current;

        SafeIterator(Iterator<E> it) {
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public E next() {
            current = it.next();
            return current;
        }

        @Override
        public void remove() {
            if (count == 0) {
                it.remove();
            } else {
                removed.add(current);
            }
        }
    }
}
