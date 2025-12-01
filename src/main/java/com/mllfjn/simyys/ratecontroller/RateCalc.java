package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.utils.DecimalFormatUtil;
import javafx.scene.Node;
import javafx.scene.control.*;

public class RateCalc {
    private double currentRate = 1;
    // 主开关
    public boolean mainSwitch = false;

    // 暴击
    private boolean controlCrit = true;
    // 命中
    private boolean controlEffectHit = true;
    // 其他是否
    private boolean controlWhetherOther = true;
    // 选取
    private boolean controlChoose = true;


    // 显示概率
    private final Label label = new Label();
    //
    private final CheckBox control = new CheckBox("概率控制模式");

    public RateCalc() {
        control.selectedProperty().addListener(
                (obs, old, val) -> mainSwitch = val);

        CheckMenuItem menuItemCrit = new CheckMenuItem("控制暴击");
        CheckMenuItem menuItemEffectHit = new CheckMenuItem("控制命中");
        CheckMenuItem menuItemOther = new CheckMenuItem("控制其他");
        CheckMenuItem menuItemChoose = new CheckMenuItem("控制选取");

        menuItemCrit.setSelected(true);
        menuItemEffectHit.setSelected(true);
        menuItemOther.setSelected(true);
        menuItemChoose.setSelected(true);

        menuItemCrit.selectedProperty().addListener(
                (obs, old, val) -> controlCrit = val);
        menuItemEffectHit.selectedProperty().addListener(
                (obs, old, val) -> controlEffectHit = val);
        menuItemOther.selectedProperty().addListener(
                (obs, old, val) -> controlWhetherOther = val);
        menuItemChoose.selectedProperty().addListener(
                (obs, old, val) -> controlChoose = val);

        control.setContextMenu(new ContextMenu(menuItemCrit, menuItemEffectHit, menuItemOther, menuItemChoose));
    }


    public void change(double rate) {
        currentRate *= rate;
        refresh();
    }

    private void refresh() {
        if (currentRate == 1) {
            label.setText("");
        } else {
            label.setText("当前概率：" + DecimalFormatUtil.df_2_1.format(currentRate));
        }
    }

    public Label getLabel() {
        return label;
    }

    public CheckBox getControl() {
        return control;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
        refresh();
    }

    public double getCurrentRate() {
        return currentRate;
    }

    public boolean isControlCrit() {
        return mainSwitch && controlCrit;
    }

    public boolean isControlEffectHit() {
        return mainSwitch && controlEffectHit;
    }

    public boolean isControlChoose() {
        return mainSwitch && controlChoose;
    }

    public boolean isControlWhetherOther() {
        return mainSwitch && controlWhetherOther;
    }
}