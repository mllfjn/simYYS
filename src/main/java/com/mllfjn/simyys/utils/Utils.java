package com.mllfjn.simyys.utils;

import javafx.scene.control.Alert;

public class Utils {
    public static void throwException(String text, Exception e) {
        e.printStackTrace(System.out);
        new Alert(Alert.AlertType.ERROR, text).show();
    }

    public static void information(String text) {
        new Alert(Alert.AlertType.INFORMATION, text).show();
    }
    public static double parseDoubleOrDefault(String text, double defaultValue) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public static int parseIntOrDefault(String text, int defaultValue) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
