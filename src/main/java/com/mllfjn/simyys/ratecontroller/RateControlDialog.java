package com.mllfjn.simyys.ratecontroller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

class RateControlDialog extends Stage {
    Return[] returns;
    ReturnSelector[] selectors;
    public RateControlDialog(String title, String[] names, double[] rates, String effect ,Return[] returns) {
        super();
        this.returns = returns;
        this.selectors = new ReturnSelector[names.length];
        for (int i = 0; i < names.length; i++) {
            this.selectors[i] = new ReturnSelector(names[i], rates[i], effect);
        }

        setupWindowBehavior();
        setupUI();

        this.setTitle(title);
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
