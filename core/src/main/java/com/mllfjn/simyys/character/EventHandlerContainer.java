package com.mllfjn.simyys.character;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.io.Serializable;

public interface EventHandlerContainer extends Serializable {
    void handle(MouseEvent event);
}
