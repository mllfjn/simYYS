package com.mllfjn.simyys.starter.singleLine;

import com.mllfjn.simyys.customnode.CustomTextField;
import com.mllfjn.simyys.customnode.LabelChooser;
import com.mllfjn.simyys.customnode.NameChooser;
import com.mllfjn.simyys.customnode.SpecialChooser;
import com.mllfjn.simyys.starter.CharacterPane;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class CharacterLine extends HBox {
    public final NameChooser name = new NameChooser();
    final CustomTextField speed = new CustomTextField();
    final CustomTextField baseAttack = new CustomTextField();
    final CustomTextField yuHunAttack = new CustomTextField();
    final CustomTextField team = new CustomTextField();
    final CustomTextField hp = new CustomTextField();
    final CustomTextField defense = new CustomTextField();
    final CustomTextField critRate = new CustomTextField();
    final CustomTextField critPower = new CustomTextField();
    final CustomTextField effectHitRate = new CustomTextField();
    final CustomTextField effectResistRate = new CustomTextField();
    final LabelChooser[] special = new LabelChooser[3];
    public CharacterLine(CharacterPane characterPane) {
        super();
        Button deleteButton = new Button("删除");
        deleteButton.setOnAction(actionEvent -> characterPane.getChildren().remove(this));
        deleteButton.setPrefSize(75, 25);

        this.getChildren().addAll(deleteButton, name, speed, baseAttack, yuHunAttack, team, hp, defense, critRate, critPower, effectHitRate, effectResistRate);

        for (int i = 0; i < special.length; i++) {
            special[i] = new SpecialChooser(this);
            this.getChildren().add(special[i]);
        }
    }
    public void fillData(CharacterInfo info) {
        this.name.setText(info.name());
        this.speed.setText(info.speed());
        this.baseAttack.setText(info.baseAttack());
        this.yuHunAttack.setText(info.yuHunAttack());
        this.team.setText(info.team());
        this.hp.setText(info.hp());
        this.defense.setText(info.defense());
        this.critRate.setText(info.critRate());
        this.critPower.setText(info.critPower());
        this.effectHitRate.setText(info.effectHitRate());
        this.effectResistRate.setText(info.effectResistRate());
        for (int i = 0; i < info.special().size(); i++) {
            if (i >= special.length || info.special().get(i) == null) {
                break;
            }
            special[i].setText(info.special().get(i));
        }

        resetSpecial();

    }

    public CharacterInfo getCharacterInfo() {
        ArrayList<String> specialText = new ArrayList<>();
        for (LabelChooser labelChooser : this.special) {
            specialText.add(labelChooser.getText());
        }
        return new CharacterInfo(name.getText(),
                speed.getText(),
                baseAttack.getText(),
                yuHunAttack.getText(),
                team.getText(),
                hp.getText(),
                defense.getText(),
                critRate.getText(),
                critPower.getText(),
                effectHitRate.getText(),
                effectResistRate.getText(),
                specialText);
    }

    public void resetSpecial() {
        String[] existText = new String[3];
        int count = 0;
        for (int i = 0; i < special.length; i++) {
            if (!special[i].getText().isEmpty() && !special[i].getText().equals("+")) {
                existText[i] = special[i].getText();
                count++;
            }
        }

        for (int i = 0; i < special.length; i++) {
            if (i < count) {
                special[i].setText(existText[i]);
            } else if (i == count) {
                special[i].setText("+");
            } else {
                special[i].setText("");
            }
        }
    }
}
