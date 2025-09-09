package com.mllfjn.simyys;

import javafx.scene.control.Alert;

public class Utils {
    public static void throwException(String text, Exception e) {
        e.printStackTrace(System.out);
        new Alert(Alert.AlertType.ERROR, text).show();
    }
}
