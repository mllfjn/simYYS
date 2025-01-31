package com.mllfjn.simyys.customnode;

import com.mllfjn.simyys.character.CharacterFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NameChooser extends LabelChooser {
    public NameChooser() {
        super("选择式神", CharacterFactory.characterType, CharacterFactory.characterList);

    }
}
