package com.mllfjn.simyys.ratecontroller;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class ReturnSelector {
    private final ToggleGroup group = new ToggleGroup();
    private final double rate;
    private final OnChange onChange;
    ReturnSelector(GridPane root, int index, String name, double rate, String event, OnChange onChange) {
        this.rate = rate;
        this.onChange = onChange;
        configureDescribeLabel(root, index, name, rate);
        configureRadioButton(root, index, event);
    }

    private void configureDescribeLabel(GridPane root, int index, String name, double rate) {
        Label nameLabel = new Label(name);
        Label rateLabel = new Label("概率" + rate);
        root.addRow(index, nameLabel, rateLabel);
    }

    private void configureRadioButton(GridPane root, int index, String event) {
        RadioButton yesBtn = new RadioButton(event);
        RadioButton noBtn = new RadioButton("不" + event);
        RadioButton defaultBtn = new RadioButton("未指定");

        yesBtn.setToggleGroup(group);
        defaultBtn.setToggleGroup(group);
        noBtn.setToggleGroup(group);

        defaultBtn.setSelected(true);
        group.selectedToggleProperty().addListener((obs, old, val) -> onChange.call());

        root.addRow(index, yesBtn, defaultBtn, noBtn);
    }

    public Boolean getReturn() {
        return switch (group.getToggles().indexOf(group.getSelectedToggle())) {
            case 0 -> true;
            case 2 -> false;
            default -> null;
        };
    }

    public double getCurrentRate() {
        return switch (group.getToggles().indexOf(group.getSelectedToggle())) {
            case 0 -> rate / 100;
            case 2 -> 1 - rate / 100;
            default -> 1;
        };
    }
}
