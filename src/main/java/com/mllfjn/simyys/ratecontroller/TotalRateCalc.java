package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.utils.DecimalFormatUtil;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.Serializable;
import java.text.DecimalFormat;

public class TotalRateCalc {
    private double currentRate = 1;
    private transient Label label;
    public void add(double rate) {
        currentRate *= rate;
        refresh();
    }

    public Node getNode() {
        if (label == null) {
            label = new Label();
        }
        return label;
    }

    public double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
        refresh();
    }

    private void refresh() {
        if (currentRate == 1) {
            label.setText("");
        } else {
            label.setText("当前概率：" + DecimalFormatUtil.df_2_1.format(currentRate));
        }
    }
}