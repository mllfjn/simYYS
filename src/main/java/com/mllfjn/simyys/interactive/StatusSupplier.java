package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.CrowdControl;
import com.mllfjn.simyys.character.status.Status;

import java.util.function.BiConsumer;

public class StatusSupplier {
    private final String statusName;
    private final Class<? extends Status> statusClass;
    private final BiConsumer<Character, Character> statusConsumer;

    public StatusSupplier(String statusName, Class<? extends Status> statusClass
            , BiConsumer<Character, Character> statusConsumer) {
        this.statusName = statusName;
        this.statusClass = statusClass;
        this.statusConsumer = statusConsumer;
    }

    public String getStatusName() {
        return statusName;
    }

    public void supply(Character from, Character to) {
        statusConsumer.accept(from, to);
    }

    public boolean isCrowdControl() {
        return CrowdControl.class.isAssignableFrom(statusClass);
    }
}
