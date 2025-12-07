package com.mllfjn.simyys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.character.yuhun.list.HuoLing;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.guihuo.GuiHuo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamPane implements Serializable {
    private transient HBox center;
    private transient BorderPane root;

    public final List<Character> characters = new ArrayList<>();
    private final Character[] autos = new Character[2];
    private GuiHuo guiHuo;
    private boolean mobTeam = true;

    public Pane getPane() {
        if (root == null) {
            center = new HBox();
            center.setSpacing(5);
            center.setPadding(new Insets(5));
            center.setAlignment(Pos.CENTER);
            for (Character character : characters) {
                center.getChildren().add(character.getCharacterIcon());
                if (character == autos[0]) {
                    character.getCharacterIcon().setIsAuto(FlagChangeInfo.FlagType.GREEN, true);
                } else if (character == autos[1]) {
                    character.getCharacterIcon().setIsAuto(FlagChangeInfo.FlagType.RED, true);
                }
            }

            root = new BorderPane();
            root.setCenter(center);
            if (guiHuo != null) {
                root.setBottom(guiHuo.getGuiHuoDisplay());
            }
        }
        return root;
    }

    public void setAuto(Character characterSelected, FlagChangeInfo.FlagType flagType) {
        Character auto = autos[flagType.index];

        // 如果没标任何人，给选择目标设置标
        if (auto == null) {
            autos[flagType.index] = characterSelected;
            characterSelected.doIfCharacterIconExist(
                    characterIcon -> characterIcon.setIsAuto(flagType, true));
        } else {
            // 如果已有标，先取消标
            auto.doIfCharacterIconExist(characterIcon -> characterIcon.setIsAuto(flagType, false));
            // 如果已有标，且和选择的目标是一个，置null
            if (auto == characterSelected) {
                autos[flagType.index] = null;
            } else {
                // 如果已有标，且和选择的目标不是一个，换到新目标
                autos[flagType.index] = characterSelected;
                characterSelected.doIfCharacterIconExist(
                        characterIcon -> characterIcon.setIsAuto(flagType, true));
            }
        }
    }

    public Optional<Character> getAuto(FlagChangeInfo.FlagType flagType) {
        return Optional.ofNullable(autos[flagType.index]);
    }

    public void addCharacter(Character character) {
        characters.add(character);
        if (center != null) {
            center.getChildren().add(character.getCharacterIcon());
        }
        if (mobTeam && !character.isMob()) {
            guiHuo = new GuiHuo(4);
            if (root != null) {
                root.setBottom(guiHuo.getGuiHuoDisplay());
            }
            mobTeam = false;
        }
    }

    public void removeCharacter(Character character) {
        characters.remove(character);
        if (center != null) {
            center.getChildren().remove(character.getCharacterIcon());
        }

        for (int i = 0; i < autos.length; i++) {
            if (autos[i] == character) {
                autos[i] = null;
            }
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

    public int getGuiHuoCount() {
        return guiHuo.getNow();
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
        if (!mobTeam) {
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

    public boolean isMobTeam() {
        return mobTeam;
    }
}