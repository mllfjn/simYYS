package com.mllfjn.simyys.character.propertygetter;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableHolder implements Serializable {
    public final List<PropertiesHolder> list;
    public SerializableHolder(ObservableList<PropertiesHolder> items) {
        this.list = new ArrayList<>(items);
    }
}
