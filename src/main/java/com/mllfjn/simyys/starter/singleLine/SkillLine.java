package com.mllfjn.simyys.starter.singleLine;

import com.mllfjn.simyys.customnode.CustomTextField;
import com.mllfjn.simyys.customnode.NameChooser;
import com.mllfjn.simyys.starter.SkillChangePane;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class SkillLine extends HBox {
    NameChooser name = new NameChooser();
    CustomTextField timesToAct = new CustomTextField();
    CustomTextField lockSkill = new CustomTextField();
    public SkillLine(SkillChangePane skillChangePane) {
        super();
        Button deleteButton = new Button("删除");
        deleteButton.setOnAction(actionEvent -> skillChangePane.getChildren().remove(this));
        deleteButton.setPrefSize(100, 25);

        this.getChildren().addAll(name, timesToAct, lockSkill, deleteButton);
    }

    public void fillData(SkillChangeInfo skillChangeInfo) {
        this.name.setText(skillChangeInfo.name);
        this.timesToAct.setText(skillChangeInfo.timesToAct);
        this.lockSkill.setText(skillChangeInfo.lockSkill);
    }

    public SkillChangeInfo getSkillChangeInfo() {
        return new SkillChangeInfo(name.getText(),
                timesToAct.getText(),
                lockSkill.getText());
    }
}
