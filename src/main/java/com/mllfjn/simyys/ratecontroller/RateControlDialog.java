package com.mllfjn.simyys.ratecontroller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class RateControlDialog extends Stage {
    Return[] returns;
    ReturnSelector[] selectors;
    public RateControlDialog(List<String> names, List<Double> rates, String effect ,Return[] returns) {
        super();
        this.returns = returns;
        this.selectors = new ReturnSelector[names.size()];
        for (int i = 0; i < names.size(); i++) {
            this.selectors[i] = new ReturnSelector(names.get(i), rates.get(i), effect);
        }

        setupWindowBehavior();
        setupUI();

        this.showAndWait();
    }

    private void setupUI() {
        VBox container = new VBox(30, selectors);
        container.setPadding(new Insets(20));

        Button button = new Button("确定");
        button.setOnAction( event -> {
            handlerReturn();
            close();
        });
        container.getChildren().add(button);
        setScene(new Scene(container));
    }

    private void setupWindowBehavior() {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setOnCloseRequest(event -> handlerReturn());
    }

    private void handlerReturn() {
        for (int i = 0; i < selectors.length; i++) {
            returns[i] = selectors[i].getReturn();
        }
    }
}
