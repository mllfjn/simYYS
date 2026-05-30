package com.mllfjn.simyys.character.status;

import javafx.scene.paint.Color;

public interface Displayable {
    String DELIMITER = " ";
    Color GOOD_COLOR = Color.ORANGE;
    Color BAD_COLOR = Color.RED;

    String getDisplayText();

    default Color getColor(StatusType type) {
        if (type == StatusType.DEBUFF) {
            return BAD_COLOR;
        } else {
            return null;
        }
    }
}
