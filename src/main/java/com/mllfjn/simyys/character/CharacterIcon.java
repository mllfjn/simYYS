package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.state.Displayable;
import com.mllfjn.simyys.state.State;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;

public class CharacterIcon extends VBox {
    public final Character character;
    private final Label stateLabel = new Label();
    private final ProgressBar healthBar = new ProgressBar();
    private final ComboBox<Skill> skillBox;
    private final Label[] info = new Label[8];
    private final ImageView autoTo;

    public CharacterIcon(Character character, OnClickListener onClickListener) {
        super();
        this.character = character;
        this.setPadding(new Insets(0, 0, 40, 0));
        this.setAlignment(Pos.BOTTOM_CENTER);

        // 从下到上，分别是信息，技能选择，头像，生命，（盾），状态栏
        Pane image = CharacterFactory.getImageByName(character.name, CharacterFactory.ImageSize.LARGE, Color.ORANGE, 5);
        image.setOnMouseClicked(event -> onClickListener.onClick(this.character));

        ObservableList<Skill> skills = character.getSkills();
        skillBox = new ComboBox<>(skills);
        skills.stream()
                .filter(skill -> skill.getSkillID() == character.getLockSkill())
                .findFirst()
                .ifPresentOrElse(
                        skill -> skillBox.getSelectionModel().select(skill),
                        () -> skillBox.getSelectionModel().select(0)
                );

        skillBox.valueProperty().addListener((obs, old, val) -> character.setLockSkill(val.getSkillID()));

        healthBar.setMaxWidth(Double.MAX_VALUE);
        skillBox.setMaxWidth(Double.MAX_VALUE);
        stateLabel.setMaxWidth(Double.MAX_VALUE);

        if (character.team % 2 == 0) {
            healthBar.setStyle("-fx-accent: orange");
            URL url = CharacterFactory.class.getResource("images/绿标.png");
            this.autoTo = new ImageView(String.valueOf(url));
        } else {
            healthBar.setStyle("-fx-accent: red");
            URL url = CharacterFactory.class.getResource("images/红标.png");
            this.autoTo = new ImageView(String.valueOf(url));
        }
        setIsAuto(false);
        this.getChildren().add(0, autoTo);


        this.getChildren().addAll(
                this.stateLabel,
                this.healthBar,
                image,
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
        info[1].setText("生命:" + String.format("%.2f(%.2f%%)", character.getHp(), character.getHp() / character.getMaxHp() * 100));
        info[2].setText("防御:" + String.format("%.2f", character.getDefense()));
        info[3].setText("速度:" + String.format("%.2f", character.getSpeed()));
        info[4].setText("暴击:" + String.format("%.2f", character.getCritRate()));
        info[5].setText("爆伤:" + String.format("%.2f", character.getCritPower()));
        info[6].setText("命中:" + String.format("%.2f", character.getEffectHitRate()));
        info[7].setText("抵抗:" + String.format("%.2f", character.getEffectResistRate()));

        this.healthBar.setProgress(character.getHp() / character.getMaxHp());

        updateState();
    }

    public void updateState() {
        StringBuilder sb = new StringBuilder();
        for (State state : character.getStates()) {
            if (state instanceof Displayable d) {
                sb.append(d.getText()).append(" ");
            }
        }
        this.stateLabel.setText(sb.toString());
    }


    public interface OnClickListener {
        void onClick(Character character);
    }

    public void setIsAuto(boolean visible) {
        autoTo.setVisible(visible);
    }
}
