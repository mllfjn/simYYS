package com.mllfjn.simyys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterIcon;
import com.mllfjn.simyys.character.yuhun.HuoLing;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.guihuo.GuiHuo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TeamPane implements Serializable {
    private transient HBox center;
    private transient BorderPane root;

    public final List<Character> characters = new ArrayList<>();
    private Character auto;
    private GuiHuo guiHuo;

//    public TeamPane() {
//        setupUI();
//            this.minHeightProperty().bind(heightProperty.divide(2).subtract(5));
//            setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
//    }

    public Pane getPane() {
        if (root == null) {
            center = new HBox();
            center.setSpacing(5);
            center.setPadding(new Insets(5));
            center.setAlignment(Pos.CENTER);
            for (Character character : characters) {
                center.getChildren().add(character.getCharacterIcon(this::setAuto));
            }

            root = new BorderPane();
            root.setCenter(center);
            if (guiHuo != null) {
                root.setBottom(guiHuo.getGuiHuoDisplay());
            }
        }
        return root;
    }

    public void setAuto(Character characterSelected) {
        // 如果没标任何人，给选择目标设置标
        if (auto == null) {
            auto = characterSelected;
            auto.characterIcon.setIsAuto(true);
        } else {
            // 如果已有标，先取消标
            auto.characterIcon.setIsAuto(false);
            // 如果已有标，且和选择的目标是一个，置null
            if (auto == characterSelected) {
                auto = null;
            } else {
                // 如果已有标，且和选择的目标不是一个，换到新目标
                auto = characterSelected;
                characterSelected.characterIcon.setIsAuto(true);
            }
        }
    }

    public Optional<Character> getAuto() {
        return Optional.ofNullable(auto);
    }

    public void addCharacter(Character character) {
        characters.add(character);
        if (center != null) {
            center.getChildren().add(character.getCharacterIcon(this::setAuto));
        }
        if (guiHuo == null && !character.isMob()) {
            guiHuo = new GuiHuo(4);
            root.setBottom(guiHuo.getGuiHuoDisplay());
        }
    }

    public void removeCharacter(Character character) {
        characters.remove(character);
        center.getChildren().remove(character.characterIcon);
        if (auto == character) {
            auto = null;
        }
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void reset(BattlePane bp) {
        for (Character character : characters) {
            character.setBattlePane(bp);
        }
    }

    public boolean canUseGuiHuo(int num) {
        return guiHuo.canUseGuiHuo(num);
    }

    public void useGuiHuo(int num) {
        guiHuo.useGuiHuo(num);
    }

    public void gainGuiHuo(int num) {
        guiHuo.gainGuiHuo(num);
    }

    public void addProgress() {
        guiHuo.addProgress();
    }

    public boolean canSummon() {
        for (Character character : characters) {
            if (character.isSummon()) {
                return false;
            }
        }
        return true;
    }

    public void init() {
        if (guiHuo != null) {
            for (Character character : characters) {
                for (YuHun yuHun : character.getYuHunSet()) {
                    if (yuHun instanceof HuoLing) {
                        gainGuiHuo(3);
                        return;
                    }
                }
            }
        }
    }
}