package com.mllfjn.simyys.customnode;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;

public class CustomTextField extends TextField {
    private static final int width = 75;
    private static final int height = 25;
    public CustomTextField() {
        super();
        this.setAlignment(Pos.CENTER);
        this.setPrefSize(width, height);
        this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
        this.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                this.setText("") ;
            }
        });
    }
}
