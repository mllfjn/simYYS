package com.mllfjn.simyys;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CustomException {
    public static void throwException(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text);
        alert.show();
    }
}
