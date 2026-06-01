package com.mllfjn.simyys.ratecontroller;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class RangeControlMenuItem extends CustomMenuItem {
    private final CheckBox checkBox;
    private final Slider slider = new Slider(0.99, 1.01, 1);
    private final TextField tf = new TextField("1");

    private boolean isChanging = false;

    public RangeControlMenuItem(String text) {
        checkBox = new CheckBox(text);
        slider.valueProperty().addListener((obs, old, val) -> {
            if (!isChanging) {
                isChanging = true;
                tf.setText(String.valueOf(val.doubleValue()));
                isChanging = false;
            }
        });
        slider.setMajorTickUnit(0.01);
        slider.setMinorTickCount(9);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setPrefWidth(100);

        tf.textProperty().addListener((obs, old, val) -> {
            if (!isChanging && !val.isEmpty()) {
                double v = Double.parseDouble(val);
                if (v >= 0.99 && v <= 1.01) {
                    isChanging = true;
                    slider.setValue(v);
                    isChanging = false;
                }
            }
        });
        tf.setPrefWidth(50);

        setContent(new HBox(checkBox, tf, slider));
    }

    public BooleanProperty selectedProperty() {
        return checkBox.selectedProperty();
    }

    public double getValue() {
        return slider.getValue();
    }
}
