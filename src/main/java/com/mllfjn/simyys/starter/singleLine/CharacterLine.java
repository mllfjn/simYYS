package com.mllfjn.simyys.starter.singleLine;

import com.mllfjn.simyys.customnode.CustomTextField;
import com.mllfjn.simyys.customnode.LabelChooser;
import com.mllfjn.simyys.customnode.NameChooser;
import com.mllfjn.simyys.starter.CharacterPane;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class CharacterLine extends HBox {
    NameChooser name = new NameChooser();
    CustomTextField speed = new CustomTextField();
    CustomTextField baseAttack = new CustomTextField();
    CustomTextField yuHunAttack = new CustomTextField();
    CustomTextField team = new CustomTextField();
    CustomTextField hp = new CustomTextField();
    CustomTextField defense = new CustomTextField();
    CustomTextField criticalRate = new CustomTextField();
    CustomTextField criticalMultiplier = new CustomTextField();
    CustomTextField mingZhong = new CustomTextField();
    CustomTextField diKang = new CustomTextField();
    LabelChooser[] special = new LabelChooser[3];
    public CharacterLine(CharacterPane characterPane) {
        super();
        Button deleteButton = new Button("删除");
        deleteButton.setOnAction(actionEvent -> characterPane.getChildren().remove(this));
        deleteButton.setPrefSize(75, 25);
        for (int i = 0; i < special.length; i++) {
            special[i] = new LabelChooser("");
        }

        this.getChildren().addAll(deleteButton, name, speed, baseAttack, yuHunAttack, team, hp, defense, criticalRate, criticalMultiplier, mingZhong, diKang);
    }
    public void fillData(CharacterInfo info) {
        this.name.setText(info.name);
        this.speed.setText(info.speed);
        this.baseAttack.setText(info.baseAttack);
        this.yuHunAttack.setText(info.yuHunAttack);
        this.team.setText(info.team);
        this.hp.setText(info.hp);
        this.defense.setText(info.defense);
        this.criticalRate.setText(info.criticalRate);
        this.criticalMultiplier.setText(info.criticalMultiplier);
        this.mingZhong.setText(info.mingZhong);
        this.diKang.setText(info.diKang);
        this.special
    }

    public CharacterInfo getCharacterInfo() {
        return new CharacterInfo(name.getText(),
                speed.getText(),
                baseAttack.getText(),
                yuHunAttack.getText(),
                team.getText(),
                hp.getText(),
                defense.getText(),
                criticalRate.getText(),
                criticalMultiplier.getText(),
                mingZhong.getText(),
                diKang.getText(),
                special);
    }
}
