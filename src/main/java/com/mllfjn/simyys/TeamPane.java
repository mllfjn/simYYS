package com.mllfjn.simyys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterIcon;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.character.yuhun.list.HuoLing;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.guihuo.GuiHuo;
import com.mllfjn.simyys.guihuo.SubstituteProvider;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamPane implements Serializable {
    private transient GridPane center;
    private transient BorderPane root;

    public final List<Character> characters = new ArrayList<>();
    private final Character[] autos = new Character[2];
    private GuiHuo guiHuo;
    private boolean mobTeam = true;

    public Pane getPane() {
        if (root == null) {
            center = new GridPane();
            center.setAlignment(Pos.CENTER);
            for (Character character : characters) {
                CharacterIcon characterIcon = character.getCharacterIcon();
                int index = center.getColumnCount();

                VBox top = characterIcon.getTop();
                GridPane.setVgrow(top, Priority.ALWAYS);

                center.add(top, index, 0);
                center.add(characterIcon.getCenter(), index, 1);
                center.add(characterIcon.getBottom(), index, 2);

                if (character == autos[0]) {
                    characterIcon.setIsAuto(FlagChangeInfo.FlagType.GREEN, true);
                } else if (character == autos[1]) {
                    characterIcon.setIsAuto(FlagChangeInfo.FlagType.RED, true);
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
            CharacterIcon characterIcon = character.getCharacterIcon();
//            int index = ;
            center.addColumn(center.getColumnCount(), characterIcon.getTop(), characterIcon.getCenter(), characterIcon.getBottom());
//            center.add(characterIcon.getTop(), index, 0);
//            center.add(characterIcon.getCenter(), index, 1);
//            center.add(characterIcon.getBottom(), index, 2);
        }
        if (mobTeam && !character.isMob() && !character.isSummon()) {
            guiHuo = new GuiHuo(2);
            if (root != null) {
                root.setBottom(guiHuo.getGuiHuoDisplay());
            }
            mobTeam = false;
        }
    }

    public void removeCharacter(Character character) {
        if (characters.contains(character)) {
            characters.remove(character);
            if (center != null) {
                CharacterIcon icon = character.getCharacterIcon();
                center.getChildren().removeAll(icon.getTop(), icon.getCenter(), icon.getBottom());
            }
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
            character.reset(bp);
        }
    }

    public void setSubstituteProvider(SubstituteProvider substituteProvider) {
        guiHuo.setSubstituteProvider(substituteProvider);
    }

    public boolean canUseGuiHuo(int num) {
        return guiHuo.canUseGuiHuo(num);
    }

    public void useGuiHuo(BattlePane bp, Character character, int num) {
        guiHuo.useGuiHuo(bp, character, num);
    }

    public void gainGuiHuo(int num) {
        guiHuo.gainGuiHuo(num, false);
    }

    public void gainGuiHuoFromYuHun(int num) {
        guiHuo.gainGuiHuo(num, true);
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

    public void calHuoLing() {
        if (!mobTeam) {
            for (Character character : characters) {
                for (YuHun yuHun : character.getYuHunSet()) {
                    if (yuHun instanceof HuoLing hl) {
                        hl.action(this);
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