package com.mllfjn.simyys.ratecontroller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ChooseController<T> {
    private T result;
    private List<T> resultList;

    public ChooseController(String title, List<T> list, Function<T, String> stringGetter) {
        TilePane tp = new TilePane();
        tp.setPadding(new Insets(20));
        tp.setHgap(10);

        Stage stage = new Stage();
        stage.setScene(new Scene(tp));
        stage.setTitle(title);

        for (T t : list) {
            Button button = new Button(stringGetter.apply(t));
            button.setOnAction(event -> {
                result = t;
                stage.close();
            });
            tp.getChildren().add(button);
        }

        stage.showAndWait();
    }

    public ChooseController(String title, List<T> list, Function<T, String> stringGetter, int max) {
        TilePane tp = new TilePane();
        tp.setPadding(new Insets(20));
        tp.setHgap(10);

        Stage stage = new Stage();
        stage.setScene(new Scene(tp));
        stage.setTitle(title);

        for (T t : list) {
            ToggleButton button = new ToggleButton(stringGetter.apply(t));
            button.setOnAction(event -> {
                if (button.isSelected()) {
                    if (resultList == null) {
                        resultList = new ArrayList<>();
                    }
                    resultList.add(t);
                    if (resultList.size() == max) {
                        stage.close();
                    }
                } else {
                    resultList.remove(t);
                }
            });
            tp.getChildren().add(button);
        }

        stage.showAndWait();
    }

    public T getResult() {
        return result;
    }

    public List<T> getResultList() {
        if (resultList == null) {
            return new ArrayList<>();
        }
        return resultList;
    }
}
