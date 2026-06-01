package com.mllfjn.simyys.ratecontroller;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;

public class CustomCheckMenuItem extends CustomMenuItem {
    private final CheckBox checkBox;

    public CustomCheckMenuItem(String text) {
        checkBox = new CheckBox(text);
        setContent(checkBox);
    }

    public BooleanProperty selectedProperty() {
        return checkBox.selectedProperty();
    }
}
