package com.mllfjn.simyys.ratecontroller;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.TilePane;

import java.util.List;
import java.util.function.Function;

public class ChooseDialog<T> extends Dialog<T> {
    T result;
    public ChooseDialog(String title, List<T> list, Function<T, String> stringGetter) {
        setTitle(title);
        setHeaderText("从以下选项中选取");

        TilePane tp = new TilePane();
        tp.setPadding(new Insets(100));
        tp.setHgap(20);

        for (T t : list) {
            Button button = new Button(stringGetter.apply(t));
            button.setOnAction(event -> {
                result = t;
                close();
            });
            tp.getChildren().add(button);
        }

        getDialogPane().setContent(tp);
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        setResultConverter(buttonType -> result);
    }
}
