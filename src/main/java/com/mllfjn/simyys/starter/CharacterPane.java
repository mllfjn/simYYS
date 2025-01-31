package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.customnode.CustomLabel;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.singleLine.CharacterLine;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CharacterPane extends VBox {
    public CharacterPane() {
        super();

        final int width = 75;
        final int height = 25;
        final String[] labelText = new String[]{"名称", "速度", "基础攻击", "御魂攻击", "队伍", "生命", "防御", "暴击率", "暴击伤害", "命中", "抵抗", "御魂/契灵/词条/其他"};

        HBox controller = new HBox();
        Button button = new Button("添加式神");
        button.setPrefSize(width, height);
        button.setOnAction(actionEvent -> addNewLine());
        controller.getChildren().add(button);

        for (int i = 0; i < labelText.length; i++) {
            CustomLabel label = new CustomLabel(labelText[i]);
            if (i == labelText.length - 1) {
                label.setPrefWidth(width * 3);
            }
            controller.getChildren().add(label);
        }

        this.getChildren().add(controller);
    }

    public void addNewLine(CharacterInfo characterInfo) {
        CharacterLine newLine = addNewLine();
        newLine.fillData(characterInfo);
    }

    private CharacterLine addNewLine() {
        CharacterLine newLine = new CharacterLine(this);
        this.getChildren().add(newLine);
        return newLine;
    }

    public CharacterInfo[] getInfo() {
        if (this.getChildren().size() > 1) {
            CharacterInfo[] info = new CharacterInfo[this.getChildren().size() - 1];
            for (int i = 1; i < this.getChildren().size(); i++) {
                info[i - 1] = ((CharacterLine) this.getChildren().get(i)).getCharacterInfo();
            }
            return info;
        } else return null;
    }
}
