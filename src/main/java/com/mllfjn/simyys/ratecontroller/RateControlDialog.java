package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.utils.DecimalFormatUtil;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Function;

class RateControlDialog extends Stage {
    private final ReturnSelector[] selectors;
    private final Boolean[] result;
    private final RateCalc calc;
    private final Label rateLabel = new Label("当前概率：100.00%");

    public <T> RateControlDialog(String title, String event, List<T> list, Function<T, String> stringGetter, double[] rates, Boolean[] result, int count, RateCalc calc) {
        super();
        this.result = result;
        this.calc = calc;
        selectors = new ReturnSelector[count];

        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(10);
        root.setVgap(10);

        int j = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i] == null) {
                selectors[j] = new ReturnSelector(root, j, stringGetter.apply(list.get(i)), rates[i], event, this::countCurrentRate);
                j++;
            }
        }

        Button button = new Button("确定");
        button.setOnAction( eventHandler -> {
            handlerReturn();
            close();
        });
        root.add(rateLabel, 0, count);
        root.add(button, 0, count + 1);

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
            if (result[i] == null) {
                result[i] = selectors[j++].getReturn();
            }
        }

        calc.change(countCurrentRate());
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
