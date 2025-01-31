package com.mllfjn.simyys.customnode;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class CustomLabel extends Label {

    private static final int width = 75;
    private static final int height = 25;
    public CustomLabel(String text) {
        super(text);
        this.setAlignment(Pos.CENTER);
        this.setPrefSize(width, height);
    }
}
