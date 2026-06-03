package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.utils.DecimalFormatUtil;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;

public class RateCalc {
    private double currentRate = 1;
    // 主开关
    public final BooleanProperty mainSwitch;

    // 暴击
    private final BooleanProperty controlCrit;
    // 命中
    private final BooleanProperty controlEffectHit;
    // 协战
    private final BooleanProperty controlXieZhan;
    // 其他是否
    private final BooleanProperty controlWhetherOther;
    // 选取
    private final BooleanProperty controlChoose;
    // 御魂
    private final BooleanProperty controlYuHun;
    // 波动
    private final RangeControlMenuItem rangeController;


    // 显示概率
    private final Label label = new Label();
    // 控制波动
    private final CheckBox control = new CheckBox("概率控制模式");

    public RateCalc() {
        mainSwitch = control.selectedProperty();

        CustomCheckMenuItem ccm1 = new CustomCheckMenuItem("控制暴击");
        controlCrit = ccm1.selectedProperty();

        CustomCheckMenuItem ccm2 = new CustomCheckMenuItem("控制命中");
        controlEffectHit = ccm2.selectedProperty();

        CustomCheckMenuItem ccm3 = new CustomCheckMenuItem("控制协战");
        controlXieZhan = ccm3.selectedProperty();

        CustomCheckMenuItem ccm4 = new CustomCheckMenuItem("控制其他是否");
        controlWhetherOther = ccm4.selectedProperty();

        CustomCheckMenuItem ccm5 = new CustomCheckMenuItem("控制选取");
        controlChoose = ccm5.selectedProperty();

        CustomCheckMenuItem ccm6 = new CustomCheckMenuItem("控制御魂");
        controlYuHun = ccm6.selectedProperty();

        rangeController = new RangeControlMenuItem("控制波动");

        control.setContextMenu(new ContextMenu(ccm1, ccm2, ccm3, ccm4, ccm5, ccm6, rangeController));
    }


    public void change(double rate) {
        currentRate *= rate;
        refresh();
    }

    private void refresh() {
        if (currentRate == 1) {
            label.setText("");
        } else {
            label.setText("当前概率：" + DecimalFormatUtil.df_2_1_percent.format(currentRate));
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
        return mainSwitch.get() && controlCrit.get();
    }

    public boolean isControlEffectHit() {
        return mainSwitch.get() && controlEffectHit.get();
    }

    public boolean isControlChoose() {
        return mainSwitch.get() && controlChoose.get();
    }

    public boolean isControlWhetherOther() {
        return mainSwitch.get() && controlWhetherOther.get();
    }

    public boolean isControlYuHun() {
        return mainSwitch.get() && controlYuHun.get();
    }

    public boolean isControlXieZhan() {
        return mainSwitch.get() && controlXieZhan.get();
    }

    public boolean isControlFluctuation() {
        return mainSwitch.get() && rangeController.selectedProperty().get();
    }

    public double getFluctuation() {
        return rangeController.getValue();
    }
}