package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterIcon;
import com.mllfjn.simyys.utils.Utils;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MultiplayerHpManager {
    public static void addTo(ContextMenu contextMenu, Character character) {
        Text text = new Text("设置血量百分比");
        TextField textField = new TextField();
        Button button = new Button("确定");
        button.setOnAction(event -> {
            double number = Utils.parseDoubleOrDefault(textField.getText(), -1);
            if (number >= 0) {
                character.setHp(character.getMaxHp() * number / 100);
                character.doIfCharacterIconExist(CharacterIcon::update);
            }
        });
        HBox hBox = new HBox(text, textField, button);
        MenuItem item = new CustomMenuItem(hBox);
        contextMenu.getItems().addAll(new SeparatorMenuItem(), item);
    }
}
