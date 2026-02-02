package com.mllfjn.simyys.customnode;

import com.mllfjn.simyys.utils.Utils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class CustomInputMenuItem {
    public static MenuItem of(String desc, Consumer<Double> consumer) {
        Text text = new Text(desc);
        TextField textField = new TextField();
        Button button = new Button("确定");
        button.setOnAction(event -> {
            double number = Utils.parseDoubleOrDefault(textField.getText(), -1);
            if (number > 0) {
                consumer.accept(number);
            }
        });
        HBox hBox = new HBox(text, textField, button);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        return new CustomMenuItem(hBox);
    }
}
