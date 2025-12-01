package com.mllfjn.simyys.utils;

import javafx.scene.control.Alert;

import java.io.Serializable;

public class Utils {
    public static void throwException(String text, Throwable e) {
        System.out.println(e.getMessage());
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

    public static boolean isHaveNonSerializable(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Object[]) {
            for (Object o : (Object[]) obj) {
                if (isHaveNonSerializable(o)) {
                    return true;
                }
            }
            return false;
        } else {
            return !(obj instanceof Serializable);
        }
    }
}
