package com.mllfjn.simyys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BattlePane {
    CharacterInfo[] characterInfo;
    SkillChangeInfo[] skillChangeInfo;
    FlagChangeInfo[] flagChangeInfo;
    List<Character> characters;
    public BattlePane(Stage stage, CharacterInfo[] characterInfo, SkillChangeInfo[] skillChangeInfo, FlagChangeInfo[] flagChangeInfo) {
        this.characterInfo = characterInfo;
        this.skillChangeInfo = skillChangeInfo;
        this.flagChangeInfo = flagChangeInfo;
        this.characters = new ArrayList<>();
        init();
    }

    private void init() {
        for (CharacterInfo info : characterInfo) {
            if (Integer.parseInt(info.team) == 0 || Integer.parseInt(info.team) == 1) {
                characters.add(CharacterFactory.createCharacter(info));
            }
        }
    }
}
