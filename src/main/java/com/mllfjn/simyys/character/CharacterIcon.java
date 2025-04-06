package com.mllfjn.simyys.character;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CharacterIcon extends VBox {
    private final Character character;
    Label stateLabel = new Label();
    ProgressBar healthBar = new ProgressBar();
    ComboBox<String> skillBox = new ComboBox<>();
    Pane image;
    Label[] info = new Label[8];

    public CharacterIcon(Character character, OnClickListener onClickListener) {
        super();
        this.character = character;
        this.setPadding(new Insets(0, 0, 40, 0));
        this.setAlignment(Pos.BOTTOM_CENTER);


        // 从下到上，分别是信息，技能选择，头像，生命，（盾），状态栏
        this.image = CharacterFactory.getImageByName(character.name, CharacterFactory.ImageSize.LARGE, Color.ORANGE, 5);
        image.setOnMouseClicked(event -> {
            onClickListener.onClick(character);
        });

        skillBox.valueProperty().addListener((obs, old, val) -> {
            character.setLockSkill(skillBox.getSelectionModel().getSelectedIndex());
        });
        skillBox.itemsProperty().bind(character.getSkillListProperty());
        skillBox.getSelectionModel().select(0);

        healthBar.setMaxWidth(Double.MAX_VALUE);
        skillBox.setMaxWidth(Double.MAX_VALUE);
        stateLabel.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().addAll(
                this.stateLabel ,
                this.healthBar,
                this.image,
                this.skillBox
        );
        for (int i = 0; i < info.length; i++) {
            info[i] = new Label();
            info[i].setMaxWidth(Double.MAX_VALUE);
            this.getChildren().add(info[i]);
        }

        update();
    }

    public void update() {
        info[0].setText("攻击:" + String.format("%.2f", character.getAttack()));
        info[1].setText("生命:" + String.format("%.2f(%.2f%%)", character.getHp(), character.getHp()/ character.getMaxHp() * 100));
        info[2].setText("防御:" + String.format("%.2f", character.getDefense()));
        info[3].setText("速度:" + String.format("%.2f", character.getSpeed()));
        info[4].setText("暴击:" + String.format("%.2f", character.getCritRate()));
        info[5].setText("爆伤:" + String.format("%.2f", character.getCritPower()));
        info[6].setText("命中:" + String.format("%.2f", character.getEffectHitRate()));
        info[7].setText("抵抗:" + String.format("%.2f", character.getEffectResistRate()));

        this.healthBar.setProgress(character.getHp() / character.getMaxHp() );
        this.stateLabel.setText(character.alive ? "存活" : "死亡");
    }

    public interface OnClickListener {
        void onClick(Character character);
    }
}
