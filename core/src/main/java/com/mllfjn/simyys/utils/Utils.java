package com.mllfjn.simyys.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Utils {
    public static void throwException(String text, Throwable e) {
        System.out.println(e.getMessage());
        e.printStackTrace(System.out);
        error(text);
    }

    public static void error(String text) {
        new Alert(Alert.AlertType.ERROR, text).show();
    }

    public static void error(String text, String solveText, Runnable solve) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text);
        ButtonType btn = new ButtonType(solveText);
        alert.getButtonTypes().add(btn);

        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(btn)) {
            solve.run();
        }
    }

    public static void information(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.getDialogPane().getScene().setOnKeyPressed(event -> alert.close());

        alert.show();
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
