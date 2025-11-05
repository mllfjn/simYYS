package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.utils.DecimalFormatUtil;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;

class RateControlDialog extends Stage {
    private final ReturnSelector[] selectors;
    private final Return[] result;
    private final boolean[] tbd;
    private final TotalRateCalc calc;
    private final Label rateLabel = new Label("当前概率：100.00%");

    <T> RateControlDialog(String title, String event, List<T> list, Function<T, String> stringGetter, boolean[] tbd, double[] rates, Return[] result, int length, TotalRateCalc calc) {
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
                selectors[i] = new ReturnSelector(root, i, stringGetter.apply(list.get(i)), rates[i], event, this::countCurrentRate);
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

        rateLabel.setText("当前概率：" + DecimalFormatUtil.df_2_2.format(currentRate));

        return currentRate;
    }
}
