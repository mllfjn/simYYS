package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.SerializableItems;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.utils.serializable.SerialFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusAdder<T extends Status> implements Serializable {
    private final SerializableItems si;

    private final SerialFunction<Character, T> statusProvider;
    private final List<T> addedStatuses = new ArrayList<>();

    public StatusAdder(SerializableItems si, SerialFunction<Character, T> statusProvider) {
        this.si = si;
        this.statusProvider = statusProvider;
    }

    public void characterAdd(Character character) {
        T status = statusProvider.apply(character);
        if (status != null) {
            character.addStatus(status);
            addedStatuses.add(status);
        }
    }

    public void deleteAndRemove() {
        for (T status : addedStatuses) {
            status.delete();
        }
        si.removeStatusAdder(this);
        addedStatuses.clear();
    }

    public List<T> getList() {
        return addedStatuses;
    }
}
