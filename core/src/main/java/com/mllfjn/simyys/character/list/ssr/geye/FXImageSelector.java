package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.function.Supplier;

class FXImageSelector extends StackPane {
    private static final String SelectedString = "幻化中";
    private static final String UnselectedString = "解除";


    private boolean nowSelected;


    public FXImageSelector(List<Character> characters, Character character, boolean initSelected,
                           Supplier<Boolean> canAdd
    ) {
        Circle border = new Circle(CharacterFactory.ImageSize.BIG.size / 2);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.GOLD);
        border.setStrokeWidth(5);

        this.nowSelected = initSelected;
        if (!nowSelected) {
            border.setVisible(false);
        }

        Text text;
        if (initSelected) {
            text = new Text(SelectedString);
            StackPane.setAlignment(text, Pos.BOTTOM_CENTER);
            text.setFont(new Font(20));
            text.setFill(Color.WHITE);
            text.setStroke(Color.ORANGE);
            text.setStrokeWidth(1);
            text.setMouseTransparent(true);
        } else {
            text = null;
        }

        Node image = CharacterFactory.getImage(character.name, CharacterFactory.ImageSize.BIG);
        image.setOnMouseClicked(event -> {
            if (canAdd.get()) {
                this.nowSelected = true;
                border.setVisible(true);
                if (initSelected) {
                    characters.remove(character);
                    text.setText(SelectedString);
                } else {
                    characters.add(character);
                }
            }
        });

        border.setOnMouseClicked(event -> {
            this.nowSelected = false;
            border.setVisible(false);
            if (initSelected) {
                text.setText(UnselectedString);
                characters.add(character);
            } else {
                characters.remove(character);
            }
        });

        this.getChildren().addAll(image, border);
        if (text != null) {
            this.getChildren().add(text);
        }
    }
}
