package com.mllfjn.simyys.ratecontroller;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class ReturnSelector extends HBox {
    private final ToggleGroup group = new ToggleGroup();
    public ReturnSelector(String name, double rate, String effect) {
        super();
        configureDescribeLabel(name, rate);
        configureRadioButton(effect);
    }

    private void configureDescribeLabel(String name, double rate) {
        Label nameLabel = new Label(name);
        Label rateLabel = new Label("概率" + rate);
        this.getChildren().addAll(nameLabel, rateLabel);
    }

    private void configureRadioButton(String effect) {
        RadioButton yesBtn = new RadioButton(effect);
        RadioButton noBtn = new RadioButton("不" + effect);
        RadioButton defaultBtn = new RadioButton("未指定");

        yesBtn.setToggleGroup(group);
        defaultBtn.setToggleGroup(group);
        noBtn.setToggleGroup(group);

        this.getChildren().addAll(yesBtn, defaultBtn, noBtn);
    }

    public Return getReturn() {
        return switch (group.getToggles().indexOf(group.getSelectedToggle())) {
            case 0 -> Return.YES;
            case 2 -> Return.NO;
            default -> Return.DEFAULT;
        };
    }
}
