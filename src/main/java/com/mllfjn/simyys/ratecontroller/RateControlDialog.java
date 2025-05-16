package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.character.Character;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

class RateControlDialog extends Stage {
    final ReturnSelector[] selectors;
    final Map<Character, Integer> map;
    final Return[] result;

    public RateControlDialog(String title, String effect, Map<Character, Integer> map, double[] rates, Return[] result) {
        super();
        this.map = map;
        this.result = result;
        selectors = new ReturnSelector[map.size()];

        int i = 0;
        for (Character character : map.keySet()) {
            selectors[i++] = new ReturnSelector(character.name, rates[map.get(character)], effect);
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
        int i = 0;
        for (Character character : map.keySet()) {
            result[map.get(character)] = selectors[i++].getReturn();
        }
    }
}
