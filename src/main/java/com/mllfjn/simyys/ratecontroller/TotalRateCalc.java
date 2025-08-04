package com.mllfjn.simyys.ratecontroller;

import javafx.scene.control.Label;

import java.text.DecimalFormat;

public class TotalRateCalc extends Label{
    private double currentRate = 1;
    private final DecimalFormat df = new DecimalFormat("0.00#%");
    public TotalRateCalc() {
        super();
    }

    public void add(double rate) {
        currentRate *= rate;
        display();
    }

    public void setRate(double rate) {
        currentRate = rate;
        display();
    }

    public double getRate() {
        return currentRate;
    }

    private void display() {
        if (currentRate == 1) {
            this.setText("");
        } else {
            this.setText("当前概率：" + df.format(currentRate));
        }
    }
}
