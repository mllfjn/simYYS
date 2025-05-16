package com.mllfjn.simyys.starter.singleLine;

import com.mllfjn.simyys.customnode.CustomTextField;
import com.mllfjn.simyys.customnode.FlagChangeChooser;
import com.mllfjn.simyys.customnode.NameChooser;
import com.mllfjn.simyys.starter.FlagChangePane;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class FlagLine extends HBox {

    final NameChooser name = new NameChooser();
    final CustomTextField timesToAct = new CustomTextField();
    final FlagChangeChooser redFlag = new FlagChangeChooser();
    final FlagChangeChooser greenFlag = new FlagChangeChooser();
    public FlagLine(FlagChangePane flagChangePane) {
        super();
        Button deleteButton = new Button("删除");
        deleteButton.setPrefSize(75, 25);
        deleteButton.setOnAction(event -> flagChangePane.getChildren().remove(this));
        this.getChildren().addAll(name, timesToAct, redFlag, greenFlag, deleteButton);
    }

    public void fillData(FlagChangeInfo flagChangeInfo) {
        this.name.setText(flagChangeInfo.name());
        this.timesToAct.setText(flagChangeInfo.timesToAct());
        this.redFlag.setText(flagChangeInfo.redFlag());
        this.greenFlag.setText(flagChangeInfo.greenFlag());
    }

    public FlagChangeInfo getFlagChangeInfo() {
        return new FlagChangeInfo(name.getText(),
                timesToAct.getText(),
                redFlag.getText(),
                greenFlag.getText());
    }
}
