package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.character.Character;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.List;

class RateControlDialog extends Stage {
    private final ReturnSelector[] selectors;
    private final Return[] result;
    private final boolean[] tbd;
    private final TotalRateCalc calc;
    private final Label rateLabel = new Label("当前概率：100.00%");
    private static final DecimalFormat df = new DecimalFormat("0.00##%");


    RateControlDialog(String title, String event, List<Character> list, boolean[] tbd, double[] rates, Return[] result, int length, TotalRateCalc calc) {
        super();
        this.result = result;
        this.tbd = tbd;
        this.calc = calc;
        selectors = new ReturnSelector[length];

        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(10);
        root.setVgap(10);

        for (int i = 0; i < length; i++) {
            if (tbd[i]) {
                selectors[i] = new ReturnSelector(root, i, list.get(i).name, rates[i], event, this::countCurrentRate);
            }
        }
        Button button = new Button("确定");
        button.setOnAction( eventHandler -> {
            handlerReturn();
            close();
        });
        root.add(rateLabel, 0, length);
        root.add(button, 0, length + 1);

        setScene(new Scene(root));

        setupWindowBehavior();

        this.setTitle(title);
        this.showAndWait();
    }

    private void setupWindowBehavior() {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setOnCloseRequest(event -> handlerReturn());
    }

    private void handlerReturn() {
        int j = 0;
        for (int i = 0; i < selectors.length; i++) {
            if (tbd[i]) {
                result[i] = selectors[j++].getReturn();
            }
        }

        calc.add(countCurrentRate());
    }

    private double countCurrentRate(){
        double currentRate = 1;
        for (ReturnSelector selector : selectors) {
            currentRate *= selector.getCurrentRate();
        }

        rateLabel.setText("当前概率：" + df.format(currentRate));

        return currentRate;
    }
}
