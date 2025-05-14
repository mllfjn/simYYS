package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.customnode.CustomLabel;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;
import com.mllfjn.simyys.starter.singleLine.FlagLine;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FlagChangePane extends VBox {
    public FlagChangePane() {
        super();

        HBox controller = new HBox();
        CustomLabel name = new CustomLabel("名称");
        CustomLabel timesToActLabel = new CustomLabel("行动次数");
        CustomLabel redFlagLabel = new CustomLabel("红标");
        CustomLabel greenFlagLabel = new CustomLabel("绿标");

        Button addLine = new Button("添加换标");
        addLine.setPrefSize(75, 25);
        addLine.setOnAction(event -> addNewLine());

        controller.getChildren().addAll(name, timesToActLabel, redFlagLabel, greenFlagLabel, addLine);
        this.getChildren().add(controller);
    }

    public void addNewLine(FlagChangeInfo flagChangeInfo) {
        FlagLine newLine = addNewLine();
        newLine.fillData(flagChangeInfo);
    }
    private FlagLine addNewLine() {
        FlagLine newLine = new FlagLine(this);
        this.getChildren().add(newLine);
        return newLine;
    }

    public FlagChangeInfo[] getInfo() {
        if (this.getChildren().size() > 1) {
            FlagChangeInfo[] info = new FlagChangeInfo[this.getChildren().size() - 1];
            for (int i = 1; i < this.getChildren().size(); i++) {
                info[i - 1] = ((FlagLine) this.getChildren().get(i)).getFlagChangeInfo();
            }
            return info;
        } else return null;
    }
}
