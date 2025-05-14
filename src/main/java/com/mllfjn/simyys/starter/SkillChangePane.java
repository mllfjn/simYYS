package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.customnode.CustomLabel;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import com.mllfjn.simyys.starter.singleLine.SkillLine;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SkillChangePane extends VBox {
    public SkillChangePane() {
        super();

        HBox controller = new HBox();
        CustomLabel nameLabel = new CustomLabel("名称");
        CustomLabel timesToActLabel = new CustomLabel("行动次数");
        CustomLabel lockSkillLabel = new CustomLabel("锁定技能");
        Button addLine = new Button("添加更改技能");
        addLine.setPrefSize(100, 25);
        addLine.setOnAction(event -> addNewLine());

        controller.getChildren().addAll(nameLabel, timesToActLabel, lockSkillLabel, addLine);
        this.getChildren().add(controller);
    }

    public void addNewLine(SkillChangeInfo skillChangeInfo) {
        SkillLine newLine = addNewLine();
        newLine.fillData(skillChangeInfo);
    }
    private SkillLine addNewLine() {
        SkillLine newLine = new SkillLine(this);
        this.getChildren().add(newLine);
        return newLine;
    }
    public SkillChangeInfo[] getInfo() {
        if (this.getChildren().size() > 1) {
            SkillChangeInfo[] info = new SkillChangeInfo[this.getChildren().size() - 1];
            for (int i = 1; i < this.getChildren().size(); i++) {
                info[i - 1] = ((SkillLine) this.getChildren().get(i)).getSkillChangeInfo();
            }
            return info;
        } else return null;
    }
}
